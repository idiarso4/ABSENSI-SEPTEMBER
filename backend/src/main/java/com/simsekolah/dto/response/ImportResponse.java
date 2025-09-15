package com.simsekolah.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Standardized response DTO for Excel import operations
 * Provides consistent structure across all import endpoints
 */
public class ImportResponse {

    private String filename;
    private int totalRows;
    private int successfulImports;
    private int failedImports;
    private List<String> createdItems;
    private List<String> updatedItems;
    private String mode;
    private List<Map<String, Object>> errors;
    private String status;
    private LocalDateTime timestamp;

    // Constructors
    public ImportResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ImportResponse(String filename, int totalRows, int successfulImports, int failedImports,
                         List<String> createdItems, List<String> updatedItems, String mode,
                         List<Map<String, Object>> errors, String status) {
        this.filename = filename;
        this.totalRows = totalRows;
        this.successfulImports = successfulImports;
        this.failedImports = failedImports;
        this.createdItems = createdItems;
        this.updatedItems = updatedItems;
        this.mode = mode;
        this.errors = errors;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getSuccessfulImports() {
        return successfulImports;
    }

    public void setSuccessfulImports(int successfulImports) {
        this.successfulImports = successfulImports;
    }

    public int getFailedImports() {
        return failedImports;
    }

    public void setFailedImports(int failedImports) {
        this.failedImports = failedImports;
    }

    public List<String> getCreatedItems() {
        return createdItems;
    }

    public void setCreatedItems(List<String> createdItems) {
        this.createdItems = createdItems;
    }

    public List<String> getUpdatedItems() {
        return updatedItems;
    }

    public void setUpdatedItems(List<String> updatedItems) {
        this.updatedItems = updatedItems;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Map<String, Object>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, Object>> errors) {
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Utility methods for status determination
    public static String determineStatus(boolean hasErrors, boolean isDryRun) {
        if (hasErrors) {
            return "PARTIAL_SUCCESS";
        } else {
            return isDryRun ? "SUCCESS_DRY_RUN" : "SUCCESS";
        }
    }

    // Builder pattern for fluent API
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String filename;
        private int totalRows;
        private int successfulImports;
        private int failedImports;
        private List<String> createdItems;
        private List<String> updatedItems;
        private String mode;
        private List<Map<String, Object>> errors;
        private String status;
        private LocalDateTime timestamp;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder totalRows(int totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        public Builder successfulImports(int successfulImports) {
            this.successfulImports = successfulImports;
            return this;
        }

        public Builder failedImports(int failedImports) {
            this.failedImports = failedImports;
            return this;
        }

        public Builder createdItems(List<String> createdItems) {
            this.createdItems = createdItems;
            return this;
        }

        public Builder updatedItems(List<String> updatedItems) {
            this.updatedItems = updatedItems;
            return this;
        }

        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public Builder errors(List<Map<String, Object>> errors) {
            this.errors = errors;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ImportResponse build() {
            ImportResponse response = new ImportResponse();
            response.setFilename(this.filename);
            response.setTotalRows(this.totalRows);
            response.setSuccessfulImports(this.successfulImports);
            response.setFailedImports(this.failedImports);
            response.setCreatedItems(this.createdItems);
            response.setUpdatedItems(this.updatedItems);
            response.setMode(this.mode);
            response.setErrors(this.errors);
            response.setStatus(this.status != null ? this.status :
                ImportResponse.determineStatus(this.errors != null && !this.errors.isEmpty(), "dryRun".equalsIgnoreCase(this.mode)));
            response.setTimestamp(this.timestamp != null ? this.timestamp : LocalDateTime.now());
            return response;
        }
    }
}