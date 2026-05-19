package dev.sam.shortener.dto.request;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UrlUpdateRequest(
	@Size(min = 2, max = 255, message = "url.title.size")
	String title,

	@URL(message = "url.actual_url.invalid")
	String actualUrl,

	@Size(min = 2, max = 255, message = "url.short_code.size")
	String shortCode
) {}
