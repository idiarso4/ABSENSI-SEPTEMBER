package com.simsekolah.controller;

import com.simsekolah.dto.response.ImportResponse;
import com.simsekolah.entity.ClassRoom;
import com.simsekolah.entity.Major;
import com.simsekolah.entity.User;
import com.simsekolah.service.ClassRoomService;
import com.simsekolah.service.ExportService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for classroom management operations
 * Provides endpoints for classroom CRUD operations, search, and filtering
 */
@RestController
@RequestMapping("/api/v1/classrooms")
@Tag(name = "Classroom Management", description = "Classroom management endpoints")
@Validated
public class ClassRoomController {

    private static final Logger logger = LoggerFactory.getLogger(ClassRoomController.class);

    @Autowired
    private ClassRoomService classRoomService;

    @Autowired
    private ExportService exportService;

    @Autowired
    private com.simsekolah.repository.MajorRepository majorRepository;

    @Autowired
    private com.simsekolah.repository.UserRepository userRepository;

    @Autowired
    private com.simsekolah.repository.ClassRoomRepository classRoomRepository;



    /**
     * Create a new classroom
     */
    @PostMapping
    @Operation(summary = "Create classroom", description = "Create a new classroom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Classroom created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Class name already exists")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ClassRoom> createClassRoom(@Valid @RequestBody ClassRoom classRoom) {
        logger.info("Creating new classroom: {}", classRoom.getName());
        try {
            ClassRoom createdClassRoom = classRoomService.createClassRoom(classRoom);
            logger.info("Successfully created classroom with ID: {}", createdClassRoom.getId());
            return ResponseEntity.status(201).body(createdClassRoom);
        } catch (Exception e) {
            logger.error("Failed to create classroom: {}", classRoom.getName(), e);
            throw e;
        }
    }

    /**
     * Get all classrooms with pagination
     */
    @GetMapping
    @Operation(summary = "Get all classrooms", description = "Retrieve all classrooms with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classrooms retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<ClassRoom>> getAllClassRooms(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Fetching all classrooms - page: {}, size: {}", page, size);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ClassRoom> classRooms = classRoomService.getAllClassRooms(pageable);
        
        logger.debug("Retrieved {} classrooms", classRooms.getTotalElements());
        return ResponseEntity.ok(classRooms);
    }

    /**
     * Get classroom by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get classroom by ID", description = "Retrieve a specific classroom by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ClassRoom> getClassRoomById(@PathVariable("id") @NotNull Long classRoomId) {
        logger.debug("Fetching classroom with ID: {}", classRoomId);
        
        return classRoomService.getClassRoomById(classRoomId)
                .map(classRoom -> {
                    logger.debug("Found classroom: {}", classRoom.getName());
                    return ResponseEntity.ok(classRoom);
                })
                .orElseGet(() -> {
                    logger.warn("Classroom not found with ID: {}", classRoomId);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Update classroom
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update classroom", description = "Update classroom information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom updated successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ClassRoom> updateClassRoom(
            @PathVariable("id") @NotNull Long classRoomId,
            @Valid @RequestBody ClassRoom classRoomDetails) {
        
        logger.info("Updating classroom with ID: {}", classRoomId);
        
        try {
            ClassRoom updatedClassRoom = classRoomService.updateClassRoom(classRoomId, classRoomDetails);
            logger.info("Successfully updated classroom with ID: {}", classRoomId);
            return ResponseEntity.ok(updatedClassRoom);
        } catch (Exception e) {
            logger.error("Failed to update classroom with ID: {}", classRoomId, e);
            throw e;
        }
    }

    /**
     * Delete classroom
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete classroom", description = "Delete classroom record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Classroom deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Classroom not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable("id") @NotNull Long classRoomId) {
        logger.info("Deleting classroom with ID: {}", classRoomId);
        
        try {
            classRoomService.deleteClassRoom(classRoomId);
            logger.info("Successfully deleted classroom with ID: {}", classRoomId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete classroom with ID: {}", classRoomId, e);
            throw e;
        }
    }

    /**
     * Get classrooms by grade
     */
    @GetMapping("/grade/{grade}")
    @Operation(summary = "Get classrooms by grade", description = "Retrieve all classrooms for a specific grade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classrooms retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<ClassRoom>> getClassRoomsByGrade(
            @PathVariable("grade") @NotNull Integer grade,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Fetching classrooms for grade: {}", grade);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<ClassRoom> classRooms = classRoomService.getClassRoomsByGrade(grade, pageable);
        
        logger.debug("Retrieved {} classrooms for grade {}", classRooms.getTotalElements(), grade);
        return ResponseEntity.ok(classRooms);
    }

    /**
     * Get active classrooms
     */
    @GetMapping("/active")
    @Operation(summary = "Get active classrooms", description = "Retrieve all active classrooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active classrooms retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<ClassRoom>> getActiveClassRooms(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Fetching active classrooms");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "grade", "name"));
        Page<ClassRoom> classRooms = classRoomService.getActiveClassRooms(pageable);
        
        logger.debug("Retrieved {} active classrooms", classRooms.getTotalElements());
        return ResponseEntity.ok(classRooms);
    }

    /**
     * Get classroom statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get classroom statistics", description = "Retrieve classroom statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getClassRoomStats() {
        logger.info("Classroom stats request received");
        
        try {
            Map<String, Object> stats = classRoomService.getClassRoomStatistics();
            stats.put("timestamp", System.currentTimeMillis());
            
            logger.info("Classroom stats retrieved successfully");
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error retrieving classroom stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve classroom statistics");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Search classrooms
     */
    @GetMapping("/search")
    @Operation(summary = "Search classrooms", description = "Search classrooms by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<ClassRoom>> searchClassRooms(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Searching classrooms with query: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<ClassRoom> classRooms = classRoomService.searchClassRooms(query, pageable);
        
        logger.debug("Found {} classrooms matching query: {}", classRooms.getTotalElements(), query);
        return ResponseEntity.ok(classRooms);
    }

    /**
     * Get classrooms with available space
     */
    @GetMapping("/available")
    @Operation(summary = "Get available classrooms", description = "Retrieve classrooms with available space")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available classrooms retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ClassRoom>> getAvailableClassRooms() {
        
        logger.debug("Fetching classrooms with available space");
        
        List<ClassRoom> classRooms = classRoomService.getClassRoomsWithAvailableSpace();
        
        logger.debug("Retrieved {} classrooms with available space", classRooms.size());
        return ResponseEntity.ok(classRooms);
    }

    /**
     * Download classroom import template (Excel)
     */
    @GetMapping("/excel/template")
    @Operation(summary = "Download classroom template", description = "Download Excel template for classroom import")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> downloadClassRoomTemplate() {
        logger.info("Downloading Excel template for classrooms");

        // Define headers/columns for the template
        List<String> headers = List.of(
            "Class Name",
            "Grade Level",
            "Academic Year",
            "Semester",
            "Major",
            "Homeroom Teacher",
            "Capacity",
            "Current Students",
            "Available Spaces",
            "Occupancy Rate (%)",
            "Location",
            "Status"
        );

    // Add example rows to guide operators
    Map<String, Object> example1 = new HashMap<>();
    example1.put("Class Name", "X RPL 1");
    example1.put("Grade Level", 10);
    example1.put("Academic Year", "2025/2026");
    example1.put("Semester", 1);
    example1.put("Major", "RPL"); // by Code
    example1.put("Homeroom Teacher", "19800101"); // by NIP
    example1.put("Capacity", 36);
    example1.put("Current Students", 0);
    example1.put("Available Spaces", 36);
    example1.put("Occupancy Rate (%)", 0);
    example1.put("Location", "Gedung A - 101");
    example1.put("Status", "ACTIVE");

    Map<String, Object> example2 = new HashMap<>();
    example2.put("Class Name", "XI TKJ 2");
    example2.put("Grade Level", 11);
    example2.put("Academic Year", "2025/2026");
    example2.put("Semester", 1);
    example2.put("Major", "Teknik Komputer Jaringan"); // by Name
    example2.put("Homeroom Teacher", "wali.kelas@sekolah.sch.id"); // by Email
    example2.put("Capacity", 36);
    example2.put("Current Students", 0);
    example2.put("Available Spaces", 36);
    example2.put("Occupancy Rate (%)", 0);
    example2.put("Location", "Gedung B - 203");
    example2.put("Status", "ACTIVE");

    List<Map<String, Object>> sampleData = List.of(example1, example2);
    byte[] excel = exportService.exportListToExcel(sampleData, headers, "Classrooms");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "classrooms_import_template.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export classrooms to Excel (POST)
     */
    @PostMapping("/excel/export")
    @Operation(summary = "Export classrooms to Excel", description = "Export all classrooms to an Excel file")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportClassRoomsToExcel() {
        logger.info("Exporting classrooms to Excel");

        Page<ClassRoom> page = classRoomService.getAllClassRooms(Pageable.unpaged());
        List<ClassRoom> classRooms = page.getContent();

        List<Map<String, Object>> rows = classRooms.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("Class Name", c.getClassName());
            m.put("Grade Level", c.getGradeLevel());
            m.put("Academic Year", c.getAcademicYear());
            m.put("Semester", c.getSemester());
            m.put("Major", c.getMajorName());
            m.put("Homeroom Teacher", c.getHomeroomTeacherName());
            m.put("Capacity", c.getCapacity());
            m.put("Current Students", c.getCurrentStudents());
            m.put("Available Spaces", c.getAvailableSpaces());
            // Round occupancy to 2 decimals
            double occ = c.getOccupancyRate();
            m.put("Occupancy Rate (%)", Math.round(occ * 100.0) / 100.0);
            m.put("Location", c.getLocation());
            m.put("Status", c.isActive() ? "ACTIVE" : "INACTIVE");
            return m;
        }).toList();

        List<String> headers = List.of(
            "Class Name",
            "Grade Level",
            "Academic Year",
            "Semester",
            "Major",
            "Homeroom Teacher",
            "Capacity",
            "Current Students",
            "Available Spaces",
            "Occupancy Rate (%)",
            "Location",
            "Status"
        );

        byte[] excel = exportService.exportListToExcel(rows, headers, "Classrooms");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(exportService.getMimeType("excel")));
        httpHeaders.setContentDispositionFormData("attachment", "classrooms_export.xlsx");
        httpHeaders.setContentLength(excel.length);

        return ResponseEntity.ok().headers(httpHeaders).body(excel);
    }

    /**
     * Export classrooms to Excel (GET variant for convenience)
     */
    @GetMapping("/excel/export")
    @Operation(summary = "Export classrooms to Excel (GET)", description = "Export all classrooms to an Excel file via GET request")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportClassRoomsToExcelGet() {
        return exportClassRoomsToExcel();
    }

    /**
     * Import classrooms from Excel (placeholder)
     */
    @PostMapping("/excel/import")
    @Operation(summary = "Import classrooms from Excel", description = "Import classrooms from an .xlsx file with columns: Class Name, Grade Level, Academic Year, Semester, Major, Homeroom Teacher, Capacity, Location, Status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> importClassRoomsFromExcel(@RequestParam("file") MultipartFile file,
                                                                         @RequestParam(value = "dryRun", required = false, defaultValue = "false") boolean dryRun,
                                                                         @RequestParam(value = "mode", required = false, defaultValue = "create") String mode) {
        logger.info("Received classroom Excel import file: {} ({} bytes) (dryRun={}, mode={})", file.getOriginalFilename(), file.getSize(), dryRun, mode);

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new java.util.ArrayList<>();
    List<String> createdItems = new java.util.ArrayList<>();
    List<String> updatedItems = new java.util.ArrayList<>();

        if (file == null || file.isEmpty()) {
            result.put("message", "No file uploaded");
            result.put("status", "BAD_REQUEST");
            return ResponseEntity.badRequest().body(result);
        }

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

            int dataRows = sheet.getLastRowNum();
            if (dataRows > 5000) {
                result.put("message", "Too many rows in Excel: " + dataRows + ". Maximum allowed: 5000");
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
                    case "class name":
                    case "name": colIdx.put("className", c); break;
                    case "grade level":
                    case "grade": colIdx.put("gradeLevel", c); break;
                    case "academic year": colIdx.put("academicYear", c); break;
                    case "semester": colIdx.put("semester", c); break;
                    case "major":
                    case "jurusan":
                    case "major code":
                    case "kode jurusan": colIdx.put("major", c); break;
                    case "homeroom teacher":
                    case "wali kelas":
                    case "teacher": colIdx.put("homeroom", c); break;
                    case "capacity": colIdx.put("capacity", c); break;
                    case "location": colIdx.put("location", c); break;
                    case "status": colIdx.put("status", c); break;
                    default: // ignore others for now (Major, Homeroom Teacher)
                }
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
                    String className = getCellString(row, colIdx.get("className"));
                    String gradeStr = getCellString(row, colIdx.get("gradeLevel"));
                    String academicYear = getCellString(row, colIdx.get("academicYear"));
                    String semesterStr = getCellString(row, colIdx.get("semester"));
                    String capacityStr = getCellString(row, colIdx.get("capacity"));
                    String majorVal = getCellString(row, colIdx.get("major"));
                    String homeroomVal = getCellString(row, colIdx.get("homeroom"));
                    String location = getCellString(row, colIdx.get("location"));
                    String status = getCellString(row, colIdx.get("status"));

                    if (className == null || className.isBlank()) { addError(errors, rowNum, "Class Name", className, "Class Name is required"); continue; }

                    // Check for duplicates
                    java.util.Optional<ClassRoom> existingClass = classRoomRepository.findByNameIgnoreCase(className.trim());
                    boolean exists = existingClass.isPresent();
                    if (exists && !"upsert".equalsIgnoreCase(mode)) {
                        addError(errors, rowNum, "Class Name", className, "Classroom with this name already exists");
                        continue;
                    }

                    ClassRoom cls = new ClassRoom();
                    cls.setClassName(className.trim());
                    if (gradeStr != null && !gradeStr.isBlank()) {
                        try { cls.setGradeLevel(Integer.parseInt(gradeStr.trim())); } catch (NumberFormatException e) { addError(errors, rowNum, "Grade Level", gradeStr, "Invalid integer"); continue; }
                    }
                    cls.setAcademicYear(academicYear != null ? academicYear.trim() : null);
                    if (semesterStr != null && !semesterStr.isBlank()) {
                        try { cls.setSemester(Integer.parseInt(semesterStr.trim())); } catch (NumberFormatException e) { addError(errors, rowNum, "Semester", semesterStr, "Invalid integer"); continue; }
                    }
                    if (capacityStr != null && !capacityStr.isBlank()) {
                        try { cls.setCapacity(Integer.parseInt(capacityStr.trim())); } catch (NumberFormatException e) { addError(errors, rowNum, "Capacity", capacityStr, "Invalid integer"); continue; }
                    }

                    // Look up Major by code or name (case-insensitive)
                    if (majorVal != null && !majorVal.isBlank()) {
                        String mv = majorVal.trim();
                        java.util.Optional<Major> majorOpt = majorRepository.findByCode(mv);
                        if (majorOpt.isEmpty()) {
                            majorOpt = majorRepository.findByName(mv);
                        }
                        if (majorOpt.isPresent()) {
                            cls.setMajor(majorOpt.get());
                        } else {
                            addError(errors, rowNum, "Major", majorVal, "Major not found by code or name");
                            continue;
                        }
                    }

                    // Look up Homeroom Teacher by NIP or Email
                    if (homeroomVal != null && !homeroomVal.isBlank()) {
                        String hv = homeroomVal.trim();
                        java.util.Optional<User> t = userRepository.findByNip(hv);
                        if (t.isEmpty()) {
                            t = userRepository.findByEmail(hv);
                        }
                        if (t.isPresent()) {
                            cls.setHomeroomTeacher(t.get());
                        } else {
                            addError(errors, rowNum, "Homeroom Teacher", homeroomVal, "Teacher not found by NIP or Email");
                            continue;
                        }
                    }

                    // Parse location into building/room if provided in "Building - Room" style
                    if (location != null && !location.isBlank()) {
                        String loc = location.trim();
                        if (loc.contains("-")) {
                            String[] parts = loc.split("-", 2);
                            cls.setBuilding(parts[0].trim());
                            cls.setRoomNumber(parts[1].trim());
                        } else {
                            cls.setRoomNumber(loc);
                        }
                    }

                    boolean isActive = status == null || status.isBlank() || status.equalsIgnoreCase("ACTIVE") || status.equalsIgnoreCase("AKTIF");
                    cls.setIsActive(isActive);

                    if (!dryRun) {
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            // Update existing classroom
                            cls.setId(existingClass.get().getId()); // Set ID for update
                            ClassRoom updated = classRoomService.updateClassRoom(existingClass.get().getId(), cls);
                            updatedItems.add(updated.getClassName());
                        } else {
                            // Create new classroom
                            ClassRoom saved = classRoomService.createClassRoom(cls);
                            createdItems.add(saved.getClassName());
                        }
                    } else {
                        // Dry run - just add to appropriate list
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            updatedItems.add(cls.getClassName());
                        } else {
                            createdItems.add(cls.getClassName());
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

            logger.info("Classroom import completed: total={}, success={}, failed={}, created={}, updated={}, mode={}", 
                       totalRows, successful, totalRows - successful, createdItems.size(), updatedItems.size(), mode);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Failed to import classrooms from Excel", ex);
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
