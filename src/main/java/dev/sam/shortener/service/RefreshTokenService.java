package dev.sam.shortener.service;

import dev.sam.shortener.entity.RefreshToken;
import dev.sam.shortener.entity.User;

public interface RefreshTokenService {
	RefreshToken create(User user);

	RefreshToken findByToken(String token);

	void revoke(RefreshToken refreshToken);

	void revoke(String token);

	void revokeAll(User user);
}
