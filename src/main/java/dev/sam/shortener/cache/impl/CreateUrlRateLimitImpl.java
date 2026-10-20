package dev.sam.shortener.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import dev.sam.shortener.cache.CreateUrlRateLimit;
import dev.sam.shortener.config.AppProperties;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.sam.shortener.constant.CacheNames.URL_LIMIT;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUrlRateLimitImpl implements CreateUrlRateLimit {

    private record RateLimitEntry(AtomicInteger counter, long nanos) {}

    AppProperties props;
    Cache<String, RateLimitEntry> cache;

    public CreateUrlRateLimitImpl(AppProperties props) {
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
                        return currentDuration; // Keep original expiry on increment
                    }

                    @Override
                    public long expireAfterRead(String key, RateLimitEntry value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    /**
     * @param key      String (The Key should be <b>'ip_address:actual_url'</b>)
     * @param duration Duration
     */
    @Override
    public void add(String key, Duration duration) {
        String cacheKey = URL_LIMIT + key;
        long nanos = duration.toNanos();

        cache.asMap().compute(cacheKey, (k, existing) -> {
            if (existing == null) {
                return new RateLimitEntry(new AtomicInteger(1), nanos);
            }
            existing.counter().incrementAndGet();
            return existing;
        });
    }

    @Override
    public boolean isLimited(String key) {
        String cacheKey = URL_LIMIT + key;
        RateLimitEntry entry = cache.getIfPresent(cacheKey);
        if (entry == null) {
            return false;
        }
        return entry.counter().get() >= props.getCreateUrlLimit();
    }
}
