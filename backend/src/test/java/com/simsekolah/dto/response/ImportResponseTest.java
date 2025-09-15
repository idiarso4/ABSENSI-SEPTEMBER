package com.simsekolah.dto.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for ImportResponse DTO
 */
public class ImportResponseTest {

    @Test
    public void testImportResponseBuilder() {
        // Test successful import response
        List<String> createdItems = Arrays.asList("item1", "item2");
        List<String> updatedItems = Arrays.asList("item3");
        Map<String, Object> error1 = new HashMap<>();
        error1.put("row", 1);
        error1.put("field", "name");
        error1.put("value", "");
        error1.put("message", "Name is required");
        List<Map<String, Object>> errors = Arrays.asList(error1);

        ImportResponse response = ImportResponse.builder()
            .filename("test.xlsx")
            .totalRows(3)
            .successfulImports(2)
            .failedImports(1)
            .createdItems(createdItems)
            .updatedItems(updatedItems)
            .mode("create")
            .errors(errors)
            .status("PARTIAL_SUCCESS")
            .build();

        assertEquals("test.xlsx", response.getFilename());
        assertEquals(3, response.getTotalRows());
        assertEquals(2, response.getSuccessfulImports());
        assertEquals(1, response.getFailedImports());
        assertEquals(createdItems, response.getCreatedItems());
        assertEquals(updatedItems, response.getUpdatedItems());
        assertEquals("create", response.getMode());
        assertEquals(errors, response.getErrors());
        assertEquals("PARTIAL_SUCCESS", response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testImportResponseDefaultConstructor() {
        ImportResponse response = new ImportResponse();
        assertNotNull(response.getTimestamp());
        assertNull(response.getFilename());
        assertEquals(0, response.getTotalRows());
        assertEquals(0, response.getSuccessfulImports());
        assertEquals(0, response.getFailedImports());
        assertNull(response.getCreatedItems());
        assertNull(response.getUpdatedItems());
        assertNull(response.getMode());
        assertNull(response.getErrors());
        assertNull(response.getStatus());
    }

    @Test
    public void testImportResponseParameterizedConstructor() {
        List<String> createdItems = Arrays.asList("item1");
        List<String> updatedItems = Arrays.asList();
        List<Map<String, Object>> errors = Arrays.asList();

        ImportResponse response = new ImportResponse(
            "test.xlsx", 1, 1, 0, createdItems, updatedItems,
            "create", errors, "SUCCESS"
        );

        assertEquals("test.xlsx", response.getFilename());
        assertEquals(1, response.getTotalRows());
        assertEquals(1, response.getSuccessfulImports());
        assertEquals(0, response.getFailedImports());
        assertEquals(createdItems, response.getCreatedItems());
        assertEquals(updatedItems, response.getUpdatedItems());
        assertEquals("create", response.getMode());
        assertEquals(errors, response.getErrors());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testDetermineStatusWithErrors() {
        List<Map<String, Object>> errors = Arrays.asList(new HashMap<>());
        String status = ImportResponse.determineStatus(true, false);
        assertEquals("PARTIAL_SUCCESS", status);

        status = ImportResponse.determineStatus(true, true);
        assertEquals("PARTIAL_SUCCESS", status);
    }

    @Test
    public void testDetermineStatusWithoutErrors() {
        List<Map<String, Object>> errors = Arrays.asList();
        String status = ImportResponse.determineStatus(false, false);
        assertEquals("SUCCESS", status);

        status = ImportResponse.determineStatus(false, true);
        assertEquals("SUCCESS_DRY_RUN", status);
    }

    @Test
    public void testBuilderWithAutomaticStatus() {
        ImportResponse response = ImportResponse.builder()
            .totalRows(2)
            .successfulImports(2)
            .failedImports(0)
            .errors(Arrays.asList()) // No errors
            .mode("create") // Not dry run
            .build();

        // Status should be automatically determined as SUCCESS
        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    public void testBuilderWithAutomaticStatusDryRun() {
        ImportResponse response = ImportResponse.builder()
            .totalRows(2)
            .successfulImports(2)
            .failedImports(0)
            .errors(Arrays.asList()) // No errors
            .mode("dryRun") // Dry run
            .build();

        // Status should be automatically determined as SUCCESS_DRY_RUN
        assertEquals("SUCCESS_DRY_RUN", response.getStatus());
    }

    @Test
    public void testTimestampIsSet() {
        LocalDateTime before = LocalDateTime.now();
        ImportResponse response = new ImportResponse();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(before) || response.getTimestamp().equals(before));
        assertTrue(response.getTimestamp().isBefore(after) || response.getTimestamp().equals(after));
    }
}