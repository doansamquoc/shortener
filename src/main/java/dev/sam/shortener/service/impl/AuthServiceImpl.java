package dev.sam.shortener.service.impl;

import com.nimbusds.jose.JOSEException;
import dev.sam.shortener.cache.AuthTemporaryCode;
import dev.sam.shortener.cache.ForgotPasswordCache;
import dev.sam.shortener.cache.ResetTokenCache;
import dev.sam.shortener.cache.TokenBlacklist;
import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.CustomOAuth2User;
import dev.sam.shortener.dto.CustomUserDetails;
import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.request.*;
import dev.sam.shortener.dto.response.VerifyResetCodeResponse;
import dev.sam.shortener.entity.RefreshToken;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.event.ForgotPasswordEvent;
import dev.sam.shortener.event.UserRegisteredEvent;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.service.AuthService;
import dev.sam.shortener.service.JwtService;
import dev.sam.shortener.service.RefreshTokenService;
import dev.sam.shortener.service.UserService;
import dev.sam.shortener.util.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
	AppProperties props;
	JwtService jwtService;
	UserService userService;
	TokenBlacklist tokenBlacklist;
	PasswordEncoder passwordEncoder;
	ResetTokenCache resetTokenCache;
	AuthenticationManager authManager;
	AuthTemporaryCode authTemporaryCode;
	ApplicationEventPublisher publisher;
	ForgotPasswordCache forgotPasswordCache;
	RefreshTokenService refreshTokenService;

	private CustomUserDetails authenticate(String identifier, String password) throws AuthenticationException {
		try {
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
			return (CustomUserDetails) auth.getPrincipal();
		} catch (BadCredentialsException bce) {
			throw new BadCredentialsException("Bad credentials", bce);
		}
	}

	@Override
	@Transactional
	public TokenDto login(LoginRequest request) {
		CustomUserDetails customUser = authenticate(request.identifier(), request.password());
		User user = customUser.getUser();

		// Generate refresh token
		RefreshToken refreshToken = refreshTokenService.create(user);

		// Generate access token and return TokenDto
		return new TokenDto(generateToken(fromCustomUser(customUser)), refreshToken);
	}

	@Override
	@Transactional
	public TokenDto register(UserRegistrationRequest request) {
		String hashedPassword = passwordEncoder.encode(request.password());
		User user = userService.create(new UserRegistrationRequest(request.email(), request.username(), hashedPassword));

		RefreshToken refreshToken = refreshTokenService.create(user);
		String accessToken = generateToken(fromUser(user));

		publisher.publishEvent(new UserRegisteredEvent(user));
		return new TokenDto(accessToken, refreshToken);
	}

	@Override
	public void logout(Jwt jwt, String token) {
		if (jwt.getExpiresAt() == null) return;
		long remaining = jwt.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
		tokenBlacklist.add(jwt.getId(), remaining, TimeUnit.MILLISECONDS);

		// revoke the session's user
		refreshTokenService.revoke(token);
	}

	@Override
	public TokenDto exchangeToken(ExchangeTokenRequest request) {
		Object userObj = authTemporaryCode.get(request.code());

		CustomOAuth2User oauth2User = (CustomOAuth2User) userObj;
		authTemporaryCode.remove(request.code());

		User user = oauth2User.createUser();
		RefreshToken refreshToken = refreshTokenService.create(user);

		return new TokenDto(generateToken(fromOAuth2User(oauth2User)), refreshToken);
	}

	@Override
	public TokenDto refreshToken(String token) {
		RefreshToken refreshToken = refreshTokenService.findByToken(token);
		if (refreshToken.isRevoked() || refreshToken.isExpired()) throw AppException.of(ErrorCode.AUTH_UNAUTHORIZED);

		refreshTokenService.revoke(refreshToken);
		User user = refreshToken.getUser();
		RefreshToken newRefreshToken = refreshTokenService.create(user);

		return new TokenDto(generateToken(fromUser(user)), newRefreshToken);
	}

	@Override
	public void forgotPassword(String email) {
		userService.findByEmail(email).ifPresent(u -> {
			String code = Utils.generateCode();
			long duration = System.currentTimeMillis() + props.getForgotPasswordCodeExpiration();
			forgotPasswordCache.add(email, code, duration);
			publisher.publishEvent(new ForgotPasswordEvent(u, code));
		});
	}

	@Override
	public VerifyResetCodeResponse verifyResetPasswordCode(String email, String code) {
		String realCode = forgotPasswordCache.get(email);
		if (code == null || !code.equals(realCode)) throw AppException.of(ErrorCode.RESET_CODE_INVALID);

		forgotPasswordCache.remove(email);
		String resetToken = UUID.randomUUID().toString();

		long duration = System.currentTimeMillis() + props.getResetPasswordTokenExpiration();
		resetTokenCache.add(resetToken, email, duration);
		return new VerifyResetCodeResponse(resetToken);
	}

	@Override
	public void resetPassword(String token, ResetPasswordRequest request) {
		String email = resetTokenCache.get(token);
		if (email == null) throw AppException.of(ErrorCode.RESET_CODE_INVALID);
		userService.findByEmail(email).ifPresentOrElse(
			user -> {
				String hashedPassword = passwordEncoder.encode(request.password());
				user.setPassword(hashedPassword);
				userService.save(user);

				resetTokenCache.remove(email);
				refreshTokenService.revokeAll(user);
			},
			() -> {
				throw AppException.of(ErrorCode.RESET_CODE_INVALID);
			}
		);
	}

	private String generateToken(JwtCreationRequest request) {
		try {
			return jwtService.generateToken(request);
		} catch (JOSEException e) {
			throw AppException.of(ErrorCode.SERVER_INTERNAL);
		}
	}

	private JwtCreationRequest fromUser(User user) {
		Set<String> roles = user.getRoles().stream().map(role -> role.getRole().name()).collect(Collectors.toSet());
		return new JwtCreationRequest(user.getId(), user.getUsername(), roles);
	}

	private JwtCreationRequest fromCustomUser(CustomUserDetails user) {
		Set<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return new JwtCreationRequest(user.getId(), user.getUsername(), roles);
	}

	private JwtCreationRequest fromOAuth2User(CustomOAuth2User user) {
		Set<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return new JwtCreationRequest(user.getId(), user.getUsername(), roles);
	}
}
