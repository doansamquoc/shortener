package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ExchangeTokenRequest(
	@NotBlank(message = "auth.exchange_code.required")
	String code
) {}
