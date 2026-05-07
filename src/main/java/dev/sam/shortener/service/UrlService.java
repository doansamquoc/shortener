package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.request.UrlUpdateRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Pageable;

public interface UrlService {
	UrlResponse create(UrlCreationRequest request);

	String getRedirectUrl(String shortCode);

	UrlResponse update(Long id, UrlUpdateRequest request);

	void delete(Long userId, Long id);

	void deleteAll(Long userId);

	void cleanupUrls();

	void incrementTotalClicks(String shortCode);

	PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable);

	UrlResponse getUrl(String shortCode);

	Url getReference(Long id);
}
