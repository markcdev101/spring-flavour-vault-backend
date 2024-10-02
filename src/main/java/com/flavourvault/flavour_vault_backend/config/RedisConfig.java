package com.flavourvault.flavour_vault_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * This configuration sets up the RedisTemplate and RedisCacheManager for interacting with Redis
 */
@Configuration
public class RedisConfig {

	 @Bean
	    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
	        RedisTemplate<String, Object> template = new RedisTemplate<>();
	        template.setConnectionFactory(redisConnectionFactory);

	        // Configure the serializer
	        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
	        template.setValueSerializer(serializer);
	        template.setKeySerializer(RedisSerializer.string());
	        return template;
	    }

	    @Bean
	    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
	        return RedisCacheManager.builder(redisConnectionFactory).build();
	    }
}
