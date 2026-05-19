package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyResetCodeRequest(
	@NotBlank(message = "user.email.required")
	@Email(message = "user.email.invalid")
	String email,

	@NotBlank(message = "user.reset_code.required")
	@Size(min = 6, max = 6, message = "user.reset_code.size")
	String code
) {}
