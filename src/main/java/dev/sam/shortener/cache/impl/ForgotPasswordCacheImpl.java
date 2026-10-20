package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import dev.sam.shortener.cache.ForgotPasswordCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ForgotPasswordCacheImpl implements ForgotPasswordCache {

    private record ExpiringCode(String code, long nanos) {}

    private final Cache<String, ExpiringCode> cache;

    public ForgotPasswordCacheImpl() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(50_000)
                .expireAfter(new Expiry<String, ExpiringCode>() {
                    @Override
                    public long expireAfterCreate(String key, ExpiringCode value, long currentTime) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, ExpiringCode value, long currentTime, long currentDuration) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterRead(String key, ExpiringCode value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    @Override
    public void add(String email, String code, long duration) {
        if (get(email) != null) {
            remove(email);
        }
        long nanos = TimeUnit.MILLISECONDS.toNanos(duration);
        cache.put(email, new ExpiringCode(code, nanos));
    }

    @Override
    public String get(String email) {
        ExpiringCode entry = cache.getIfPresent(email);
        return entry != null ? entry.code() : null;
    }

    @Override
    public void remove(String email) {
        cache.invalidate(email);
    }
}
