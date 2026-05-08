package dev.sam.shortener.service.impl;

import com.nimbusds.jose.JOSEException;
import dev.sam.shortener.cache.AuthTemporaryCode;
import dev.sam.shortener.cache.TokenBlacklist;
import dev.sam.shortener.dto.CustomOAuth2User;
import dev.sam.shortener.dto.CustomUserDetails;
import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.request.ExchangeAuthCodeRequest;
import dev.sam.shortener.dto.request.JwtCreationRequest;
import dev.sam.shortener.dto.request.LoginRequest;
import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.service.AuthService;
import dev.sam.shortener.service.JwtService;
import dev.sam.shortener.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
	AuthTemporaryCode authTemporaryCode;
	JwtService jwtService;
	UserService userService;
	TokenBlacklist tokenBlacklist;
	PasswordEncoder passwordEncoder;
	AuthenticationManager authManager;

	private CustomUserDetails authenticate(String identifier, String password) throws AuthenticationException {
		try {
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
			return (CustomUserDetails) auth.getPrincipal();
		} catch (BadCredentialsException bce) {
			throw new BadCredentialsException("Bad credentials", bce);
		}
	}

	@Override
	public TokenDto login(LoginRequest request) {
		CustomUserDetails user = authenticate(request.identifier(), request.password());
		return generateToken(getJwtCreationRequest(user.getId(), user.getUsername(), user.getAuthorities()));
	}

	@Override
	public TokenDto register(UserRegistrationRequest request) {
		String hashedPassword = passwordEncoder.encode(request.password());
		User user = userService.create(new UserRegistrationRequest(request.email(), request.username(), hashedPassword));
		return login(new LoginRequest(user.getEmail(), request.password()));
	}

	@Override
	public void logout(Jwt jwt) {
		if (jwt.getExpiresAt() == null) return;
		long remaining = jwt.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
		tokenBlacklist.add(jwt.getId(), remaining, TimeUnit.MILLISECONDS);
	}

	@Override
	public TokenDto exchangeCode(ExchangeAuthCodeRequest request) {
		Object userObj = authTemporaryCode.get(request.code());
		CustomOAuth2User user = (CustomOAuth2User) userObj;
		authTemporaryCode.remove(request.code());
		return generateToken(getJwtCreationRequest(user.getId(), user.getUsername(), user.getAuthorities()));
	}

	private JwtCreationRequest getJwtCreationRequest(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
		Set<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return new JwtCreationRequest(id, username, roles);
	}

	private TokenDto generateToken(JwtCreationRequest request) {
		try {
			return new TokenDto(jwtService.generateToken(request));
		} catch (JOSEException e) {
			throw AppException.of(ErrorCode.SERVER_INTERNAL);
		}
	}
}
