package com.simsekolah.controller;

import com.simsekolah.service.CacheService;
import com.simsekolah.service.DatabaseOptimizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PerformanceControllerTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private DatabaseOptimizationService databaseOptimizationService;

    @InjectMocks
    private PerformanceController performanceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(performanceController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCacheStatistics_Success() throws Exception {
        Map<String, Object> statistics = new HashMap<>();
        when(cacheService.getCacheStatistics()).thenReturn(statistics);

        mockMvc.perform(get("/api/v1/performance/cache/statistics"))
                .andExpect(status().isOk());

        verify(cacheService).getCacheStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCacheStatisticsByName_Success() throws Exception {
        Map<String, Object> statistics = new HashMap<>();
        when(cacheService.getCacheStatistics("users")).thenReturn(statistics);

        mockMvc.perform(get("/api/v1/performance/cache/statistics/users"))
                .andExpect(status().isOk());

        verify(cacheService).getCacheStatistics("users");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidateAllCaches_Success() throws Exception {
        doNothing().when(cacheService).invalidateAllCaches();

        mockMvc.perform(delete("/api/v1/performance/cache/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All caches invalidated successfully"));

        verify(cacheService).invalidateAllCaches();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidateCache_Success() throws Exception {
        doNothing().when(cacheService).invalidateCache("users");

        mockMvc.perform(delete("/api/v1/performance/cache/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cache invalidated successfully"));

        verify(cacheService).invalidateCache("users");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidateCacheEntry_Success() throws Exception {
        doNothing().when(cacheService).invalidateCacheEntry("users", "user-123");

        mockMvc.perform(delete("/api/v1/performance/cache/users/entry/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cache entry invalidated successfully"));

        verify(cacheService).invalidateCacheEntry("users", "user-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void checkCacheHealth_Success() throws Exception {
        Map<String, Object> health = new HashMap<>();
        when(cacheService.checkCacheHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/performance/cache/health"))
                .andExpect(status().isOk());

        verify(cacheService).checkCacheHealth();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void warmUpCaches_Success() throws Exception {
        doNothing().when(cacheService).warmUpCaches();

        mockMvc.perform(post("/api/v1/performance/cache/warmup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cache warm-up completed successfully"));

        verify(cacheService).warmUpCaches();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDatabasePerformanceMetrics_Success() throws Exception {
        Map<String, Object> metrics = new HashMap<>();
        when(databaseOptimizationService.getDatabasePerformanceMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/api/v1/performance/database/metrics"))
                .andExpect(status().isOk());

        verify(databaseOptimizationService).getDatabasePerformanceMetrics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void analyzeSlowQueries_Success() throws Exception {
        List<Map<String, Object>> slowQueries = List.of(new HashMap<>());
        when(databaseOptimizationService.analyzeSlowQueries()).thenReturn(slowQueries);

        mockMvc.perform(get("/api/v1/performance/database/slow-queries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(databaseOptimizationService).analyzeSlowQueries();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getQueryExecutionPlan_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("query", "SELECT * FROM users");
        Map<String, Object> executionPlan = new HashMap<>();
        when(databaseOptimizationService.getQueryExecutionPlan("SELECT * FROM users")).thenReturn(executionPlan);

        mockMvc.perform(post("/api/v1/performance/database/explain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"SELECT * FROM users\"}"))
                .andExpect(status().isOk());

        verify(databaseOptimizationService).getQueryExecutionPlan("SELECT * FROM users");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void identifyMissingIndexes_Success() throws Exception {
        List<Map<String, Object>> missingIndexes = List.of(new HashMap<>());
        when(databaseOptimizationService.identifyMissingIndexes()).thenReturn(missingIndexes);

        mockMvc.perform(get("/api/v1/performance/database/missing-indexes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(databaseOptimizationService).identifyMissingIndexes();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPerformanceIndexes_Success() throws Exception {
        doNothing().when(databaseOptimizationService).createPerformanceIndexes();

        mockMvc.perform(post("/api/v1/performance/database/indexes/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Performance indexes created successfully"));

        verify(databaseOptimizationService).createPerformanceIndexes();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void analyzeIndexUsage_Success() throws Exception {
        List<Map<String, Object>> indexUsage = List.of(new HashMap<>());
        when(databaseOptimizationService.analyzeIndexUsage()).thenReturn(indexUsage);

        mockMvc.perform(get("/api/v1/performance/database/indexes/usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(databaseOptimizationService).analyzeIndexUsage();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTableSizeStatistics_Success() throws Exception {
        List<Map<String, Object>> tableStats = List.of(new HashMap<>());
        when(databaseOptimizationService.getTableSizeStatistics()).thenReturn(tableStats);

        mockMvc.perform(get("/api/v1/performance/database/tables/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(databaseOptimizationService).getTableSizeStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void checkDatabaseHealth_Success() throws Exception {
        Map<String, Object> health = new HashMap<>();
        when(databaseOptimizationService.checkDatabaseHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/performance/database/health"))
                .andExpect(status().isOk());

        verify(databaseOptimizationService).checkDatabaseHealth();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void monitorLongRunningQueries_Success() throws Exception {
        List<Map<String, Object>> longRunningQueries = List.of(new HashMap<>());
        when(databaseOptimizationService.monitorLongRunningQueries()).thenReturn(longRunningQueries);

        mockMvc.perform(get("/api/v1/performance/database/long-running-queries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(databaseOptimizationService).monitorLongRunningQueries();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void performDatabaseMaintenance_Success() throws Exception {
        doNothing().when(databaseOptimizationService).performDatabaseMaintenance();

        mockMvc.perform(post("/api/v1/performance/database/maintenance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Database maintenance completed successfully"));

        verify(databaseOptimizationService).performDatabaseMaintenance();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getConnectionPoolStatistics_Success() throws Exception {
        Map<String, Object> poolStats = new HashMap<>();
        when(databaseOptimizationService.getConnectionPoolStatistics()).thenReturn(poolStats);

        mockMvc.perform(get("/api/v1/performance/database/connection-pool"))
                .andExpect(status().isOk());

        verify(databaseOptimizationService).getConnectionPoolStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPerformanceOverview_Success() throws Exception {
        Map<String, Object> cacheStats = new HashMap<>();
        Map<String, Object> dbMetrics = new HashMap<>();
        Map<String, Object> cacheHealth = new HashMap<>();
        Map<String, Object> dbHealth = new HashMap<>();
        when(cacheService.getCacheStatistics()).thenReturn(cacheStats);
        when(databaseOptimizationService.getDatabasePerformanceMetrics()).thenReturn(dbMetrics);
        when(cacheService.checkCacheHealth()).thenReturn(cacheHealth);
        when(databaseOptimizationService.checkDatabaseHealth()).thenReturn(dbHealth);

        mockMvc.perform(get("/api/v1/performance/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cache").exists())
                .andExpect(jsonPath("$.database").exists())
                .andExpect(jsonPath("$.health.cache").exists())
                .andExpect(jsonPath("$.health.database").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(cacheService).getCacheStatistics();
        verify(databaseOptimizationService).getDatabasePerformanceMetrics();
        verify(cacheService).checkCacheHealth();
        verify(databaseOptimizationService).checkDatabaseHealth();
    }
}