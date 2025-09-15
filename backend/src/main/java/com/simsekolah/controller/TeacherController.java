package com.simsekolah.controller;

import com.simsekolah.dto.response.ImportResponse;
import com.simsekolah.dto.response.UserResponse;
import com.simsekolah.entity.User;
import com.simsekolah.enums.UserType;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.service.ExportService;
import com.simsekolah.service.UserService;
import com.simsekolah.dto.request.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for teacher management operations
 * Provides endpoints for teacher CRUD operations, search, filtering, and statistics
 */
@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teacher Management", description = "Teacher management endpoints")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExportService exportService;

    @Value("${app.import.teacher.default-password:Password123!}")
    private String teacherDefaultPassword;

    @Value("${app.import.excel.allowed-content-types:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet}")
    private String allowedContentTypes;

    @Value("${app.import.max-rows:5000}")
    private int maxRows;

    /**
     * Get all teachers with pagination and sorting
     */
    @GetMapping
    @Operation(summary = "Get all teachers", description = "Retrieve all teachers with pagination and sorting")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<UserResponse>> getAllTeachers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "firstName") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Teachers list request received - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);

        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<UserResponse> teachers = userService.getUsersByType(UserType.TEACHER, pageable);
            
            logger.info("Teachers retrieved successfully - total: {}, page: {}", 
                       teachers.getTotalElements(), teachers.getNumber());
            
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            logger.error("Error retrieving teachers", e);
            throw e;
        }
    }

    /**
     * Get teacher by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieve a specific teacher by their ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<UserResponse> getTeacherById(@PathVariable("id") @NotNull Long teacherId) {
        logger.debug("Fetching teacher by ID: {}", teacherId);
        
        return userService.getUserById(teacherId)
                .filter(user -> user.getUserType() == UserType.TEACHER)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search teachers by name or email
     */
    @GetMapping("/search")
    @Operation(summary = "Search teachers", description = "Search teachers by name or email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<UserResponse>> searchTeachers(
            @Parameter(description = "Search query")
            @RequestParam String query,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.info("Teacher search request - query: '{}', page: {}, size: {}", query, page, size);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("firstName"));
            Page<UserResponse> teachers = userService.searchUsersByTypeAndQuery(UserType.TEACHER, query, pageable);
            
            logger.info("Teacher search completed - found: {} teachers", teachers.getTotalElements());
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            logger.error("Error searching teachers with query: {}", query, e);
            throw e;
        }
    }

    /**
     * Get active teachers only
     */
    @GetMapping("/active")
    @Operation(summary = "Get active teachers", description = "Retrieve only active teachers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<UserResponse>> getActiveTeachers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.info("Active teachers request received - page: {}, size: {}", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("firstName"));
            Page<UserResponse> teachers = userService.getActiveUsersByType(UserType.TEACHER, pageable);
            
            logger.info("Active teachers retrieved successfully - total: {}", teachers.getTotalElements());
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            logger.error("Error retrieving active teachers", e);
            throw e;
        }
    }

    /**
     * Get teacher statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get teacher statistics", description = "Retrieve teacher statistics and metrics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getTeacherStats() {
        logger.info("Teacher stats request received");

        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Basic counts
            long totalTeachers = userRepository.countByUserType(UserType.TEACHER);
            long activeTeachers = userRepository.countByUserTypeAndIsActive(UserType.TEACHER, true);
            long inactiveTeachers = totalTeachers - activeTeachers;
            
            stats.put("totalTeachers", totalTeachers);
            stats.put("activeTeachers", activeTeachers);
            stats.put("inactiveTeachers", inactiveTeachers);
            
            // Recent additions (last 30 days)
            java.time.LocalDateTime thirtyDaysAgo = java.time.LocalDateTime.now().minusDays(30);
            long recentTeachers = userRepository.countByUserTypeAndCreatedAtAfter(UserType.TEACHER, thirtyDaysAgo);
            stats.put("recentTeachers", recentTeachers);
            
            // Department distribution (placeholder - can be enhanced with actual department data)
            Map<String, Long> departmentStats = new HashMap<>();
            departmentStats.put("General", totalTeachers);
            stats.put("byDepartment", departmentStats);
            
            logger.info("Teacher stats retrieved successfully");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving teacher stats", e);
            throw e;
        }
    }

    /**
     * Get teachers by department (placeholder implementation)
     */
    @GetMapping("/department/{department}")
    @Operation(summary = "Get teachers by department", description = "Retrieve teachers by department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<UserResponse>> getTeachersByDepartment(
            @PathVariable("department") String department) {
        
        logger.info("Teachers by department request - department: {}", department);
        
        try {
            // For now, return all teachers as we don't have department field
            List<User> teachers = userRepository.findByUserType(UserType.TEACHER);
            List<UserResponse> teacherResponses = teachers.stream()
                    .map(UserResponse::from)
                    .collect(Collectors.toList());
            
            logger.info("Teachers by department retrieved - count: {}", teacherResponses.size());
            return ResponseEntity.ok(teacherResponses);
        } catch (Exception e) {
            logger.error("Error retrieving teachers by department: {}", department, e);
            throw e;
        }
    }

    /**
     * Download teacher import template (Excel)
     */
    @GetMapping("/excel/template")
    @Operation(summary = "Download teacher template", description = "Download Excel template for teacher import")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> downloadTeacherTemplate() {
        logger.info("Downloading Excel template for teachers");

        // Define headers/columns for the template
    List<String> headers = List.of("NIP", "Full Name", "Email", "Phone", "Address", "Status");
    Map<String, Object> t1 = new HashMap<>();
    t1.put("NIP", "19800101");
    t1.put("Full Name", "Ahmad Wijaya");
    t1.put("Email", "ahmad.wijaya@sekolah.sch.id");
    t1.put("Phone", "081234567890");
    t1.put("Address", "Jl. Merdeka No. 1");
    t1.put("Status", "ACTIVE");
    Map<String, Object> t2 = new HashMap<>();
    t2.put("NIP", "19791212");
    t2.put("Full Name", "Siti Nurhaliza");
    t2.put("Email", "siti.nurhaliza@sekolah.sch.id");
    t2.put("Phone", "081298765432");
    t2.put("Address", "Jl. Sudirman No. 10");
    t2.put("Status", "ACTIVE");
    List<Map<String, Object>> sample = List.of(t1, t2);

    byte[] excel = exportService.exportListToExcel(sample, headers, "Teachers");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "teacher_import_template.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export teachers to Excel
     */
    @PostMapping("/excel/export")
    @Operation(summary = "Export teachers to Excel", description = "Export all teachers to an Excel file")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportTeachersToExcel() {
        logger.info("Exporting teachers to Excel");

        Page<UserResponse> page = userService.getUsersByType(UserType.TEACHER, Pageable.unpaged());
        List<UserResponse> teachers = page.getContent();

        // Map to a simple list of maps for export
        List<Map<String, Object>> rows = teachers.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("NIP", t.getNip());
            m.put("Full Name", t.getName());
            m.put("Email", t.getEmail());
            m.put("Phone", t.getPhone());
            m.put("Address", t.getAddress());
            m.put("Status", t.isActive() ? "ACTIVE" : "INACTIVE");
            return m;
        }).collect(Collectors.toList());

        List<String> headers = List.of("NIP", "Full Name", "Email", "Phone", "Address", "Status");
        byte[] excel = exportService.exportListToExcel(rows, headers, "Teachers");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "teachers_export.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export teachers to Excel (GET variant for convenience)
     * Some clients trigger downloads via GET links; this mirrors the POST behavior.
     */
    @GetMapping("/excel/export")
    @Operation(summary = "Export teachers to Excel (GET)", description = "Export all teachers to an Excel file via GET request")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportTeachersToExcelGet() {
        return exportTeachersToExcel();
    }

    /**
     * Import teachers from Excel (placeholder)
     */
    @PostMapping("/excel/import")
    @Operation(summary = "Import teachers from Excel", description = "Import teachers from an .xlsx file with columns: NIP, Full Name, Email, Phone, Address, Status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> importTeachersFromExcel(@RequestParam("file") MultipartFile file,
                                                                       @RequestParam(value = "dryRun", required = false, defaultValue = "false") boolean dryRun,
                                                                       @RequestParam(value = "mode", required = false, defaultValue = "create") String mode) {
        logger.info("Received teacher Excel import file: {} ({} bytes) (dryRun={}, mode={})", file.getOriginalFilename(), file.getSize(), dryRun, mode);

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new java.util.ArrayList<>();
    List<String> createdItems = new java.util.ArrayList<>();
    List<String> updatedItems = new java.util.ArrayList<>();

        if (file == null || file.isEmpty()) {
            result.put("message", "No file uploaded");
            result.put("status", "BAD_REQUEST");
            return ResponseEntity.badRequest().body(result);
        }

        // Content-Type validation (allow .xlsx)
    String contentType = file.getContentType();
    String originalFilename = file.getOriginalFilename();
        boolean contentOk = false;
        try {
            java.util.Set<String> allowed = new java.util.HashSet<>();
            for (String ct : allowedContentTypes.split(",")) {
                String t = ct.trim();
                if (!t.isEmpty()) allowed.add(t);
            }
            contentOk = (contentType != null && allowed.contains(contentType)) ||
                        (originalFilename != null && originalFilename.toLowerCase().endsWith(".xlsx"));
        } catch (Exception ignore) { /* fallback to filename check above */ }
        if (!contentOk) {
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

            // Max rows validation (excluding header)
            int dataRows = sheet.getLastRowNum();
            if (dataRows > maxRows) {
                result.put("message", "Too many rows in Excel: " + dataRows + ". Maximum allowed: " + maxRows);
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            Row header = sheet.getRow(0);
            if (header == null) {
                result.put("message", "Header row (first row) is missing");
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            Map<String, Integer> colIdx = new HashMap<>();
            for (int c = 0; c < header.getLastCellNum(); c++) {
                Cell cell = header.getCell(c);
                if (cell == null) continue;
                String raw = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell);
                if (raw == null) continue;
                String key = raw.trim().toLowerCase();
                switch (key) {
                    case "nip": colIdx.put("nip", c); break;
                    case "full name":
                    case "name":
                    case "nama": colIdx.put("name", c); break;
                    case "email": colIdx.put("email", c); break;
                    case "phone":
                    case "telepon": colIdx.put("phone", c); break;
                    case "address":
                    case "alamat": colIdx.put("address", c); break;
                    case "status": colIdx.put("status", c); break;
                    default: // ignore
                }
            }

            // Validate required headers
            if (!colIdx.containsKey("nip") || !colIdx.containsKey("name") || !colIdx.containsKey("email")) {
                result.put("message", "Required headers missing. Need at least: NIP, Full Name, Email");
                result.put("status", "BAD_REQUEST");
                return ResponseEntity.badRequest().body(result);
            }

            int totalRows = 0;
            int successful = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                if (isRowEmpty(row)) continue;

                totalRows++;
                int rowNum = i + 1;

                try {
                    String nip = getCellString(row, colIdx.get("nip"));
                    String name = getCellString(row, colIdx.get("name"));
                    String email = getCellString(row, colIdx.get("email"));
                    String phone = getCellString(row, colIdx.get("phone"));
                    String address = getCellString(row, colIdx.get("address"));
                    String status = getCellString(row, colIdx.get("status"));

                    if (nip == null || nip.isBlank()) { addError(errors, rowNum, "NIP", nip, "NIP is required"); continue; }
                    if (name == null || name.isBlank()) { addError(errors, rowNum, "Name", name, "Full Name is required"); continue; }
                    if (email == null || email.isBlank()) { addError(errors, rowNum, "Email", email, "Email is required"); continue; }

                    // Check for duplicates
                    boolean nipExists = userService.existsByNip(nip);
                    boolean emailExists = userService.existsByEmail(email);
                    
                    if (!"upsert".equalsIgnoreCase(mode)) {
                        if (nipExists) { addError(errors, rowNum, "NIP", nip, "NIP already exists"); continue; }
                        if (emailExists) { addError(errors, rowNum, "Email", email, "Email already exists"); continue; }
                    } else {
                        // For upsert mode, only NIP is the key. Email can be updated.
                        // If NIP doesn't exist, we'll create new. If it does, we'll update.
                        // But if email exists and it's not the same user (different NIP), that's an error.
                        if (emailExists && !nipExists) {
                            addError(errors, rowNum, "Email", email, "Email already exists for different user"); continue;
                        }
                    }

                    com.simsekolah.dto.request.CreateUserRequest req = new com.simsekolah.dto.request.CreateUserRequest();
                    req.setName(name.trim());
                    req.setEmail(email.trim());
                    req.setPhone(phone != null ? phone.trim() : null);
                    req.setAddress(address != null ? address.trim() : null);
                    req.setNip(nip.trim());
                    req.setUserType(UserType.TEACHER);
                    // default password policy from configuration
                    req.setPassword(teacherDefaultPassword);
                    // active status mapping
                    boolean isActive = status == null || status.isBlank() || status.equalsIgnoreCase("ACTIVE") || status.equalsIgnoreCase("AKTIF");
                    req.setActive(isActive);

                    if (!dryRun) {
                        if (nipExists && "upsert".equalsIgnoreCase(mode)) {
                            // Update existing teacher
                            Optional<UserResponse> existingUser = userService.getUserByNip(nip.trim());
                            if (existingUser.isPresent()) {
                                UpdateUserRequest updateReq = new UpdateUserRequest();
                                updateReq.setName(req.getName());
                                updateReq.setEmail(req.getEmail());
                                updateReq.setNip(req.getNip()); // NIP can be updated too
                                updateReq.setPhone(req.getPhone());
                                updateReq.setAddress(req.getAddress());
                                updateReq.setUserType(req.getUserType());
                                updateReq.setActive(req.isActive());
                                
                                UserResponse updated = userService.updateUser(existingUser.get().getId(), updateReq);
                                updatedItems.add(updated.getNip());
                            } else {
                                // This shouldn't happen since we checked exists, but just in case
                                addError(errors, rowNum, "NIP", nip, "Teacher exists but could not be found for update");
                                continue;
                            }
                        } else {
                            // Create new teacher
                            UserResponse created = userService.createUser(req);
                            createdItems.add(created.getNip());
                        }
                    } else {
                        // Dry run - just add to appropriate list
                        if (nipExists && "upsert".equalsIgnoreCase(mode)) {
                            updatedItems.add(req.getNip());
                        } else {
                            createdItems.add(req.getNip());
                        }
                    }
                    successful++;
                } catch (Exception e) {
                    addError(errors, rowNum, "General", "", e.getMessage());
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

            logger.info("Teacher import completed: total={}, success={}, failed={}, created={}, updated={}, mode={}", 
                       totalRows, successful, totalRows - successful, createdItems.size(), updatedItems.size(), mode);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Failed to import teachers from Excel", ex);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to process Excel file: " + ex.getMessage());
            errorResponse.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
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

    private void addError(List<Map<String, Object>> errors, int rowNumber, String field, String value, String message) {
        Map<String, Object> err = new HashMap<>();
        err.put("row", rowNumber);
        err.put("field", field);
        err.put("value", value);
        err.put("message", message);
        errors.add(err);
    }
}
