package com.simsekolah.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * Condition to check if Redis is available and enabled
 */
public class RedisCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        // Check if Redis is enabled via property
        String redisEnabled = context.getEnvironment().getProperty("spring.redis.enabled", "false");

        if ("true".equalsIgnoreCase(redisEnabled)) {
            // Try to connect to Redis to verify it's available
            try {
                String host = context.getEnvironment().getProperty("spring.redis.host", "localhost");
                String port = context.getEnvironment().getProperty("spring.redis.port", "6379");

                // Simple socket test
                java.net.Socket socket = new java.net.Socket();
                socket.connect(new java.net.InetSocketAddress(host, Integer.parseInt(port)), 1000);
                socket.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}