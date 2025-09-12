package com.simsekolah.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;

/**
 * Simple in-memory cache configuration for development when Redis is not available
 */
@Configuration
@Conditional(SimpleCacheCondition.class)
public class SimpleCacheConfig {

    /**
     * Simple in-memory cache manager using ConcurrentHashMap
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
            "users", "userProfiles", "userRoles",
            "students", "studentProfiles", "studentsByClass",
            "assessments", "grades", "transcripts",
            "attendance", "attendanceReports", "dailyAttendance",
            "academicReports", "performanceReports", "statisticsReports",
            "classRooms", "subjects", "departments", "majors",
            "sessions", "authTokens",
            "extracurricularActivities", "activityParticipants",
            "dashboardData", "kpiData"
        );
    }
}