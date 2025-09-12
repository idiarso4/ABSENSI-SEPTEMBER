package com.simsekolah.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * Condition to check if Redis is NOT available and we should use simple cache
 */
public class SimpleCacheCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        // Check if Redis is NOT enabled
        String redisEnabled = context.getEnvironment().getProperty("spring.redis.enabled", "false");

        if ("false".equalsIgnoreCase(redisEnabled)) {
            return true; // Use simple cache when Redis is disabled
        }

        // If Redis is enabled, try to check if it's actually available
        try {
            String host = context.getEnvironment().getProperty("spring.redis.host", "localhost");
            String port = context.getEnvironment().getProperty("spring.redis.port", "6379");

            // Simple socket test
            java.net.Socket socket = new java.net.Socket();
            socket.connect(new java.net.InetSocketAddress(host, Integer.parseInt(port)), 1000);
            socket.close();
            return false; // Redis is available, don't use simple cache
        } catch (Exception e) {
            return true; // Redis is enabled but not available, use simple cache
        }
    }
}