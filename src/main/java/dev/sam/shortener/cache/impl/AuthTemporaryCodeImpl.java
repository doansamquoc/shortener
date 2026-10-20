package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.sam.shortener.cache.AuthTemporaryCode;
import dev.sam.shortener.config.AppProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthTemporaryCodeImpl implements AuthTemporaryCode {
    private final Cache<String, Object> cache;

    public AuthTemporaryCodeImpl(AppProperties props) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMillis(props.getAuthCodeExpiration()))
                .maximumSize(10_000)
                .build();
    }

    @Override
    public void add(String code, Object value) {
        cache.put(code, value);
    }

    @Override
    public Object get(String code) {
        return cache.getIfPresent(code);
    }

    @Override
    public void remove(String code) {
        cache.invalidate(code);
    }
}
