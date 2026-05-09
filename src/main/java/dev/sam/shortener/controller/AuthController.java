package dev.sam.shortener.controller;

import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.TokenDto;
import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.request.*;
import dev.sam.shortener.dto.response.AuthResponse;
import dev.sam.shortener.dto.response.VerifyResetCodeResponse;
import dev.sam.shortener.service.AuthService;
import dev.sam.shortener.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static dev.sam.shortener.constant.AppConstant.REFRESH_TOKEN_COOKIE_NAME;
import static dev.sam.shortener.constant.AppConstant.REFRESH_TOKEN_COOKIE_PATH;
import static dev.sam.shortener.constant.EndpointConstant.V1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
	AuthService service;
	AppProperties props;

	@PostMapping("/login")
	private ResponseEntity<ApiResponse<AuthResponse>> login(
	@Valid @RequestBody LoginRequest request,
	HttpServletResponse servletResponse
	) {
		TokenDto token = service.login(request);
		AuthResponse response = authResponse(servletResponse, token);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/register")
	private ResponseEntity<ApiResponse<AuthResponse>> register(
	@Valid @RequestBody UserRegistrationRequest request,
	HttpServletResponse servletResponse
	) {
		TokenDto token = service.register(request);
		AuthResponse response = authResponse(servletResponse, token);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/logout")
	private ResponseEntity<ApiResponse<?>> logout(
	JwtAuthenticationToken auth,
	@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
	HttpServletResponse servletResponse
	) {
		Jwt jwt = auth.getToken();
		service.logout(jwt, refreshToken);
		CookieUtils.remove(servletResponse, REFRESH_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_PATH);
		return ResponseEntity.ok(ApiResponse.of("Logged out successfully"));
	}

	@PostMapping("/exchange-token")
	private ResponseEntity<ApiResponse<AuthResponse>> exchangeToken(
	@Valid @RequestBody ExchangeTokenRequest request,
	HttpServletResponse servletResponse
	) {
		TokenDto token = service.exchangeToken(request);
		AuthResponse response = authResponse(servletResponse, token);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/refresh")
	private ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
	@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshTokenCookie,
	HttpServletResponse servletResponse
	) {
		TokenDto token = service.refreshToken(refreshTokenCookie);
		AuthResponse response = authResponse(servletResponse, token);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/forgot-password")
	private ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		service.forgotPassword(request.email());
		return ResponseEntity.ok(ApiResponse.of("If the email valid, we'll send a verification email!"));
	}

	@PostMapping("/verify-reset-code")
	private ResponseEntity<ApiResponse<VerifyResetCodeResponse>> verifyResetCode(
	@Valid @RequestBody VerifyResetCodeRequest request
	) {
		VerifyResetCodeResponse response = service.verifyResetPasswordCode(request.email(), request.code());
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/reset-password")
	private ResponseEntity<ApiResponse<?>> resetPassword(
	@RequestParam(name = "token", required = false) String token,
	@Valid @RequestBody ResetPasswordRequest request
	) {
		service.resetPassword(token, request);
		return ResponseEntity.ok(ApiResponse.of("Password reset successfully"));
	}

	private void addRefreshTokenCookie(HttpServletResponse servletResponse, String refreshToken) {
		long maxAgeInSeconds = props.getRefreshTokenExpiration() / 1000;
		CookieUtils.add(servletResponse, REFRESH_TOKEN_COOKIE_NAME, refreshToken, maxAgeInSeconds, REFRESH_TOKEN_COOKIE_PATH);
	}

	private AuthResponse authResponse(HttpServletResponse servletResponse, TokenDto token) {
		String refreshToken = token.refreshToken().getToken();
		addRefreshTokenCookie(servletResponse, refreshToken);
		return new AuthResponse(token.accessToken());
	}
}
