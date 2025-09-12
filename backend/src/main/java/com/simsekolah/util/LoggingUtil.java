package com.simsekolah.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class for structured logging with MDC (Mapped Diagnostic Context)
 * Provides consistent logging patterns and performance monitoring
 */
public class LoggingUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    /**
     * Log method entry with parameters
     */
    public static void logMethodEntry(Logger logger, String methodName, Object... params) {
        if (logger.isDebugEnabled()) {
            MDC.put("method", methodName);
            logger.debug("Entering method: {} with params: {}", methodName, params);
        }
    }

    /**
     * Log method exit with result
     */
    public static void logMethodExit(Logger logger, String methodName, Object result) {
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting method: {} with result: {}", methodName, result);
            MDC.remove("method");
        }
    }

    /**
     * Log method exit with exception
     */
    public static void logMethodExitWithException(Logger logger, String methodName, Exception e) {
        logger.error("Exception in method: {} - {}", methodName, e.getMessage(), e);
        MDC.remove("method");
    }

    /**
     * Log performance metrics
     */
    public static void logPerformance(Logger logger, String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        if (duration > 1000) { // Log slow operations (> 1 second)
            logger.warn("Slow operation detected: {} took {}ms", operation, duration);
        } else if (logger.isDebugEnabled()) {
            logger.debug("Operation: {} completed in {}ms", operation, duration);
        }
    }

    /**
     * Log with MDC context
     */
    public static void logWithContext(Logger logger, String message, Map<String, String> context) {
        context.forEach(MDC::put);
        logger.info(message);
        context.keySet().forEach(MDC::remove);
    }

    /**
     * Log user action for audit trail
     */
    public static void logUserAction(String userId, String action, String resource, String details) {
        MDC.put("userId", userId);
        MDC.put("action", action);
        MDC.put("resource", resource);
        logger.info("USER_ACTION: {} performed {} on {} - {}", userId, action, resource, details);
        MDC.clear();
    }

    /**
     * Log security event
     */
    public static void logSecurityEvent(String event, String userId, String ipAddress, String details) {
        MDC.put("event", event);
        MDC.put("userId", userId != null ? userId : "anonymous");
        MDC.put("ipAddress", ipAddress);
        logger.warn("SECURITY_EVENT: {} - User: {} - IP: {} - {}", event, userId, ipAddress, details);
        MDC.clear();
    }

    /**
     * Execute operation with performance logging
     */
    public static <T> T executeWithPerformanceLogging(Logger logger, String operationName,
                                                     Supplier<T> operation) {
        long startTime = System.currentTimeMillis();
        try {
            T result = operation.get();
            logPerformance(logger, operationName, startTime);
            return result;
        } catch (Exception e) {
            logger.error("Operation failed: {} - {}", operationName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Execute operation with performance logging (void return)
     */
    public static void executeWithPerformanceLogging(Logger logger, String operationName,
                                                    Runnable operation) {
        long startTime = System.currentTimeMillis();
        try {
            operation.run();
            logPerformance(logger, operationName, startTime);
        } catch (Exception e) {
            logger.error("Operation failed: {} - {}", operationName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Log database operation
     */
    public static void logDatabaseOperation(String operation, String table, String conditions, long duration) {
        if (duration > 500) { // Log slow queries (> 500ms)
            logger.warn("Slow database operation: {} on {} with conditions: {} took {}ms",
                       operation, table, conditions, duration);
        } else if (logger.isDebugEnabled()) {
            logger.debug("Database operation: {} on {} took {}ms", operation, table, duration);
        }
    }

    /**
     * Log API request
     */
    public static void logApiRequest(String method, String path, String userId, int statusCode, long duration) {
        MDC.put("method", method);
        MDC.put("path", path);
        MDC.put("userId", userId != null ? userId : "anonymous");
        MDC.put("statusCode", String.valueOf(statusCode));
        MDC.put("duration", String.valueOf(duration));

        if (statusCode >= 400) {
            logger.warn("API_REQUEST: {} {} - Status: {} - Duration: {}ms", method, path, statusCode, duration);
        } else if (duration > 2000) { // Log slow requests (> 2 seconds)
            logger.warn("Slow API request: {} {} took {}ms", method, path, duration);
        } else if (logger.isDebugEnabled()) {
            logger.debug("API_REQUEST: {} {} - Status: {} - Duration: {}ms", method, path, statusCode, duration);
        }

        MDC.clear();
    }

    /**
     * Log business event
     */
    public static void logBusinessEvent(String eventType, String entityType, String entityId,
                                       String action, String details) {
        MDC.put("eventType", eventType);
        MDC.put("entityType", entityType);
        MDC.put("entityId", entityId);
        MDC.put("action", action);

        logger.info("BUSINESS_EVENT: {} - {} {} {} - {}", eventType, action, entityType, entityId, details);
        MDC.clear();
    }

    /**
     * Clear all MDC values
     */
    public static void clearMDC() {
        MDC.clear();
    }
}