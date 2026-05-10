package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
@NotBlank(message = "auth.identifier.required")
String identifier,

@NotBlank(message = "user.password.required")
String password
) {}
