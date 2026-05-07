package dev.sam.shortener.service.impl;

import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.entity.UserRole;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.enums.Role;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.mapper.UserMapper;
import dev.sam.shortener.repository.UserRepository;
import dev.sam.shortener.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
	UserMapper mapper;
	UserRepository repository;

	@Override
	public User create(UserRegistrationRequest request) {
		if (existsByEmail(request.email())) throw AppException.of(ErrorCode.USER_EMAIL_EXISTS);
		if (existsByUsername(request.username())) throw AppException.of(ErrorCode.USER_USERNAME_EXISTS);

		User user = mapper.toEntity(request);

		// Set role
		UserRole role = UserRole.builder().user(user).role(Role.USER).build();
		user.setRoles(Set.of(role));

		return repository.save(user);
	}

	// Don't tell users that the identifier is incorrect
	@Override
	public User findByIdentifier(String identifier) {
		return repository.findByIdentifier(identifier);
	}

	private boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	private boolean existsByUsername(String username) {
		return repository.existsByUsername(username);
	}
}
