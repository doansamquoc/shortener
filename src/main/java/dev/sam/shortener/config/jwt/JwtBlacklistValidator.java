package dev.sam.shortener.config.jwt;

import dev.sam.shortener.cache.TokenBlacklist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtBlacklistValidator implements OAuth2TokenValidator<Jwt> {
	TokenBlacklist tokenBlacklist;

	@Override
	public OAuth2TokenValidatorResult validate(Jwt jwt) {
		if (!tokenBlacklist.isBlacklisted(jwt.getId())) return OAuth2TokenValidatorResult.success();
		OAuth2Error error = new OAuth2Error("invalid_token", "The token has expired", null);
		return OAuth2TokenValidatorResult.failure(error);
	}
}
