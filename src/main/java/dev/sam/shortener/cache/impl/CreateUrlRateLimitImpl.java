package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.CreateUrlRateLimit;
import dev.sam.shortener.config.AppProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.URL_LIMIT;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUrlRateLimitImpl implements CreateUrlRateLimit {
	AppProperties props;
	RedisTemplate<String, Object> template;

	/**
	 * @param key      String (The Key should be <b>'ip_address:actual_url'</b>)
	 * @param duration long
	 */
	@Override
	public void add(String key, Duration duration) {
		key = URL_LIMIT + key;
		Long count = template.opsForValue().increment(key);
		if (count != null && count == 1) template.expire(key, duration);
	}

	@Override
	public boolean isLimited(String key) {
		key = URL_LIMIT + key;
		Object value = template.opsForValue().get(key);
		if (value == null) return false;
		return Integer.parseInt(value.toString()) >= props.getCreateUrlLimit();
	}
}
