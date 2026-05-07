package dev.sam.shortener.controller;

import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.request.UrlUpdateRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/urls")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UrlController {
	UrlService service;

	@PostMapping
	ResponseEntity<ApiResponse<UrlResponse>> create(@Valid @RequestBody UrlCreationRequest request) {
		UrlResponse response = service.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@GetMapping
	ResponseEntity<ApiResponse<PageResponse<UrlResponse>>> searchUrl(
		@RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PageResponse<UrlResponse> response = service.searchUrl(1L, searchTerm, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PatchMapping("/{id}")
	ResponseEntity<ApiResponse<UrlResponse>> update(@PathVariable Long id, @Valid @RequestBody UrlUpdateRequest request) {
		UrlResponse response = service.update(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/{shortCode}")
	ResponseEntity<ApiResponse<UrlResponse>> getUrlById(@PathVariable String shortCode) {
		UrlResponse response = service.getUrl(shortCode);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}
}
