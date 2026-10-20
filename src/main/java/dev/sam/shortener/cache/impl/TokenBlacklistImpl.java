package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import dev.sam.shortener.cache.TokenBlacklist;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistImpl implements TokenBlacklist {

    private record ExpiringValue(String value, long nanos) {}

    private final Cache<String, ExpiringValue> cache;

    public TokenBlacklistImpl() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfter(new Expiry<String, ExpiringValue>() {
                    @Override
                    public long expireAfterCreate(String key, ExpiringValue value, long currentTime) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, ExpiringValue value, long currentTime, long currentDuration) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterRead(String key, ExpiringValue value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    @Override
    public void add(String tokenId, long timeout, TimeUnit unit) {
        long nanos = unit.toNanos(timeout);
        cache.put(tokenId, new ExpiringValue("Revoked!", nanos));
    }

    @Override
    public boolean isBlacklisted(String tokenId) {
        return cache.getIfPresent(tokenId) != null;
    }
}
