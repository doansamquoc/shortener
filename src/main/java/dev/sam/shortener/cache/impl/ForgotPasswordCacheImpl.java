package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.ForgotPasswordCache;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.FORGOT_PASSWORD;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordCacheImpl implements ForgotPasswordCache {
	RedisTemplate<String, String> template;

	@Override
	public void add(String email, String code, long duration) {
		if (get(email) == null) remove(email);
		template.opsForValue().set(FORGOT_PASSWORD + email, code, duration, TimeUnit.MILLISECONDS);
	}

	@Override
	public String get(String email) {
		return template.opsForValue().get(FORGOT_PASSWORD + email);
	}

	@Override
	public void remove(String email) {
		template.delete(FORGOT_PASSWORD + email);
	}
}
