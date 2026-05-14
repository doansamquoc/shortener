package dev.sam.shortener.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.sam.shortener.constant.CacheNames;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisConfiguration {
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer().configure(mapper -> {
            mapper.registerModule(new JavaTimeModule());
            
            // Handler SimpleGrantedAuthority deserialize
            mapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        });
    }
    
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(GenericJackson2JsonRedisSerializer serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(Duration.ofMinutes(60))
                                      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                          serializer
                                      ));
    }
    
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, RedisCacheConfiguration config) {
        Map<String, RedisCacheConfiguration> cache = new HashMap<>();
        cache.put(CacheNames.URL_SHORT, config.entryTtl(Duration.ofHours(2)));
        return RedisCacheManager.builder(factory).cacheDefaults(config).withInitialCacheConfigurations(cache).build();
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory factory,
        GenericJackson2JsonRedisSerializer serializer
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>(); template.setConnectionFactory(factory);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        template.setValueSerializer(serializer); template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet(); return template;
    }
}
