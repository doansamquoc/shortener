package dev.sam.shortener.controller;

import dev.sam.shortener.annotation.CurrentUserId;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/urls")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UrlController {
	UrlService service;

	@PostMapping
	ResponseEntity<ApiResponse<UrlResponse>> create(
	@Valid @RequestBody UrlCreationRequest request,
	@AuthenticationPrincipal Jwt jwt
	) {
		Long userId = jwt == null ? null : Long.valueOf(jwt.getSubject());
		UrlResponse response = service.create(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@GetMapping
	ResponseEntity<ApiResponse<PageResponse<UrlResponse>>> searchUrl(
	@RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
	@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
	@CurrentUserId Long currentUserId
	) {
		PageResponse<UrlResponse> response = service.searchUrl(currentUserId, searchTerm, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PatchMapping("/{id}")
	ResponseEntity<ApiResponse<UrlResponse>> update(
	@PathVariable Long id,
	@Valid @RequestBody UrlUpdateRequest request,
	@CurrentUserId Long currentUserId
	) {
		UrlResponse response = service.update(currentUserId, id, request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/{shortCode}")
	ResponseEntity<ApiResponse<UrlResponse>> getUrlById(@PathVariable String shortCode) {
		UrlResponse response = service.getUrl(shortCode);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<ApiResponse<String>> deleteUrlById(
	@PathVariable Long id,
	@CurrentUserId Long currentUserId
	) {
		service.delete(currentUserId, id);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of("Deleted successfully"));
	}

	@DeleteMapping
	ResponseEntity<ApiResponse<String>> deleteAllByUser(@CurrentUserId Long currentUserId) {
		service.deleteAll(currentUserId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of("All urls deleted successfully"));
	}
}
