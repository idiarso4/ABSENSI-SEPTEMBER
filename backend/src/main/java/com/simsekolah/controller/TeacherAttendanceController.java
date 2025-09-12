package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateTeacherAttendanceRequest;
import com.simsekolah.dto.response.TeacherAttendanceResponse;
import com.simsekolah.service.TeacherAttendanceService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for teacher attendance management operations
 * Provides endpoints for absensi-guru (teacher attendance)
 */
@RestController
@RequestMapping({"/api/v1/lms/absensi-guru", "/api/lms/absensi-guru"})
@Tag(name = "Teacher Attendance Management", description = "Teacher attendance management endpoints")
@Validated
public class TeacherAttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherAttendanceController.class);

    @Autowired
    private TeacherAttendanceService attendanceService;

    /**
     * Create a new teacher attendance record
     */
    @PostMapping
    @Operation(summary = "Create teacher attendance", description = "Create a new teacher attendance record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Attendance created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherAttendanceResponse> createAttendance(@Valid @RequestBody CreateTeacherAttendanceRequest request) {
        logger.info("Creating teacher attendance for teacher: {}", request.getTeacherId());

        try {
            TeacherAttendanceResponse response = attendanceService.createAttendance(request);
            logger.info("Successfully created teacher attendance with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create teacher attendance for teacher: {}", request.getTeacherId(), e);
            throw e;
        }
    }

    /**
     * Get all teacher attendances with pagination
     */
    @GetMapping
    @Operation(summary = "Get all teacher attendances", description = "Retrieve all teacher attendances with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TeacherAttendanceResponse>> getAllAttendances(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all teacher attendances - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<TeacherAttendanceResponse> attendances = attendanceService.getAllAttendances(pageable);
        logger.debug("Retrieved {} teacher attendances", attendances.getTotalElements());

        return ResponseEntity.ok(attendances);
    }

    /**
     * Get teacher attendance by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get teacher attendance by ID", description = "Retrieve teacher attendance information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance found"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherAttendanceResponse> getAttendanceById(@PathVariable("id") @NotNull Long attendanceId) {
        logger.debug("Fetching teacher attendance by ID: {}", attendanceId);

        Optional<TeacherAttendanceResponse> attendance = attendanceService.getAttendanceById(attendanceId);
        if (attendance.isPresent()) {
            logger.debug("Teacher attendance found with ID: {}", attendanceId);
            return ResponseEntity.ok(attendance.get());
        } else {
            logger.debug("Teacher attendance not found with ID: {}", attendanceId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get attendances by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get attendances by teacher", description = "Get all attendances for a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TeacherAttendanceResponse>> getAttendancesByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching teacher attendances by teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<TeacherAttendanceResponse> attendances = attendanceService.getAttendancesByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} teacher attendances for teacher: {}", attendances.getTotalElements(), teacherId);
        return ResponseEntity.ok(attendances);
    }

    /**
     * Get attendances by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get attendances by date range", description = "Get all attendances within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TeacherAttendanceResponse>> getAttendancesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching teacher attendances by date range: {} to {}", startDate, endDate);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<TeacherAttendanceResponse> attendances = attendanceService.getAttendancesByDateRange(startDate, endDate, pageable);

        logger.debug("Retrieved {} teacher attendances for date range", attendances.getTotalElements());
        return ResponseEntity.ok(attendances);
    }

    /**
     * Update attendance status
     */
    @PostMapping("/{id}/status/{status}")
    @Operation(summary = "Update attendance status", description = "Update the status of a teacher attendance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherAttendanceResponse> updateAttendanceStatus(
            @PathVariable("id") @NotNull Long attendanceId,
            @PathVariable("status") String status) {

        logger.info("Updating teacher attendance {} status to: {}", attendanceId, status);

        try {
            TeacherAttendanceResponse response = attendanceService.updateAttendanceStatus(attendanceId, status);
            logger.info("Successfully updated teacher attendance {} status to: {}", attendanceId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update teacher attendance {} status to: {}", attendanceId, status, e);
            throw e;
        }
    }

    /**
     * Verify attendance
     */
    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify attendance", description = "Verify teacher attendance record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance verified successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TeacherAttendanceResponse> verifyAttendance(@PathVariable("id") @NotNull Long attendanceId) {
        logger.info("Verifying teacher attendance: {}", attendanceId);

        try {
            TeacherAttendanceResponse response = attendanceService.verifyAttendance(attendanceId);
            logger.info("Successfully verified teacher attendance: {}", attendanceId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to verify teacher attendance: {}", attendanceId, e);
            throw e;
        }
    }

    /**
     * Get unverified attendances
     */
    @GetMapping("/unverified")
    @Operation(summary = "Get unverified attendances", description = "Get all unverified teacher attendances")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unverified attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TeacherAttendanceResponse>> getUnverifiedAttendances() {
        logger.debug("Fetching unverified teacher attendances");

        List<TeacherAttendanceResponse> attendances = attendanceService.getUnverifiedAttendances();
        logger.debug("Retrieved {} unverified teacher attendances", attendances.size());

        return ResponseEntity.ok(attendances);
    }

    /**
     * Get attendance statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get attendance statistics", description = "Get teacher attendance statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceStatistics() {
        logger.debug("Fetching teacher attendance statistics");

        try {
            Map<String, Object> statistics = attendanceService.getAttendanceStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved teacher attendance statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get teacher attendance statistics", e);
            throw e;
        }
    }

    /**
     * Get teacher attendance summary
     */
    @GetMapping("/teacher/{teacherId}/summary")
    @Operation(summary = "Get teacher attendance summary", description = "Get attendance summary for a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getTeacherAttendanceSummary(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        logger.debug("Fetching teacher attendance summary for teacher: {}", teacherId);

        try {
            Map<String, Object> summary = attendanceService.getTeacherAttendanceSummary(teacherId, startDate, endDate);
            summary.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved teacher attendance summary for teacher: {}", teacherId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Failed to get teacher attendance summary for teacher: {}", teacherId, e);
            throw e;
        }
    }

    /**
     * Advanced search for attendances
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for teacher attendances with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TeacherAttendanceResponse>> advancedSearch(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String attendanceStatus,
            @RequestParam(required = false) String verificationMethod,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for teacher attendances with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<TeacherAttendanceResponse> attendances = attendanceService.advancedSearch(
                teacherId, attendanceStatus, verificationMethod, startDate, endDate, pageable);

        logger.debug("Found {} teacher attendances matching search criteria", attendances.getTotalElements());
        return ResponseEntity.ok(attendances);
    }
}