package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Pageable;

public interface UrlService {
	UrlResponse create(UrlCreationRequest request);

	UrlResponse getRedirectUrl(String shortCode);

	void incrementTotalClicks(Long id);

	PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable);

	Url getReference(Long id);
}
