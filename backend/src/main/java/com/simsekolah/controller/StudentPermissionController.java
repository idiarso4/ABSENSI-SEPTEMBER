package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateStudentPermissionRequest;
import com.simsekolah.dto.response.StudentPermissionResponse;
import com.simsekolah.service.StudentPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for student permission management operations
 * Provides endpoints for izin-masuk (entry late) and izin-keluar (exit early)
 */
@RestController
@RequestMapping({"/api/v1/perizinan", "/api/perizinan"})
@Tag(name = "Student Permission Management", description = "Student permission management endpoints")
@Validated
public class StudentPermissionController {

    private static final Logger logger = LoggerFactory.getLogger(StudentPermissionController.class);

    @Autowired
    private StudentPermissionService permissionService;

    /**
     * Create a new student permission (izin-masuk or izin-keluar)
     */
    @PostMapping
    @Operation(summary = "Create student permission", description = "Create a new student permission request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Permission created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<StudentPermissionResponse> createPermission(@Valid @RequestBody CreateStudentPermissionRequest request) {
        logger.info("Creating student permission for student: {}", request.getStudentId());

        try {
            StudentPermissionResponse response = permissionService.createPermission(request);
            logger.info("Successfully created student permission with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create student permission for student: {}", request.getStudentId(), e);
            throw e;
        }
    }

    /**
     * Get all student permissions with pagination
     */
    @GetMapping
    @Operation(summary = "Get all student permissions", description = "Retrieve all student permissions with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentPermissionResponse>> getAllPermissions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "permissionDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all student permissions - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<StudentPermissionResponse> permissions = permissionService.getAllPermissions(pageable);
        logger.debug("Retrieved {} student permissions", permissions.getTotalElements());

        return ResponseEntity.ok(permissions);
    }

    /**
     * Get student permission by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get student permission by ID", description = "Retrieve student permission information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission found"),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<StudentPermissionResponse> getPermissionById(@PathVariable("id") @NotNull Long permissionId) {
        logger.debug("Fetching student permission by ID: {}", permissionId);

        Optional<StudentPermissionResponse> permission = permissionService.getPermissionById(permissionId);
        if (permission.isPresent()) {
            logger.debug("Student permission found with ID: {}", permissionId);
            return ResponseEntity.ok(permission.get());
        } else {
            logger.debug("Student permission not found with ID: {}", permissionId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get permissions by student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get permissions by student", description = "Get all permissions for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<StudentPermissionResponse>> getPermissionsByStudent(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching student permissions by student ID: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "permissionDate"));
        Page<StudentPermissionResponse> permissions = permissionService.getPermissionsByStudent(studentId, pageable);

        logger.debug("Retrieved {} student permissions for student: {}", permissions.getTotalElements(), studentId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * Approve permission by duty teacher
     */
    @PostMapping("/{id}/approve/{teacherId}")
    @Operation(summary = "Approve permission", description = "Approve student permission by duty teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission approved successfully"),
        @ApiResponse(responseCode = "404", description = "Permission or teacher not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentPermissionResponse> approvePermission(
            @PathVariable("id") @NotNull Long permissionId,
            @PathVariable("teacherId") @NotNull Long teacherId,
            @RequestParam(required = false) String approvalNotes) {

        logger.info("Approving student permission {} by teacher {}", permissionId, teacherId);

        try {
            StudentPermissionResponse response = permissionService.approvePermission(permissionId, teacherId, approvalNotes);
            logger.info("Successfully approved student permission {} by teacher {}", permissionId, teacherId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to approve student permission {} by teacher {}", permissionId, teacherId, e);
            throw e;
        }
    }

    /**
     * Reject permission by duty teacher
     */
    @PostMapping("/{id}/reject/{teacherId}")
    @Operation(summary = "Reject permission", description = "Reject student permission by duty teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Permission or teacher not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentPermissionResponse> rejectPermission(
            @PathVariable("id") @NotNull Long permissionId,
            @PathVariable("teacherId") @NotNull Long teacherId,
            @RequestParam String rejectionReason) {

        logger.info("Rejecting student permission {} by teacher {}", permissionId, teacherId);

        try {
            StudentPermissionResponse response = permissionService.rejectPermission(permissionId, teacherId, rejectionReason);
            logger.info("Successfully rejected student permission {} by teacher {}", permissionId, teacherId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to reject student permission {} by teacher {}", permissionId, teacherId, e);
            throw e;
        }
    }

    /**
     * Get pending permissions for duty teacher approval
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending permissions", description = "Get all pending permissions awaiting duty teacher approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending permissions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<StudentPermissionResponse>> getPendingPermissions() {
        logger.debug("Fetching pending student permissions");

        List<StudentPermissionResponse> permissions = permissionService.getPendingPermissions();
        logger.debug("Retrieved {} pending student permissions", permissions.size());

        return ResponseEntity.ok(permissions);
    }

    /**
     * Get permission statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get permission statistics", description = "Get student permission statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getPermissionStatistics() {
        logger.debug("Fetching student permission statistics");

        try {
            Map<String, Object> statistics = permissionService.getPermissionStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved student permission statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get student permission statistics", e);
            throw e;
        }
    }

    /**
     * Get izin-masuk (entry late) permissions
     */
    @GetMapping("/izin-masuk")
    @Operation(summary = "Get izin-masuk permissions", description = "Get all izin-masuk (entry late) permissions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Izin-masuk permissions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentPermissionResponse>> getIzinMasukPermissions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching izin-masuk permissions");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "permissionDate"));
        Page<StudentPermissionResponse> permissions = permissionService.getPermissionsByStatus("ENTRY_LATE", pageable);

        logger.debug("Retrieved {} izin-masuk permissions", permissions.getTotalElements());
        return ResponseEntity.ok(permissions);
    }

    /**
     * Get izin-keluar (exit early) permissions
     */
    @GetMapping("/izin-keluar")
    @Operation(summary = "Get izin-keluar permissions", description = "Get all izin-keluar (exit early) permissions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Izin-keluar permissions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<StudentPermissionResponse>> getIzinKeluarPermissions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching izin-keluar permissions");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "permissionDate"));
        Page<StudentPermissionResponse> permissions = permissionService.getPermissionsByStatus("EXIT_EARLY", pageable);

        logger.debug("Retrieved {} izin-keluar permissions", permissions.getTotalElements());
        return ResponseEntity.ok(permissions);
    }
}