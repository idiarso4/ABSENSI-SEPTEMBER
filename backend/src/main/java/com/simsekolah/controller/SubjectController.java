package com.simsekolah.controller;

import com.simsekolah.dto.response.ImportResponse;
import com.simsekolah.entity.Subject;
import com.simsekolah.service.ExportService;
import com.simsekolah.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Subject Management", description = "Subject management endpoints")
@RestController
@RequestMapping("/api/v1/subjects")
@Validated
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExportService exportService;

    @PostMapping
    @Operation(summary = "Create subject", description = "Create a new subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subject created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Subject code already exists")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        logger.info("Creating new subject with code: {}", subject.getCode());
        try {
            Subject createdSubject = subjectService.createSubject(subject);
            logger.info("Successfully created subject with ID: {}", createdSubject.getId());
            return ResponseEntity.status(201).body(createdSubject);
        } catch (Exception e) {
            logger.error("Failed to create subject with code: {}", subject.getCode(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieve all subjects with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<Subject>> getAllSubjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Fetching all subjects - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Subject> subjects = subjectService.getAllSubjects(pageable);
        logger.debug("Retrieved {} subjects", subjects.getTotalElements());
        
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieve subject information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject found"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Subject> getSubjectById(@PathVariable("id") @NotNull Long subjectId) {
        logger.debug("Fetching subject by ID: {}", subjectId);
        
        Optional<Subject> subject = subjectService.getSubjectById(subjectId);
        if (subject.isPresent()) {
            logger.debug("Subject found with ID: {}", subjectId);
            return ResponseEntity.ok(subject.get());
        } else {
            logger.debug("Subject not found with ID: {}", subjectId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get subject by code", description = "Retrieve subject information by code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject found"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Subject> getSubjectByCode(@PathVariable("code") String code) {
        logger.debug("Fetching subject by code: {}", code);
        
        Optional<Subject> subject = subjectService.getSubjectByCode(code);
        if (subject.isPresent()) {
            logger.debug("Subject found with code: {}", code);
            return ResponseEntity.ok(subject.get());
        } else {
            logger.debug("Subject not found with code: {}", code);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", description = "Update subject information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject updated successfully"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable("id") @NotNull Long subjectId,
            @Valid @RequestBody Subject subjectDetails) {
        
        logger.info("Updating subject with ID: {}", subjectId);
        
        try {
            Subject updatedSubject = subjectService.updateSubject(subjectId, subjectDetails);
            logger.info("Successfully updated subject with ID: {}", subjectId);
            return ResponseEntity.ok(updatedSubject);
        } catch (Exception e) {
            logger.error("Failed to update subject with ID: {}", subjectId, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", description = "Delete subject record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Subject deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable("id") @NotNull Long subjectId) {
        logger.info("Deleting subject with ID: {}", subjectId);
        
        try {
            subjectService.deleteSubject(subjectId);
            logger.info("Successfully deleted subject with ID: {}", subjectId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete subject with ID: {}", subjectId, e);
            throw e;
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search subjects", description = "Search subjects by name or code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<Subject>> searchSubjects(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Searching subjects with query: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Subject> subjects = subjectService.searchSubjects(query, pageable);
        
        logger.debug("Found {} subjects matching query: {}", subjects.getTotalElements(), query);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get subjects by type", description = "Retrieve subjects by type (THEORY, PRACTICE, MIXED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Subject>> getSubjectsByType(@PathVariable Subject.SubjectType type) {
        logger.debug("Fetching subjects by type: {}", type);
        
        List<Subject> subjects = subjectService.getSubjectsByType(type);
        logger.debug("Retrieved {} subjects of type: {}", subjects.size(), type);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get subject statistics", description = "Retrieve subject count and distribution statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getSubjectStatistics() {
        logger.debug("Fetching subject statistics");
        
        try {
            long activeCount = subjectService.countActiveSubjects();
            Map<String, Object> stats = new HashMap<>();
            stats.put("activeCount", activeCount);
            stats.put("totalCount", subjectService.getAllSubjects(PageRequest.of(0, 1)).getTotalElements());
            stats.put("timestamp", System.currentTimeMillis());
            
            logger.debug("Retrieved subject statistics");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Failed to get subject statistics", e);
            throw e;
        }
    }

    /**
     * Download subject import template (Excel)
     */
    @GetMapping("/excel/template")
    @Operation(summary = "Download subject template", description = "Download Excel template for subject import")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> downloadSubjectTemplate() {
        logger.info("Downloading Excel template for subjects");

    List<String> headers = List.of("Code", "Name", "Type", "Description");
    // Add example subjects
    Map<String, Object> s1 = new HashMap<>();
    s1.put("Code", "MAT");
    s1.put("Name", "Matematika");
    s1.put("Type", "THEORY");
    s1.put("Description", "Pelajaran matematika dasar");
    Map<String, Object> s2 = new HashMap<>();
    s2.put("Code", "RPL101");
    s2.put("Name", "Pemrograman Dasar");
    s2.put("Type", "PRACTICE");
    s2.put("Description", "Dasar-dasar pemrograman");
    List<Map<String, Object>> sample = List.of(s1, s2);
    byte[] excel = exportService.exportListToExcel(sample, headers, "Subjects");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "subject_import_template.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export subjects to Excel
     */
    @PostMapping("/excel/export")
    @Operation(summary = "Export subjects to Excel", description = "Export all subjects to an Excel file")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportSubjectsToExcel() {
        logger.info("Exporting subjects to Excel");

        Page<Subject> page = subjectService.getAllSubjects(Pageable.unpaged());
        List<Subject> subjects = page.getContent();

        List<Map<String, Object>> rows = subjects.stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("Code", s.getCode());
            m.put("Name", s.getName());
            m.put("Type", s.getSubjectType() != null ? s.getSubjectType().name() : "");
            m.put("Description", s.getDescription());
            return m;
        }).toList();

        List<String> headers = List.of("Code", "Name", "Type", "Description");
        byte[] excel = exportService.exportListToExcel(rows, headers, "Subjects");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "subjects_export.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export subjects to Excel (GET variant for convenience)
     */
    @GetMapping("/excel/export")
    @Operation(summary = "Export subjects to Excel (GET)", description = "Export all subjects to an Excel file via GET request")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportSubjectsToExcelGet() {
        return exportSubjectsToExcel();
    }

    /**
     * Import subjects from Excel (placeholder)
     */
    @PostMapping("/excel/import")
    @Operation(summary = "Import subjects from Excel", description = "Import subjects from an .xlsx file with columns: Code, Name, Type, Description")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> importSubjectsFromExcel(@RequestParam("file") MultipartFile file,
                                                                       @RequestParam(value = "dryRun", required = false, defaultValue = "false") boolean dryRun,
                                                                       @RequestParam(value = "mode", required = false, defaultValue = "create") String mode) {
        logger.info("Received subject Excel import file: {} ({} bytes) (dryRun={}, mode={})", file.getOriginalFilename(), file.getSize(), dryRun, mode);

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new java.util.ArrayList<>();
    List<String> createdItems = new java.util.ArrayList<>();
    List<String> updatedItems = new java.util.ArrayList<>();

        // Basic validation
        if (file == null || file.isEmpty()) {
            result.put("message", "No file uploaded");
            result.put("status", "BAD_REQUEST");
            return ResponseEntity.badRequest().body(result);
        }

        // Validate file type (xlsx) and row count conservatively
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
            result.put("message", "Invalid file type. Please upload an .xlsx Excel file.");
            result.put("status", "BAD_REQUEST");
            return ResponseEntity.badRequest().body(result);
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                result.put("message", "Excel file does not contain any sheets");
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            // Guard against excessively large files (default 5000, same as teacher)
            int dataRows = sheet.getLastRowNum();
            if (dataRows > 5000) {
                result.put("message", "Too many rows in Excel: " + dataRows + ". Maximum allowed: 5000");
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            // Read header row to map columns
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                result.put("message", "Header row (first row) is missing");
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            Map<String, Integer> colIdx = mapHeaderColumns(headerRow);

            int totalRows = 0;
            int successful = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Skip completely empty rows
                if (isRowEmpty(row)) continue;

                totalRows++;
                int rowNumber = i + 1; // human-friendly

                try {
                    String code = getCellString(row, colIdx.get("code"));
                    String name = getCellString(row, colIdx.get("name"));
                    String typeStr = getCellString(row, colIdx.get("type"));
                    String description = getCellString(row, colIdx.get("description"));

                    // Validate mandatory fields
                    if (code == null || code.isBlank()) {
                        addError(errors, rowNumber, "Code", code, "Code is required");
                        continue;
                    }
                    if (name == null || name.isBlank()) {
                        addError(errors, rowNumber, "Name", name, "Name is required");
                        continue;
                    }

                    // Check for duplicates
                    Optional<Subject> existingSubject = subjectService.getSubjectByCode(code);
                    boolean exists = existingSubject.isPresent();
                    if (exists && !"upsert".equalsIgnoreCase(mode)) {
                        addError(errors, rowNumber, "Code", code, "Subject with this code already exists");
                        continue;
                    }

                    Subject subject = new Subject();
                    subject.setCode(code.trim());
                    subject.setName(name.trim());
                    subject.setDescription((description != null && !description.isBlank()) ? description.trim() : null);
                    subject.setIsActive(true);

                    // Parse subject type if provided
                    if (typeStr != null && !typeStr.isBlank()) {
                        Subject.SubjectType parsedType = parseSubjectType(typeStr);
                        if (parsedType == null) {
                            addError(errors, rowNumber, "Type", typeStr, "Invalid type. Use THEORY, PRACTICE, or MIXED");
                            continue;
                        }
                        subject.setSubjectType(parsedType);
                    }

                    // Persist
                    if (!dryRun) {
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            // Update existing subject
                            subject.setId(existingSubject.get().getId()); // Set ID for update
                            subjectService.updateSubject(existingSubject.get().getId(), subject);
                            updatedItems.add(code.trim());
                        } else {
                            // Create new subject
                            subjectService.createSubject(subject);
                            createdItems.add(code.trim());
                        }
                    } else {
                        // Dry run - just add to appropriate list
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            updatedItems.add(code.trim());
                        } else {
                            createdItems.add(code.trim());
                        }
                    }
                    successful++;
                } catch (Exception e) {
                    addError(errors, rowNumber, "General", "", e.getMessage());
                }
            }

            ImportResponse response = ImportResponse.builder()
                .filename(file.getOriginalFilename())
                .totalRows(totalRows)
                .successfulImports(successful)
                .failedImports(totalRows - successful)
                .createdItems(createdItems)
                .updatedItems(updatedItems)
                .mode(mode)
                .errors(errors)
                .status(errors.isEmpty() ? (dryRun ? "SUCCESS_DRY_RUN" : "SUCCESS") : "PARTIAL_SUCCESS")
                .build();

            logger.info("Subject import completed: total={}, success={}, failed={}, created={}, updated={}, mode={}", 
                       totalRows, successful, totalRows - successful, createdItems.size(), updatedItems.size(), mode);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Failed to import subjects from Excel", ex);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to process Excel file: " + ex.getMessage());
            errorResponse.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Map header names to column indices (case-insensitive with common aliases)
    private Map<String, Integer> mapHeaderColumns(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            Cell cell = headerRow.getCell(c);
            if (cell == null) continue;
            String raw = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell);
            if (raw == null) continue;
            String key = raw.trim().toLowerCase();
            switch (key) {
                case "code":
                case "kode":
                case "kode mapel":
                    map.put("code", c); break;
                case "name":
                case "nama":
                case "nama mapel":
                    map.put("name", c); break;
                case "type":
                case "tipe":
                case "jenis":
                    map.put("type", c); break;
                case "description":
                case "deskripsi":
                case "keterangan":
                    map.put("description", c); break;
                default:
                    // ignore unknown headers
            }
        }
        return map;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String v = getCellString(cell);
                if (v != null && !v.isBlank()) return false;
            }
        }
        return true;
    }

    private String getCellString(Row row, Integer col) {
        if (col == null) return null;
        return getCellString(row.getCell(col));
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                if (Math.floor(d) == d) {
                    yield String.valueOf((long) d);
                }
                yield String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }

    private Subject.SubjectType parseSubjectType(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toUpperCase();
        // Accept some aliases in EN/ID
        if (v.equals("THEORY") || v.equals("TEORI")) return Subject.SubjectType.THEORY;
        if (v.equals("PRACTICE") || v.equals("PRAKTIK") || v.equals("PRAKTEK")) return Subject.SubjectType.PRACTICE;
        if (v.equals("MIXED") || v.equals("CAMPURAN")) return Subject.SubjectType.MIXED;
        try {
            return Subject.SubjectType.valueOf(v);
        } catch (Exception ignored) {
            return null;
        }
    }

    private void addError(List<Map<String, Object>> errors, int rowNumber, String field, String value, String message) {
        Map<String, Object> err = new HashMap<>();
        err.put("row", rowNumber);
        err.put("field", field);
        err.put("value", value);
        err.put("message", message);
        errors.add(err);
    }
}