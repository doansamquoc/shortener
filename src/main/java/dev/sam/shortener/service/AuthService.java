package dev.sam.shortener.service;

import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.request.LoginRequest;
import dev.sam.shortener.dto.request.UserRegistrationRequest;

public interface AuthService {
	TokenDto login(LoginRequest request);

	TokenDto register(UserRegistrationRequest request);
}
