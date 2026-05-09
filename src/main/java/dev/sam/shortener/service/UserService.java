package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.entity.User;

import java.util.Optional;

public interface UserService {
	User create(UserRegistrationRequest request);

	User findByIdentifier(String identifier);

	User processOAuth2(String email);

	Optional<User> findByEmail(String email);

	User getReference(Long id);

	User save(User user);
}
