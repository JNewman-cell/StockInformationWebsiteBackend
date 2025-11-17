package com.stockInformation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * Redis-backed cache configuration for the application.
 *
 * Declares a primary CacheManager backed by Redis so existing in-memory
 * configuration is overridden when Redis is available at runtime.
 *
 * This configuration is conditional on Redis being properly configured.
 * If Redis is unavailable, the application will fallback to no-op caching.
 */
@Configuration
// Only enable this Redis-backed cache configuration when a Redis host is configured
// AND the active cache type is set to `redis` (the default). This lets tests or local
// runs disable caching by setting `spring.cache.type=none` or `CACHE_TYPE=none`.
@ConditionalOnProperty(name = "spring.redis.host")
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = true)
public class RedisCacheConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.username:}")
    private String redisUsername;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    @Value("${spring.redis.ssl.enabled:false}")
    private boolean sslEnabled;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        
        if (redisUsername != null && !redisUsername.isBlank()) {
            config.setUsername(redisUsername);
        }
        
        if (redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(redisPassword);
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfig = 
                LettuceClientConfiguration.builder();
        
        if (sslEnabled) {
            clientConfig.useSsl();
        }

        return new LettuceConnectionFactory(config, clientConfig.build());
    }
    @Primary
    @Bean(name = "redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        RedisSerializationContext.SerializationPair<Object> valuePair =
                RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(valuePair)
                // sensible default TTL for caches
                .entryTtl(Duration.ofMinutes(60));

        // shorter TTL for autocomplete results
        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                "autocomplete", defaultConfig.entryTtl(Duration.ofMinutes(15))
        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .transactionAware()
                .build();
    }
}
