package dev.sam.shortener.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import dev.sam.shortener.constant.CacheNames;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CaffeineCacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache urlShortCache = new CaffeineCache(
                CacheNames.URL_SHORT,
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofHours(2))
                        .maximumSize(50_000)
                        .recordStats()
                        .build()
        );

        cacheManager.setCaches(List.of(urlShortCache));
        return cacheManager;
    }
}
