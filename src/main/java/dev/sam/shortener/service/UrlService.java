package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Pageable;

public interface UrlService {
	UrlResponse create(UrlCreationRequest request);

	String getRedirectUrl(String shortCode);

	void incrementTotalClicks(String shortCode);

	PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable);

	UrlResponse getUrlById(Long id);

	UrlResponse getUrl(String shortCode);

	Url getReference(Long id);
}
