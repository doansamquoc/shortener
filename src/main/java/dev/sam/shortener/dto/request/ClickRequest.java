package dev.sam.shortener.dto.request;

import lombok.Builder;

@Builder
public record ClickRequest(
	String ipAddress,
	String userAgent,
	String referrer,
	String countryCode
) {}
