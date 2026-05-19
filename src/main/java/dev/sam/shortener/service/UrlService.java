package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.request.UrlUpdateRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface UrlService {
	@Transactional
	UrlResponse create(Long userId, String ipAddress, UrlCreationRequest request);

	String getRedirectUrl(String shortCode);

	UrlResponse update(Long userId, Long id, UrlUpdateRequest request);

	void delete(Long userId, Long id);

	void deleteAll(Long userId);

	void cleanupUrls();

	void incrementTotalClicks(String shortCode);

	PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable);

	Page<Url> findAll(Long userId, String searchTerm, Double threshold, Pageable pageable);

	UrlResponse getUrl(String shortCode);

	Url getReference(Long id);
}
