package dev.sam.shortener.service.impl;

import com.nimbusds.jose.JOSEException;
import dev.sam.shortener.dto.CustomUserDetails;
import dev.sam.shortener.dto.TokenDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
	JwtService jwtService;
	UserService userService;
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
		try {
			return new TokenDto(jwtService.createToken(user));
		} catch (JOSEException e) {
			throw AppException.of(ErrorCode.SERVER_INTERNAL);
		}
	}

	@Override
	public TokenDto register(UserRegistrationRequest request) {
		String hashedPassword = passwordEncoder.encode(request.password());
		User user = userService.create(new UserRegistrationRequest(request.email(), request.username(), hashedPassword));
		return login(new LoginRequest(user.getEmail(), request.password()));
	}
}
