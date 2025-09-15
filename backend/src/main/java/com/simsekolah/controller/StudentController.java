package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateStudentRequest;
import com.simsekolah.dto.request.StudentSearchRequest;
import com.simsekolah.dto.request.UpdateStudentRequest;
import com.simsekolah.dto.response.ImportResponse;
import com.simsekolah.dto.response.StudentResponse;
import com.simsekolah.enums.Gender;
import com.simsekolah.enums.StudentStatus;
import com.simsekolah.entity.ClassRoom;
import com.simsekolah.repository.ClassRoomRepository;
import com.simsekolah.service.ExcelService;
import com.simsekolah.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for student management operations
 * Provides endpoints for student CRUD operations, search, filtering, and Excel import/export
 */
@RestController
@RequestMapping({"/api/v1/students", "/api/students"})
@Tag(name = "Student Management", description = "Student management endpoints")
@Validated
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    /**
     * Create a new student
     */
    @PostMapping
    @Operation(summary = "Create student", description = "Create a new student record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Student already exists")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        logger.info("Creating new student with NIS: {}", request.getNis());
        
        try {
            StudentResponse response = studentService.createStudent(request);
            logger.info("Successfully created student with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create student with NIS: {}", request.getNis(), e);
            throw e;
        }
    }

    /**
     * Get all students with pagination
     */
    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve all students with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "namaLengkap") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Fetching all students - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StudentResponse> students = studentService.getAllStudents(pageable);
        logger.debug("Retrieved {} students", students.getTotalElements());
        
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve student information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") @NotNull Long studentId) {
        logger.debug("Fetching student by ID: {}", studentId);
        
        Optional<StudentResponse> student = studentService.getStudentById(studentId);
        if (student.isPresent()) {
            logger.debug("Student found with ID: {}", studentId);
            return ResponseEntity.ok(student.get());
        } else {
            logger.debug("Student not found with ID: {}", studentId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get student by NIS
     */
    @GetMapping("/nis/{nis}")
    @Operation(summary = "Get student by NIS", description = "Retrieve student information by NIS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> getStudentByNis(@PathVariable("nis") String nis) {
        logger.debug("Fetching student by NIS: {}", nis);
        
        Optional<StudentResponse> student = studentService.getStudentByNis(nis);
        if (student.isPresent()) {
            logger.debug("Student found with NIS: {}", nis);
            return ResponseEntity.ok(student.get());
        } else {
            logger.debug("Student not found with NIS: {}", nis);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update student information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable("id") @NotNull Long studentId,
            @Valid @RequestBody UpdateStudentRequest request) {
        
        logger.info("Updating student with ID: {}", studentId);
        
        try {
            StudentResponse response = studentService.updateStudent(studentId, request);
            logger.info("Successfully updated student with ID: {}", studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update student with ID: {}", studentId, e);
            throw e;
        }
    }

    /**
     * Delete student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete student record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") @NotNull Long studentId) {
        logger.info("Deleting student with ID: {}", studentId);
        
        try {
            studentService.deleteStudent(studentId);
            logger.info("Successfully deleted student with ID: {}", studentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete student with ID: {}", studentId, e);
            throw e;
        }
    }

    /**
     * Search students with advanced criteria
     */
    @PostMapping("/search")
    @Operation(summary = "Search students", description = "Search students with advanced criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentResponse>> searchStudents(
            @Valid @RequestBody StudentSearchRequest searchRequest,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "namaLengkap") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Searching students with criteria: {}", searchRequest);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StudentResponse> students = studentService.searchStudents(searchRequest, pageable);
        logger.debug("Found {} students matching search criteria", students.getTotalElements());
        
        return ResponseEntity.ok(students);
    }

    /**
     * Get students by class room
     */
    @GetMapping("/class/{classRoomId}")
    @Operation(summary = "Get students by class", description = "Get all students in a specific class room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentResponse>> getStudentsByClassRoom(
            @PathVariable("classRoomId") @NotNull Long classRoomId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Fetching students by class room ID: {}", classRoomId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "namaLengkap"));
        Page<StudentResponse> students = studentService.getStudentsByClassRoom(classRoomId, pageable);
        
        logger.debug("Retrieved {} students from class room: {}", students.getTotalElements(), classRoomId);
        return ResponseEntity.ok(students);
    }

    /**
     * Get students by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get students by status", description = "Get all students with specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentResponse>> getStudentsByStatus(
            @PathVariable("status") StudentStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Fetching students by status: {}", status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "namaLengkap"));
        Page<StudentResponse> students = studentService.getStudentsByStatus(status, pageable);
        
        logger.debug("Retrieved {} students with status: {}", students.getTotalElements(), status);
        return ResponseEntity.ok(students);
    }

    /**
     * Assign student to class room
     */
    @PostMapping("/{id}/assign-class/{classRoomId}")
    @Operation(summary = "Assign to class", description = "Assign student to class room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student assigned successfully"),
        @ApiResponse(responseCode = "404", description = "Student or class room not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> assignToClassRoom(
            @PathVariable("id") @NotNull Long studentId,
            @PathVariable("classRoomId") @NotNull Long classRoomId) {
        
        logger.info("Assigning student {} to class room {}", studentId, classRoomId);
        
        try {
            studentService.assignToClassRoom(studentId, classRoomId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student assigned to class room successfully");
            response.put("studentId", studentId);
            response.put("classRoomId", classRoomId);
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("Successfully assigned student {} to class room {}", studentId, classRoomId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to assign student {} to class room {}", studentId, classRoomId, e);
            throw e;
        }
    }

    /**
     * Remove student from class room
     */
    @PostMapping("/{id}/remove-class")
    @Operation(summary = "Remove from class", description = "Remove student from class room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student removed successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> removeFromClassRoom(@PathVariable("id") @NotNull Long studentId) {
        logger.info("Removing student {} from class room", studentId);
        
        try {
            studentService.removeFromClassRoom(studentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student removed from class room successfully");
            response.put("studentId", studentId);
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("Successfully removed student {} from class room", studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to remove student {} from class room", studentId, e);
            throw e;
        }
    }

    /**
     * Get student statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get student statistics", description = "Get student count and distribution statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getStudentStatistics() {
        logger.debug("Fetching student statistics");
        
        try {
            Map<String, Object> statistics = studentService.getStudentStatistics();
            statistics.put("timestamp", System.currentTimeMillis());
            
            logger.debug("Retrieved student statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get student statistics", e);
            throw e;
        }
    }

    /**
     * Import students from Excel
     */
    @PostMapping("/excel/import")
    @Operation(summary = "Import from Excel", description = "Import students from Excel file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Import completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> importStudentsFromExcel(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(value = "dryRun", required = false, defaultValue = "false") boolean dryRun,
                                                      @RequestParam(value = "mode", required = false, defaultValue = "create") String mode) {
        logger.info("Importing students from Excel file: {} (dryRun={}, mode={})", file.getOriginalFilename(), dryRun, mode);

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new java.util.ArrayList<>();
    List<String> createdItems = new java.util.ArrayList<>();
    List<String> updatedItems = new java.util.ArrayList<>();

        if (file == null || file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "No file uploaded");
            errorResponse.put("status", "BAD_REQUEST");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Optional: validate content type and extension
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean contentOk = false;
        try {
            // allow .xlsx by default
            contentOk = (contentType != null && contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) ||
                        (originalFilename != null && originalFilename.toLowerCase().endsWith(".xlsx"));
        } catch (Exception ignore) { /* fallback below */ }
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

            // Guard against excessively large files (sane default 5000 data rows)
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

            Map<String, Integer> colIdx = mapStudentHeaderColumns(header);

            // Validate required headers: NIS, Nama Lengkap, Tahun Masuk
            if (!colIdx.containsKey("nis") || !colIdx.containsKey("namaLengkap") || !colIdx.containsKey("tahunMasuk")) {
                result.put("message", "Required headers missing. Need at least: NIS, Nama Lengkap, Tahun Masuk");
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
                int rowNum = i + 1; // human-friendly

                try {
                    String nis = getCellString(row, colIdx.get("nis"));
                    String nama = getCellString(row, colIdx.get("namaLengkap"));
                    String kelas = getCellString(row, colIdx.get("kelas"));
                    String tempatLahir = getCellString(row, colIdx.get("tempatLahir"));
                    String tanggalLahirStr = getCellString(row, colIdx.get("tanggalLahir"));
                    String jenisKelaminStr = getCellString(row, colIdx.get("jenisKelamin"));
                    String agama = getCellString(row, colIdx.get("agama"));
                    String alamat = getCellString(row, colIdx.get("alamat"));
                    String namaAyah = getCellString(row, colIdx.get("namaAyah"));
                    String namaIbu = getCellString(row, colIdx.get("namaIbu"));
                    String pekerjaanAyah = getCellString(row, colIdx.get("pekerjaanAyah"));
                    String pekerjaanIbu = getCellString(row, colIdx.get("pekerjaanIbu"));
                    String noHpOrtu = getCellString(row, colIdx.get("noHpOrtu"));
                    String alamatOrtu = getCellString(row, colIdx.get("alamatOrtu"));
                    String tahunMasukStr = getCellString(row, colIdx.get("tahunMasuk"));
                    String asalSekolah = getCellString(row, colIdx.get("asalSekolah"));
                    String statusStr = getCellString(row, colIdx.get("status"));

                    // Required field checks
                    if (nis == null || nis.isBlank()) { addError(errors, rowNum, "NIS", nis, "NIS is required"); continue; }
                    if (nama == null || nama.isBlank()) { addError(errors, rowNum, "Nama Lengkap", nama, "Nama Lengkap is required"); continue; }
                    if (tahunMasukStr == null || tahunMasukStr.isBlank()) { addError(errors, rowNum, "Tahun Masuk", tahunMasukStr, "Tahun Masuk is required"); continue; }

                    // Check for duplicates
                    boolean exists = studentService.existsByNis(nis.trim());
                    if (exists && !"upsert".equalsIgnoreCase(mode)) {
                        addError(errors, rowNum, "NIS", nis, "NIS already exists");
                        continue;
                    }

                    CreateStudentRequest req = new CreateStudentRequest();
                    req.setNis(nis.trim());
                    req.setNamaLengkap(nama.trim());
                    req.setTempatLahir(tempatLahir != null ? tempatLahir.trim() : null);
                    req.setTanggalLahir(parseDateSafe(tanggalLahirStr));
                    req.setAgama(agama != null ? agama.trim() : null);
                    req.setAlamat(alamat != null ? alamat.trim() : null);
                    req.setNamaAyah(namaAyah != null ? namaAyah.trim() : null);
                    req.setNamaIbu(namaIbu != null ? namaIbu.trim() : null);
                    req.setPekerjaanAyah(pekerjaanAyah != null ? pekerjaanAyah.trim() : null);
                    req.setPekerjaanIbu(pekerjaanIbu != null ? pekerjaanIbu.trim() : null);
                    req.setNoHpOrtu(noHpOrtu != null ? noHpOrtu.trim() : null);
                    req.setAlamatOrtu(alamatOrtu != null ? alamatOrtu.trim() : null);

                    Integer tahunMasuk = parseInteger(tahunMasukStr);
                    if (tahunMasuk == null) {
                        addError(errors, rowNum, "Tahun Masuk", tahunMasukStr, "Invalid Tahun Masuk");
                        continue;
                    }
                    req.setTahunMasuk(tahunMasuk);
                    req.setAsalSekolah(asalSekolah != null ? asalSekolah.trim() : null);

                    // Parse gender
                    if (jenisKelaminStr != null && !jenisKelaminStr.isBlank()) {
                        Gender g = parseGender(jenisKelaminStr);
                        if (g == null) {
                            addError(errors, rowNum, "Jenis Kelamin", jenisKelaminStr, "Invalid gender. Use Laki-laki/Perempuan or LAKI_LAKI/PEREMPUAN");
                            continue;
                        }
                        req.setJenisKelamin(g);
                    }

                    // Parse status
                    if (statusStr != null && !statusStr.isBlank()) {
                        StudentStatus st = parseStudentStatus(statusStr);
                        if (st == null) {
                            addError(errors, rowNum, "Status", statusStr, "Invalid status. Use ACTIVE/AKTIF, INACTIVE, GRADUATED/LULUS, DROPPED_OUT/KELUAR, TRANSFERRED/PINDAH");
                            continue;
                        }
                        req.setStatus(st);
                    } else {
                        req.setStatus(StudentStatus.ACTIVE);
                    }

                    // Map class by name if provided
                    if (kelas != null && !kelas.isBlank()) {
                        String className = kelas.trim().replaceAll("\\s+", " ");
                        java.util.Optional<ClassRoom> crOpt = classRoomRepository.findByNameIgnoreCase(className);
                        if (crOpt.isEmpty()) {
                            crOpt = classRoomRepository.findByName(className);
                        }
                        if (crOpt.isEmpty()) {
                            addError(errors, rowNum, "Kelas", kelas, "Class room not found (case-insensitive match)");
                            continue;
                        }
                        Long classRoomId = crOpt.get().getId();
                        // capacity check (also enforced when persisting, but we validate early)
                        if (!studentService.canAssignToClassRoom(classRoomId, 1)) {
                            addError(errors, rowNum, "Kelas", kelas, "Class room has reached maximum capacity");
                            continue;
                        }
                        req.setClassRoomId(classRoomId);
                    }

                    if (!dryRun) {
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            // Update existing student
                            Optional<StudentResponse> existingStudent = studentService.getStudentByNis(nis.trim());
                            if (existingStudent.isPresent()) {
                                UpdateStudentRequest updateReq = new UpdateStudentRequest();
                                updateReq.setNamaLengkap(req.getNamaLengkap());
                                updateReq.setTempatLahir(req.getTempatLahir());
                                updateReq.setTanggalLahir(req.getTanggalLahir());
                                updateReq.setJenisKelamin(req.getJenisKelamin());
                                updateReq.setAgama(req.getAgama());
                                updateReq.setAlamat(req.getAlamat());
                                updateReq.setNamaAyah(req.getNamaAyah());
                                updateReq.setNamaIbu(req.getNamaIbu());
                                updateReq.setPekerjaanAyah(req.getPekerjaanAyah());
                                updateReq.setPekerjaanIbu(req.getPekerjaanIbu());
                                updateReq.setNoHpOrtu(req.getNoHpOrtu());
                                updateReq.setAlamatOrtu(req.getAlamatOrtu());
                                updateReq.setTahunMasuk(req.getTahunMasuk());
                                updateReq.setAsalSekolah(req.getAsalSekolah());
                                updateReq.setStatus(req.getStatus());
                                updateReq.setClassRoomId(req.getClassRoomId());
                                
                                StudentResponse updated = studentService.updateStudent(existingStudent.get().getId(), updateReq);
                                updatedItems.add(updated.getNis());
                            } else {
                                // This shouldn't happen since we checked exists, but just in case
                                addError(errors, rowNum, "NIS", nis, "Student exists but could not be found for update");
                                continue;
                            }
                        } else {
                            // Create new student
                            StudentResponse created = studentService.createStudent(req);
                            createdItems.add(created.getNis());
                        }
                    } else {
                        // Dry run - just add to appropriate list
                        if (exists && "upsert".equalsIgnoreCase(mode)) {
                            updatedItems.add(req.getNis());
                        } else {
                            createdItems.add(req.getNis());
                        }
                    }
                    successful++;
                } catch (Exception ex) {
                    addError(errors, rowNum, "General", "", ex.getMessage());
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

            logger.info("Student import completed: total={}, success={}, failed={}, created={}, updated={}, mode={}", 
                       totalRows, successful, totalRows - successful, createdItems.size(), updatedItems.size(), mode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to import students from Excel file: {}", file.getOriginalFilename(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to process Excel file: " + e.getMessage());
            errorResponse.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Map header names to column indices (case-insensitive with common aliases)
    private Map<String, Integer> mapStudentHeaderColumns(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            Cell cell = headerRow.getCell(c);
            if (cell == null) continue;
            String raw = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell);
            if (raw == null) continue;
            String key = raw.trim().toLowerCase();
            switch (key) {
                case "nis": map.put("nis", c); break;
                case "nama lengkap":
                case "nama": map.put("namaLengkap", c); break;
                case "kelas": map.put("kelas", c); break;
                case "tempat lahir": map.put("tempatLahir", c); break;
                case "tanggal lahir": map.put("tanggalLahir", c); break;
                case "jenis kelamin": map.put("jenisKelamin", c); break;
                case "agama": map.put("agama", c); break;
                case "alamat": map.put("alamat", c); break;
                case "nama ayah": map.put("namaAyah", c); break;
                case "nama ibu": map.put("namaIbu", c); break;
                case "pekerjaan ayah": map.put("pekerjaanAyah", c); break;
                case "pekerjaan ibu": map.put("pekerjaanIbu", c); break;
                case "no hp orang tua":
                case "no hp ortu": map.put("noHpOrtu", c); break;
                case "alamat orang tua":
                case "alamat ortu": map.put("alamatOrtu", c); break;
                case "tahun masuk": map.put("tahunMasuk", c); break;
                case "asal sekolah": map.put("asalSekolah", c); break;
                case "status": map.put("status", c); break;
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
                // Try to get as date first
                try {
                    java.time.LocalDate d = cell.getLocalDateTimeCellValue().toLocalDate();
                    yield d.toString();
                } catch (Exception e) {
                    double d = cell.getNumericCellValue();
                    if (Math.floor(d) == d) {
                        yield String.valueOf((long) d);
                    }
                    yield String.valueOf(d);
                }
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

    private java.time.LocalDate parseDateSafe(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String v = raw.trim();
        try {
            return java.time.LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            try {
                return java.time.LocalDate.parse(v);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private Integer parseInteger(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try { return Integer.parseInt(raw.trim()); } catch (Exception e) { return null; }
    }

    private Gender parseGender(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toLowerCase();
        if (v.equals("l") || v.equals("laki-laki") || v.equals("laki laki") || v.equals("male")) return Gender.LAKI_LAKI;
        if (v.equals("p") || v.equals("perempuan") || v.equals("female")) return Gender.PEREMPUAN;
        try {
            return Gender.valueOf(raw.trim().toUpperCase());
        } catch (Exception ignored) {
            return null;
        }
    }

    private StudentStatus parseStudentStatus(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toUpperCase();
        if (v.equals("AKTIF")) return StudentStatus.ACTIVE;
        if (v.equals("TIDAK AKTIF") || v.equals("NONAKTIF") || v.equals("NON-AKTIF")) return StudentStatus.INACTIVE;
        if (v.equals("LULUS")) return StudentStatus.GRADUATED;
        if (v.equals("KELUAR")) return StudentStatus.DROPPED_OUT;
        if (v.equals("PINDAH")) return StudentStatus.TRANSFERRED;
        try {
            return StudentStatus.valueOf(v);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Export students to Excel
     */
    @PostMapping("/excel/export")
    @Operation(summary = "Export to Excel", description = "Export students to Excel file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Export completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportStudentsToExcel(@RequestBody(required = false) StudentSearchRequest searchRequest) {
        logger.info("Exporting students to Excel");
        
        try {
            // Get students based on search criteria or all students
            List<StudentResponse> students;
            if (searchRequest != null) {
                Page<StudentResponse> studentPage = studentService.searchStudents(searchRequest, Pageable.unpaged());
                students = studentPage.getContent();
            } else {
                Page<StudentResponse> studentPage = studentService.getAllStudents(Pageable.unpaged());
                students = studentPage.getContent();
            }
            
            ByteArrayOutputStream outputStream = excelService.exportStudentsToExcel(students);
            byte[] excelData = outputStream.toByteArray();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "students_export.xlsx");
            headers.setContentLength(excelData.length);
            
            logger.info("Successfully exported students to Excel");
            return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export students to Excel", e);
            throw e;
        }
    }

    /**
     * Download Excel template
     */
    @GetMapping("/excel/template")
    @Operation(summary = "Download template", description = "Download Excel template for student import")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Template downloaded successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> downloadExcelTemplate() {
        logger.info("Downloading Excel template for student import");
        
        try {
            ByteArrayOutputStream outputStream = excelService.generateStudentImportTemplate();
            byte[] templateData = outputStream.toByteArray();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "student_import_template.xlsx");
            headers.setContentLength(templateData.length);
            
            logger.info("Successfully generated Excel template");
            return ResponseEntity.ok()
                .headers(headers)
                .body(templateData);
        } catch (Exception e) {
            logger.error("Failed to generate Excel template", e);
            throw e;
        }
    }

    /**
     * Bulk assign students to class room
     */
    @PostMapping("/bulk/assign-class/{classRoomId}")
    @Operation(summary = "Bulk assign to class", description = "Assign multiple students to class room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> bulkAssignToClassRoom(
            @PathVariable("classRoomId") @NotNull Long classRoomId,
            @RequestBody List<Long> studentIds) {
        
        logger.info("Bulk assigning {} students to class room {}", studentIds.size(), classRoomId);
        
        try {
            studentService.bulkAssignToClassRoom(studentIds, classRoomId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Students assigned to class room successfully");
            response.put("count", studentIds.size());
            response.put("classRoomId", classRoomId);
            response.put("studentIds", studentIds);
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("Successfully bulk assigned {} students to class room {}", studentIds.size(), classRoomId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to bulk assign students to class room {}", classRoomId, e);
            throw e;
        }
    }
}
