package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.entity.User;

public interface UserService {
	User create(UserRegistrationRequest request);

	// Don't tell users that the identifier is incorrect
	User findByIdentifier(String identifier);
}
