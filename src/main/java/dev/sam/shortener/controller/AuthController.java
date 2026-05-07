package dev.sam.shortener.controller;

import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.request.LoginRequest;
import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.dto.response.AuthResponse;
import dev.sam.shortener.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
	AuthService service;

	@PostMapping("/login")
	private ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		TokenDto token = service.login(request);
		AuthResponse response = new AuthResponse(token.accessToken());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/register")
	private ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
		TokenDto token = service.register(request);
		AuthResponse response = new AuthResponse(token.accessToken());
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
