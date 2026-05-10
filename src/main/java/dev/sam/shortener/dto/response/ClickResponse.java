package dev.sam.shortener.dto.response;

import java.time.Instant;

public record ClickResponse(
String ipAddress,
String userAgent,
String referrer,
String countryCode,
Instant createdAt
) {}
