package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.MailRateLimit;
import dev.sam.shortener.config.AppProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.MAIL_LIMIT;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailRateLimitImpl implements MailRateLimit {
	AppProperties props;
	RedisTemplate<String, Object> template;

	@Override
	public void add(String email, long duration) {
		String key = MAIL_LIMIT + email;
		Long count = template.opsForValue().increment(key);
		if (count != null && count == 1) template.expire(key, duration, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isLimited(String email) {
		Object value = template.opsForValue().get(MAIL_LIMIT + email);
		if (value == null) return false;
		return Integer.parseInt(value.toString()) >= props.getEmail().getLimitPerAccount();
	}

	@Override
	public void decrease(String email) {
		template.opsForValue().decrement(MAIL_LIMIT + email);
	}
}
