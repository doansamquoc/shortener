package dev.sam.shortener.controller;

import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.dto.response.UserDetailsResponse;
import dev.sam.shortener.service.AdminService;
import dev.sam.shortener.service.UrlService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
	AdminService adminService;
	UrlService urlService;

	@GetMapping("/users")
	ResponseEntity<ApiResponse<PageResponse<UserDetailsResponse>>> getAllUsers(
	@RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
	@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PageResponse<UserDetailsResponse> response = adminService.findAllUsers(searchTerm, 0.3, pageable);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/users/{id}")
	ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long id) {
		adminService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/urls")
	ResponseEntity<ApiResponse<PageResponse<UrlResponse>>> getAllUrls(
	@RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
	@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PageResponse<UrlResponse> response = urlService.searchUrl(null, searchTerm, pageable);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/urls/{userId}")
	ResponseEntity<ApiResponse<PageResponse<UrlResponse>>> getAllUrls(
	@PathVariable Long userId,
	@RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
	@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PageResponse<UrlResponse> response = urlService.searchUrl(userId, searchTerm, pageable);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}