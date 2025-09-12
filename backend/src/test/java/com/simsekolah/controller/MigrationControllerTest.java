package com.simsekolah.controller;

import com.simsekolah.service.DataMigrationService;
import com.simsekolah.service.MigrationValidationService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MigrationControllerTest {

    @Mock
    private DataMigrationService dataMigrationService;

    @Mock
    private MigrationValidationService migrationValidationService;

    @InjectMocks
    private MigrationController migrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(migrationController).build();
    }

    @Test
    void initializeTestData_Success() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        when(dataMigrationService.initializeTestData()).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/init-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));

        verify(dataMigrationService).initializeTestData();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void executeCompleteMigration_Success() throws Exception {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "completed");
        when(dataMigrationService.executeCompleteMigration(anyMap())).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/execute/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("completed"));

        verify(dataMigrationService).executeCompleteMigration(anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void executePartialMigration_Success() throws Exception {
        List<String> tableNames = List.of("users", "students");
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "completed");
        when(dataMigrationService.getDefaultMigrationConfiguration()).thenReturn(config);
        when(dataMigrationService.executePartialMigration(anyList(), anyMap())).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/execute/partial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"users\",\"students\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("completed"));

        verify(dataMigrationService).executePartialMigration(anyList(), anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void initializeBasicData_Success() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        when(dataMigrationService.initializeTestData()).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/init-basic-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));

        verify(dataMigrationService).initializeTestData();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMigrationProgress_Success() throws Exception {
        Map<String, Object> progress = new HashMap<>();
        progress.put("status", "running");
        when(dataMigrationService.getMigrationProgress("migration-123")).thenReturn(progress);

        mockMvc.perform(get("/api/v1/migration/progress/migration-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("running"));

        verify(dataMigrationService).getMigrationProgress("migration-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelMigration_Success() throws Exception {
        doNothing().when(dataMigrationService).cancelMigration("migration-123");

        mockMvc.perform(post("/api/v1/migration/cancel/migration-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Migration cancelled successfully"));

        verify(dataMigrationService).cancelMigration("migration-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void rollbackMigration_Success() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "rolled back");
        when(dataMigrationService.rollbackMigration("migration-123")).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/rollback/migration-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("rolled back"));

        verify(dataMigrationService).rollbackMigration("migration-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void validateDataIntegrity_Success() throws Exception {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "valid");
        when(dataMigrationService.validateDataIntegrity(anyMap())).thenReturn(result);

        mockMvc.perform(post("/api/v1/migration/validate/integrity/migration-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("valid"));

        verify(dataMigrationService).validateDataIntegrity(anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void compareRecordCounts_Success() throws Exception {
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("status", "matched");
        when(migrationValidationService.compareRecordCounts()).thenReturn(comparison);

        mockMvc.perform(get("/api/v1/migration/validate/record-counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("matched"));

        verify(migrationValidationService).compareRecordCounts();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void runMigrationTestSuite_Success() throws Exception {
        Map<String, Object> testResults = new HashMap<>();
        testResults.put("overallStatus", "passed");
        when(migrationValidationService.runMigrationTestSuite("migration-123")).thenReturn(testResults);

        mockMvc.perform(post("/api/v1/migration/test/migration-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("passed"));

        verify(migrationValidationService).runMigrationTestSuite("migration-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateValidationReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        report.put("status", "complete");
        when(migrationValidationService.generateValidationReport("migration-123")).thenReturn(report);

        mockMvc.perform(post("/api/v1/migration/validate/report/migration-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("complete"));

        verify(migrationValidationService).generateValidationReport("migration-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMigrationHistory_Success() throws Exception {
        List<Map<String, Object>> history = List.of(new HashMap<>());
        when(dataMigrationService.getMigrationHistory()).thenReturn(history);

        mockMvc.perform(get("/api/v1/migration/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(dataMigrationService).getMigrationHistory();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSupportedTableMappings_Success() throws Exception {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("laravel_users", "spring_users");
        when(dataMigrationService.getSupportedTableMappings()).thenReturn(mappings);

        mockMvc.perform(get("/api/v1/migration/mappings/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.laravel_users").value("spring_users"));

        verify(dataMigrationService).getSupportedTableMappings();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDataTypeMappings_Success() throws Exception {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("varchar(255)", "VARCHAR(255)");
        when(dataMigrationService.getDataTypeMappings()).thenReturn(mappings);

        mockMvc.perform(get("/api/v1/migration/mappings/data-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.varchar(255)").value("VARCHAR(255)"));

        verify(dataMigrationService).getDataTypeMappings();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void checkMigrationPrerequisites_Success() throws Exception {
        Map<String, Object> prerequisites = new HashMap<>();
        prerequisites.put("status", "ready");
        when(dataMigrationService.checkMigrationPrerequisites()).thenReturn(prerequisites);

        mockMvc.perform(get("/api/v1/migration/prerequisites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ready"));

        verify(dataMigrationService).checkMigrationPrerequisites();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void estimateMigrationTime_Success() throws Exception {
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> estimation = new HashMap<>();
        estimation.put("estimatedTime", "2 hours");
        when(dataMigrationService.estimateMigrationTime(anyMap())).thenReturn(estimation);

        mockMvc.perform(post("/api/v1/migration/estimate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estimatedTime").value("2 hours"));

        verify(dataMigrationService).estimateMigrationTime(anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDefaultMigrationConfiguration_Success() throws Exception {
        Map<String, Object> config = new HashMap<>();
        config.put("batchSize", 1000);
        when(dataMigrationService.getDefaultMigrationConfiguration()).thenReturn(config);

        mockMvc.perform(get("/api/v1/migration/config/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchSize").value(1000));

        verify(dataMigrationService).getDefaultMigrationConfiguration();
    }
}