package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import dev.sam.shortener.cache.MailRateLimit;
import dev.sam.shortener.config.AppProperties;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.sam.shortener.constant.CacheNames.MAIL_LIMIT;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailRateLimitImpl implements MailRateLimit {

    private record RateLimitEntry(AtomicInteger counter, long nanos) {}

    AppProperties props;
    Cache<String, RateLimitEntry> cache;

    public MailRateLimitImpl(AppProperties props) {
        this.props = props;
        this.cache = Caffeine.newBuilder()
                .maximumSize(50_000)
                .expireAfter(new Expiry<String, RateLimitEntry>() {
                    @Override
                    public long expireAfterCreate(String key, RateLimitEntry value, long currentTime) {
                        return value.nanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, RateLimitEntry value, long currentTime, long currentDuration) {
                        return currentDuration; // Keep original expiry on increment/decrement
                    }

                    @Override
                    public long expireAfterRead(String key, RateLimitEntry value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    @Override
    public void add(String email, long duration) {
        String cacheKey = MAIL_LIMIT + email;
        long nanos = TimeUnit.MILLISECONDS.toNanos(duration);

        cache.asMap().compute(cacheKey, (k, existing) -> {
            if (existing == null) {
                return new RateLimitEntry(new AtomicInteger(1), nanos);
            }
            existing.counter().incrementAndGet();
            return existing;
        });
    }

    @Override
    public boolean isLimited(String email) {
        String cacheKey = MAIL_LIMIT + email;
        RateLimitEntry entry = cache.getIfPresent(cacheKey);
        if (entry == null) {
            return false;
        }
        return entry.counter().get() >= props.getEmail().getLimitPerAccount();
    }

    @Override
    public void decrease(String email) {
        String cacheKey = MAIL_LIMIT + email;
        RateLimitEntry entry = cache.getIfPresent(cacheKey);
        if (entry != null) {
            entry.counter().decrementAndGet();
        }
    }
}
