package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.ResetTokenCache;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.RESET_CODE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResetTokenCacheImpl implements ResetTokenCache {
	RedisTemplate<String, Object> template;

	@Override
	public void add(String token, String email, long duration) {
		template.opsForValue().set(RESET_CODE + token, email, duration, TimeUnit.MILLISECONDS);
	}

	@Override
	public String get(String token) {
		return (String) template.opsForValue().get(RESET_CODE + token);
	}

	@Override
	public void remove(String token) {
		template.delete(RESET_CODE + token);
	}
}
