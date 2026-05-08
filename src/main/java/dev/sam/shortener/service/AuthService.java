package dev.sam.shortener.service;

import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.request.ExchangeTokenRequest;
import dev.sam.shortener.dto.request.LoginRequest;
import dev.sam.shortener.dto.request.UserRegistrationRequest;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {
	TokenDto login(LoginRequest request);

	TokenDto register(UserRegistrationRequest request);

	void logout(Jwt jwt);

	TokenDto exchangeToken(ExchangeTokenRequest request);
}
