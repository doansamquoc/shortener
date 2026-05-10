package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.ClickRequest;
import dev.sam.shortener.dto.response.ClickResponse;
import dev.sam.shortener.entity.Click;
import org.springframework.data.domain.Pageable;

public interface ClickService {
	Click create(ClickRequest request);

	void create(Long urlId, ClickRequest request);

	PageResponse<ClickResponse> findAll(Long urlId, String searchTerm, Double threshold, Pageable pageable);
}
