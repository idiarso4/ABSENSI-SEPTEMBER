package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateDutyTeacherReportRequest;
import com.simsekolah.dto.response.DutyTeacherReportResponse;
import com.simsekolah.service.DutyTeacherReportService;
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
 * REST controller for duty teacher report management operations
 * Provides endpoints for laporan-guru-piket (duty teacher reports)
 */
@RestController
@RequestMapping({"/api/v1/perizinan/laporan-guru-piket", "/api/perizinan/laporan-guru-piket"})
@Tag(name = "Duty Teacher Report Management", description = "Duty teacher report management endpoints")
@Validated
public class DutyTeacherReportController {

    private static final Logger logger = LoggerFactory.getLogger(DutyTeacherReportController.class);

    @Autowired
    private DutyTeacherReportService reportService;

    /**
     * Create a new duty teacher report
     */
    @PostMapping
    @Operation(summary = "Create duty teacher report", description = "Create a new duty teacher report")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Report created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<DutyTeacherReportResponse> createReport(@Valid @RequestBody CreateDutyTeacherReportRequest request) {
        logger.info("Creating duty teacher report for teacher: {}", request.getDutyTeacherId());

        try {
            DutyTeacherReportResponse response = reportService.createReport(request);
            logger.info("Successfully created duty teacher report with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create duty teacher report for teacher: {}", request.getDutyTeacherId(), e);
            throw e;
        }
    }

    /**
     * Get all duty teacher reports with pagination
     */
    @GetMapping
    @Operation(summary = "Get all duty teacher reports", description = "Retrieve all duty teacher reports with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<DutyTeacherReportResponse>> getAllReports(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "reportDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all duty teacher reports - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<DutyTeacherReportResponse> reports = reportService.getAllReports(pageable);
        logger.debug("Retrieved {} duty teacher reports", reports.getTotalElements());

        return ResponseEntity.ok(reports);
    }

    /**
     * Get duty teacher report by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get duty teacher report by ID", description = "Retrieve duty teacher report information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report found"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<DutyTeacherReportResponse> getReportById(@PathVariable("id") @NotNull Long reportId) {
        logger.debug("Fetching duty teacher report by ID: {}", reportId);

        Optional<DutyTeacherReportResponse> report = reportService.getReportById(reportId);
        if (report.isPresent()) {
            logger.debug("Duty teacher report found with ID: {}", reportId);
            return ResponseEntity.ok(report.get());
        } else {
            logger.debug("Duty teacher report not found with ID: {}", reportId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get reports by duty teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get reports by teacher", description = "Get all reports for a specific duty teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<DutyTeacherReportResponse>> getReportsByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching duty teacher reports by teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportDate"));
        Page<DutyTeacherReportResponse> reports = reportService.getReportsByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} duty teacher reports for teacher: {}", reports.getTotalElements(), teacherId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Submit report for approval
     */
    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit report", description = "Submit duty teacher report for admin approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report submitted successfully"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<DutyTeacherReportResponse> submitReport(@PathVariable("id") @NotNull Long reportId) {
        logger.info("Submitting duty teacher report: {}", reportId);

        try {
            DutyTeacherReportResponse response = reportService.submitReport(reportId);
            logger.info("Successfully submitted duty teacher report: {}", reportId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to submit duty teacher report: {}", reportId, e);
            throw e;
        }
    }

    /**
     * Approve report by admin
     */
    @PostMapping("/{id}/approve/{adminId}")
    @Operation(summary = "Approve report", description = "Approve duty teacher report by admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report approved successfully"),
        @ApiResponse(responseCode = "404", description = "Report or admin not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<DutyTeacherReportResponse> approveReport(
            @PathVariable("id") @NotNull Long reportId,
            @PathVariable("adminId") @NotNull Long adminId,
            @RequestParam(required = false) String approvalNotes) {

        logger.info("Approving duty teacher report {} by admin {}", reportId, adminId);

        try {
            DutyTeacherReportResponse response = reportService.approveReport(reportId, adminId, approvalNotes);
            logger.info("Successfully approved duty teacher report {} by admin {}", reportId, adminId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to approve duty teacher report {} by admin {}", reportId, adminId, e);
            throw e;
        }
    }

    /**
     * Get pending reports for admin approval
     */
    @GetMapping("/pending-approval")
    @Operation(summary = "Get pending approval reports", description = "Get all reports pending admin approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending reports retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<DutyTeacherReportResponse>> getPendingApprovalReports() {
        logger.debug("Fetching pending approval duty teacher reports");

        List<DutyTeacherReportResponse> reports = reportService.getPendingApprovalReports();
        logger.debug("Retrieved {} pending approval duty teacher reports", reports.size());

        return ResponseEntity.ok(reports);
    }

    /**
     * Get report statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get report statistics", description = "Get duty teacher report statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getReportStatistics() {
        logger.debug("Fetching duty teacher report statistics");

        try {
            Map<String, Object> statistics = reportService.getReportStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved duty teacher report statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get duty teacher report statistics", e);
            throw e;
        }
    }

    /**
     * Get today's duty teacher report
     */
    @GetMapping("/today/{teacherId}")
    @Operation(summary = "Get today's report", description = "Get today's duty teacher report for a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<DutyTeacherReportResponse> getTodaysReport(@PathVariable("teacherId") @NotNull Long teacherId) {
        logger.debug("Fetching today's duty teacher report for teacher: {}", teacherId);

        try {
            Optional<DutyTeacherReportResponse> report = reportService.getTodaysReport(teacherId);
            if (report.isPresent()) {
                logger.debug("Found today's duty teacher report for teacher: {}", teacherId);
                return ResponseEntity.ok(report.get());
            } else {
                logger.debug("No duty teacher report found for today for teacher: {}", teacherId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Failed to get today's duty teacher report for teacher: {}", teacherId, e);
            throw e;
        }
    }
}