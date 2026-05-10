package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
	User create(UserRegistrationRequest request);

	User findByIdentifier(String identifier);

	User processOAuth2(String email);

	Page<User> findAllUsers(Pageable pageable);

	Page<User> searchUsers(String searchTerm, Double threshold, Pageable pageable);

	void delete(Long id);

	Optional<User> findByEmail(String email);

	User getReference(Long id);

	void save(User user);
}
