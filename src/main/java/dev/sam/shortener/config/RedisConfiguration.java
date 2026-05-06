package dev.sam.shortener.config;

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisConfiguration {
	@Bean
	GenericJacksonJsonRedisSerializer GenericJacksonJsonRedisSerializer() {
		return new GenericJacksonJsonRedisSerializer(new ObjectMapper());
	}

	@Bean
	RedisCacheConfiguration redisCacheConfiguration(GenericJacksonJsonRedisSerializer serializer) {
		return RedisCacheConfiguration.defaultCacheConfig(
		).entryTtl(Duration.ofDays(1)
		).serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
		).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
	}

	@Bean
	RedisCacheManager redisCacheManager(RedisConnectionFactory factory, RedisCacheConfiguration config) {
		Map<String, RedisCacheConfiguration> cache = new HashMap<>();
		cache.put(CacheNames.URL, config.entryTtl(Duration.ofDays(1)));
		return RedisCacheManager.builder(factory).cacheDefaults(config).withInitialCacheConfigurations(cache).build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, GenericJacksonJsonRedisSerializer serializer) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();
		return template;
	}
}
