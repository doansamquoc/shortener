package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import org.springframework.data.domain.Pageable;

public interface UrlService {
	UrlResponse create(UrlCreationRequest request);

	String getRedirectUrl(String shortCode);

	PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable);
}
