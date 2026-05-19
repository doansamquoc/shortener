package dev.sam.shortener.service;

import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.request.ExchangeTokenRequest;
import dev.sam.shortener.dto.request.LoginRequest;
import dev.sam.shortener.dto.request.ResetPasswordRequest;
import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.dto.response.VerifyResetCodeResponse;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {
	TokenDto login(LoginRequest request);

	TokenDto register(UserRegistrationRequest request);

	void logout(Jwt jwt, String token);

	TokenDto exchangeToken(ExchangeTokenRequest request);

	TokenDto refreshToken(String token);

	void forgotPassword(String email);

	VerifyResetCodeResponse verifyResetPasswordCode(String email, String code);

	void resetPassword(String token, ResetPasswordRequest request);
}
