package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
	@NotBlank(message = "user.email.required")
	@Email(message = "user.email.invalid")
	String email,

	@NotBlank(message = "user.username.required")
	@Size(min = 4, max = 16, message = "user.username.size")
	String username,

	@NotBlank(message = "user.password.required")
	@Size(min = 6, max = 255, message = "user.password.size")
	String password
) {}
