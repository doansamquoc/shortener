package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
	@NotBlank(message = "user.password.required")
	@Size(min = 6, max = 255, message = "user.password.size")
	String password
) {}
