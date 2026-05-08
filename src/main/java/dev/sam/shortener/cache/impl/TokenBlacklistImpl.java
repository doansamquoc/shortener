package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.TokenBlacklist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.JWT_BLACKLIST;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenBlacklistImpl implements TokenBlacklist {
	RedisTemplate<String, String> template;

	@Override
	public void add(String tokenId, long timeout, TimeUnit unit) {
		template.opsForValue().set(JWT_BLACKLIST + tokenId, "Revoked!", timeout, unit);
	}

	@Override
	public boolean isBlacklisted(String tokenId) {
		return template.hasKey(JWT_BLACKLIST + tokenId);
	}
}
