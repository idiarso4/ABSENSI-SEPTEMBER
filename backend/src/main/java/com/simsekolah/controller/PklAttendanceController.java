package com.simsekolah.controller;

import com.simsekolah.dto.request.CreatePklAttendanceRequest;
import com.simsekolah.dto.request.UpdatePklAttendanceRequest;
import com.simsekolah.dto.response.PklAttendanceResponse;
import com.simsekolah.service.PklAttendanceService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for PKL attendance management operations
 * Provides endpoints for PKL attendance CRUD operations, search, filtering, and verification
 */
@RestController
@RequestMapping({"/api/v1/pkl/attendance", "/api/pkl/attendance"})
@Tag(name = "PKL Attendance Management", description = "PKL attendance management endpoints")
@Validated
public class PklAttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(PklAttendanceController.class);

    @Autowired
    private PklAttendanceService pklAttendanceService;

    /**
     * Create a new PKL attendance record
     */
    @PostMapping
    @Operation(summary = "Create PKL attendance", description = "Create a new PKL attendance record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Attendance created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Attendance already exists for student on date")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<PklAttendanceResponse> createAttendance(@Valid @RequestBody CreatePklAttendanceRequest request) {
        logger.info("Creating PKL attendance for student: {}", request.getStudentId());

        try {
            PklAttendanceResponse response = pklAttendanceService.createAttendance(request);
            logger.info("Successfully created PKL attendance with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create PKL attendance for student: {}", request.getStudentId(), e);
            throw e;
        }
    }

    /**
     * Get all PKL attendances with pagination
     */
    @GetMapping
    @Operation(summary = "Get all PKL attendances", description = "Retrieve all PKL attendances with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> getAllAttendances(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all PKL attendances - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<PklAttendanceResponse> attendances = pklAttendanceService.getAllAttendances(pageable);
        logger.debug("Retrieved {} PKL attendances", attendances.getTotalElements());

        return ResponseEntity.ok(attendances);
    }

    /**
     * Get PKL attendance by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get PKL attendance by ID", description = "Retrieve PKL attendance information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance found"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<PklAttendanceResponse> getAttendanceById(@PathVariable("id") @NotNull Long attendanceId) {
        logger.debug("Fetching PKL attendance by ID: {}", attendanceId);

        Optional<PklAttendanceResponse> attendance = pklAttendanceService.getAttendanceById(attendanceId);
        if (attendance.isPresent()) {
            logger.debug("PKL attendance found with ID: {}", attendanceId);
            return ResponseEntity.ok(attendance.get());
        } else {
            logger.debug("PKL attendance not found with ID: {}", attendanceId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update PKL attendance
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update PKL attendance", description = "Update PKL attendance information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance updated successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<PklAttendanceResponse> updateAttendance(
            @PathVariable("id") @NotNull Long attendanceId,
            @Valid @RequestBody UpdatePklAttendanceRequest request) {

        logger.info("Updating PKL attendance with ID: {}", attendanceId);

        try {
            PklAttendanceResponse response = pklAttendanceService.updateAttendance(attendanceId, request);
            logger.info("Successfully updated PKL attendance with ID: {}", attendanceId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update PKL attendance with ID: {}", attendanceId, e);
            throw e;
        }
    }

    /**
     * Delete PKL attendance
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete PKL attendance", description = "Delete PKL attendance record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Attendance deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable("id") @NotNull Long attendanceId) {
        logger.info("Deleting PKL attendance with ID: {}", attendanceId);

        try {
            pklAttendanceService.deleteAttendance(attendanceId);
            logger.info("Successfully deleted PKL attendance with ID: {}", attendanceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete PKL attendance with ID: {}", attendanceId, e);
            throw e;
        }
    }

    /**
     * Get PKL attendances by student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get attendances by student", description = "Get all PKL attendances for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> getAttendancesByStudent(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching PKL attendances for student ID: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<PklAttendanceResponse> attendances = pklAttendanceService.getAttendancesByStudent(studentId, pageable);

        logger.debug("Retrieved {} PKL attendances for student: {}", attendances.getTotalElements(), studentId);
        return ResponseEntity.ok(attendances);
    }

    /**
     * Get PKL attendances by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get attendances by teacher", description = "Get all PKL attendances supervised by a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> getAttendancesByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching PKL attendances for teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<PklAttendanceResponse> attendances = pklAttendanceService.getAttendancesByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} PKL attendances for teacher: {}", attendances.getTotalElements(), teacherId);
        return ResponseEntity.ok(attendances);
    }

    /**
     * Get PKL attendances by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get attendances by date range", description = "Get PKL attendances within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> getAttendancesByDateRange(
            @Parameter(description = "Start date") @RequestParam LocalDate startDate,
            @Parameter(description = "End date") @RequestParam LocalDate endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching PKL attendances between {} and {}", startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<PklAttendanceResponse> attendances = pklAttendanceService.getAttendancesByDateRange(startDate, endDate, pageable);

        logger.debug("Retrieved {} PKL attendances between {} and {}", attendances.getTotalElements(), startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    /**
     * Get PKL attendances by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get attendances by status", description = "Get PKL attendances with specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> getAttendancesByStatus(
            @PathVariable("status") String status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching PKL attendances by status: {}", status);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<PklAttendanceResponse> attendances = pklAttendanceService.getAttendancesByStatus(status, pageable);

        logger.debug("Retrieved {} PKL attendances with status: {}", attendances.getTotalElements(), status);
        return ResponseEntity.ok(attendances);
    }

    /**
     * Verify PKL attendance
     */
    @PostMapping("/{id}/verify/{teacherId}")
    @Operation(summary = "Verify attendance", description = "Verify PKL attendance by supervising teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance verified successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance or teacher not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<PklAttendanceResponse> verifyAttendance(
            @PathVariable("id") @NotNull Long attendanceId,
            @PathVariable("teacherId") @NotNull Long teacherId) {

        logger.info("Verifying PKL attendance {} by teacher {}", attendanceId, teacherId);

        try {
            PklAttendanceResponse response = pklAttendanceService.verifyAttendance(attendanceId, teacherId);
            logger.info("Successfully verified PKL attendance {} by teacher {}", attendanceId, teacherId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to verify PKL attendance {} by teacher {}", attendanceId, teacherId, e);
            throw e;
        }
    }

    /**
     * Get unverified attendances
     */
    @GetMapping("/unverified")
    @Operation(summary = "Get unverified attendances", description = "Get all unverified PKL attendances")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unverified attendances retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<PklAttendanceResponse>> getUnverifiedAttendances() {
        logger.debug("Fetching unverified PKL attendances");

        List<PklAttendanceResponse> attendances = pklAttendanceService.getUnverifiedAttendances();
        logger.debug("Retrieved {} unverified PKL attendances", attendances.size());

        return ResponseEntity.ok(attendances);
    }

    /**
     * Get attendance statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get attendance statistics", description = "Get PKL attendance statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceStatistics() {
        logger.debug("Fetching PKL attendance statistics");

        try {
            Map<String, Object> statistics = pklAttendanceService.getAttendanceStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved PKL attendance statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get PKL attendance statistics", e);
            throw e;
        }
    }

    /**
     * Get student attendance summary
     */
    @GetMapping("/student/{studentId}/summary")
    @Operation(summary = "Get student attendance summary", description = "Get attendance summary for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getStudentAttendanceSummary(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Start date") @RequestParam LocalDate startDate,
            @Parameter(description = "End date") @RequestParam LocalDate endDate) {

        logger.debug("Fetching attendance summary for student {} between {} and {}", studentId, startDate, endDate);

        try {
            Map<String, Object> summary = pklAttendanceService.getStudentAttendanceSummary(studentId, startDate, endDate);
            summary.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance summary for student: {}", studentId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Failed to get attendance summary for student: {}", studentId, e);
            throw e;
        }
    }

    /**
     * Check if attendance exists for student on date
     */
    @GetMapping("/exists/student/{studentId}/date/{date}")
    @Operation(summary = "Check attendance existence", description = "Check if attendance exists for student on specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> checkAttendanceExists(
            @PathVariable("studentId") @NotNull Long studentId,
            @PathVariable("date") LocalDate date) {

        logger.debug("Checking if attendance exists for student {} on date {}", studentId, date);

        boolean exists = pklAttendanceService.existsByStudentAndDate(studentId, date);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("studentId", studentId);
        response.put("date", date);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    /**
     * Bulk create attendances
     */
    @PostMapping("/bulk")
    @Operation(summary = "Bulk create attendances", description = "Create multiple PKL attendance records at once")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bulk creation completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<PklAttendanceResponse>> bulkCreateAttendances(@Valid @RequestBody List<CreatePklAttendanceRequest> requests) {
        logger.info("Bulk creating {} PKL attendances", requests.size());

        try {
            List<PklAttendanceResponse> responses = pklAttendanceService.bulkCreateAttendances(requests);

            logger.info("Successfully bulk created {} PKL attendances", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Failed to bulk create PKL attendances", e);
            throw e;
        }
    }

    /**
     * Advanced search for attendances
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for PKL attendances with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<PklAttendanceResponse>> advancedSearch(
            @Parameter(description = "Student ID") @RequestParam(required = false) Long studentId,
            @Parameter(description = "Teacher ID") @RequestParam(required = false) Long teacherId,
            @Parameter(description = "Start date") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Company name") @RequestParam(required = false) String companyName,
            @Parameter(description = "Status") @RequestParam(required = false) String status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for PKL attendances with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendanceDate"));
        Page<PklAttendanceResponse> attendances = pklAttendanceService.advancedSearch(
                studentId, teacherId, startDate, endDate, companyName, status, pageable);

        logger.debug("Found {} PKL attendances matching search criteria", attendances.getTotalElements());
        return ResponseEntity.ok(attendances);
    }
}