package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import dev.sam.shortener.cache.ResetTokenCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ResetTokenCacheImpl implements ResetTokenCache {

    private record ExpiringEmail(String email, long nanos) {}

    private final Cache<String, ExpiringEmail> cache;

    public ResetTokenCacheImpl() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(50_000)
                .expireAfter(new Expiry<String, ExpiringEmail>() {
                    @Override
                    public long expireAfterCreate(String key, ExpiringEmail value, long currentTime) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, ExpiringEmail value, long currentTime, long currentDuration) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterRead(String key, ExpiringEmail value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    @Override
    public void add(String token, String email, long duration) {
        long nanos = TimeUnit.MILLISECONDS.toNanos(duration);
        cache.put(token, new ExpiringEmail(email, nanos));
    }

    @Override
    public String get(String token) {
        ExpiringEmail entry = cache.getIfPresent(token);
        return entry != null ? entry.email() : null;
    }

    @Override
    public void remove(String token) {
        cache.invalidate(token);
    }
}
