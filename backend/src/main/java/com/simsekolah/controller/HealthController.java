package com.simsekolah.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/health", "/api/health"})
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final LocalDateTime startupTime = LocalDateTime.now();

    /**
     * Basic health check endpoint
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", appName);
        response.put("version", appVersion);
        response.put("timestamp", LocalDateTime.now());
        response.put("uptime", getUptime());
        response.put("message", "School Information Management System is running successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Detailed health check with component status
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> components = new HashMap<>();

        try {
            // Database health check
            components.put("database", checkDatabaseHealth());

            // Application health
            components.put("application", Map.of(
                "status", "UP",
                "version", appVersion,
                "uptime", getUptime()
            ));

            // System health
            components.put("system", checkSystemHealth());

            response.put("status", "UP");
            response.put("components", components);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Health check failed", e);
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    /**
     * Readiness probe for Kubernetes/Docker
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if database is accessible
            checkDatabaseHealth();

            response.put("status", "UP");
            response.put("message", "Application is ready to serve requests");
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Readiness check failed", e);
            response.put("status", "DOWN");
            response.put("message", "Application is not ready");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    /**
     * Liveness probe for Kubernetes/Docker
     */
    @GetMapping("/live")
    public ResponseEntity<Map<String, Object>> liveness() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is alive");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    /**
     * Check database connectivity and performance
     */
    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();

        try {
            long startTime = System.currentTimeMillis();

            // Test database connection
            jdbcTemplate.execute("SELECT 1");

            // Get basic database info
            Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

            long responseTime = System.currentTimeMillis() - startTime;

            dbHealth.put("status", "UP");
            dbHealth.put("responseTime", responseTime + "ms");
            dbHealth.put("userCount", userCount);
            dbHealth.put("connection", "OK");

        } catch (Exception e) {
            logger.error("Database health check failed", e);
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
            dbHealth.put("connection", "FAILED");
        }

        return dbHealth;
    }

    /**
     * Check system resources
     */
    private Map<String, Object> checkSystemHealth() {
        Map<String, Object> systemHealth = new HashMap<>();

        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

            // CPU usage
            double cpuLoad = osBean.getSystemLoadAverage();
            if (cpuLoad < 0) {
                cpuLoad = 0; // Some systems don't support this
            }

            // Memory usage
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            double memoryUsagePercent = maxMemory > 0 ? (double) usedMemory / maxMemory * 100 : 0;

            systemHealth.put("cpuUsage", String.format("%.2f%%", cpuLoad * 100));
            systemHealth.put("memoryUsage", String.format("%.2f%%", memoryUsagePercent));
            systemHealth.put("availableProcessors", osBean.getAvailableProcessors());
            systemHealth.put("systemLoad", cpuLoad);

        } catch (Exception e) {
            logger.error("System health check failed", e);
            systemHealth.put("error", e.getMessage());
        }

        return systemHealth;
    }

    /**
     * Calculate application uptime
     */
    private String getUptime() {
        Duration uptime = Duration.between(startupTime, LocalDateTime.now());

        long days = uptime.toDays();
        long hours = uptime.toHours() % 24;
        long minutes = uptime.toMinutes() % 60;

        if (days > 0) {
            return String.format("%d days, %d hours, %d minutes", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes", hours, minutes);
        } else {
            return String.format("%d minutes", minutes);
        }
    }
}
