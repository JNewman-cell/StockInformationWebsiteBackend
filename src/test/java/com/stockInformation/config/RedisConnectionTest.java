package com.stockInformation.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        assertNotNull(redisConnectionFactory, "RedisConnectionFactory should be available");
        
        try {
            // Try to ping Redis
            redisConnectionFactory.getConnection().ping();
            System.out.println("✓ Redis connection successful!");
        } catch (Exception e) {
            System.err.println("✗ Redis connection failed: " + e.getMessage());
            e.printStackTrace();
            fail("Redis connection test failed: " + e.getMessage());
        }
    }

    @Test
    public void testRedisOperations() {
        assertNotNull(redisTemplate, "RedisTemplate should be available");
        
        try {
            String testKey = "test:connection";
            String testValue = "Hello Redis!";
            
            // Set a value
            redisTemplate.opsForValue().set(testKey, testValue);
            
            // Get the value
            Object result = redisTemplate.opsForValue().get(testKey);
            assertEquals(testValue, result, "Should retrieve the same value");
            
            // Clean up
            redisTemplate.delete(testKey);
            
            System.out.println("✓ Redis operations successful!");
        } catch (Exception e) {
            System.err.println("✗ Redis operations failed: " + e.getMessage());
            e.printStackTrace();
            fail("Redis operations test failed: " + e.getMessage());
        }
    }
}
