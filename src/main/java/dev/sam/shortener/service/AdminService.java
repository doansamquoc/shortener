package dev.sam.shortener.service;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.response.UserDetailsResponse;
import org.springframework.data.domain.Pageable;

public interface AdminService {
	PageResponse<UserDetailsResponse> findAllUsers(String searchTerm, Double threshold, Pageable pageable);

	void deleteUser(Long userId);
}
