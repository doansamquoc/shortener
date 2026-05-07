package dev.sam.shortener.dto.response;

import java.time.Instant;

public record UrlResponse(
	Long id,
	String title,
	String actualUrl,
	String shortenedUrl,
	Long totalClicks,
	Instant lastClickAt,
	Instant createdAt,
	Instant updatedAt
) {}
