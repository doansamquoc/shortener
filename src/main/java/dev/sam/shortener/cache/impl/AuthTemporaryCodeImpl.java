package dev.sam.shortener.cache.impl;

import dev.sam.shortener.cache.AuthTemporaryCode;
import dev.sam.shortener.config.AppProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static dev.sam.shortener.constant.CacheNames.AUTH_CODE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthTemporaryCodeImpl implements AuthTemporaryCode {
	AppProperties props;
	RedisTemplate<String, Object> template;

	@Override
	public void add(String code, Object value) {
		template.opsForValue().set(AUTH_CODE + code, value, props.getAuthCodeExpiration(), TimeUnit.MILLISECONDS);
	}

	@Override
	public Object get(String code) {
		return template.opsForValue().get(AUTH_CODE + code);
	}

	@Override
	public void remove(String code) {
		template.delete(AUTH_CODE + code);
	}
}
