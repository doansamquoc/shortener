package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
	@NotBlank(message = "user.email.required")
	@Email(message = "user.email.invalid")
	String email
) {}
