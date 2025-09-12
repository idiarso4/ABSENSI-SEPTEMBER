package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateCounselingCaseRequest;
import com.simsekolah.dto.response.CounselingCaseResponse;
import com.simsekolah.service.CounselingCaseService;
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
 * REST controller for counseling case management operations
 * Provides endpoints for penanganan-siswa (student handling/counseling cases)
 */
@RestController
@RequestMapping({"/api/v1/bk/penanganan-siswa", "/api/bk/penanganan-siswa"})
@Tag(name = "Counseling Case Management", description = "Counseling case management endpoints")
@Validated
public class CounselingCaseController {

    private static final Logger logger = LoggerFactory.getLogger(CounselingCaseController.class);

    @Autowired
    private CounselingCaseService caseService;

    /**
     * Create a new counseling case
     */
    @PostMapping
    @Operation(summary = "Create counseling case", description = "Create a new counseling case for student handling")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Case created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingCaseResponse> createCase(@Valid @RequestBody CreateCounselingCaseRequest request) {
        logger.info("Creating counseling case for student: {}", request.getStudentId());

        try {
            CounselingCaseResponse response = caseService.createCase(request);
            logger.info("Successfully created counseling case with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create counseling case for student: {}", request.getStudentId(), e);
            throw e;
        }
    }

    /**
     * Get all counseling cases with pagination
     */
    @GetMapping
    @Operation(summary = "Get all counseling cases", description = "Retrieve all counseling cases with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cases retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingCaseResponse>> getAllCases(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "reportedDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all counseling cases - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CounselingCaseResponse> cases = caseService.getAllCases(pageable);
        logger.debug("Retrieved {} counseling cases", cases.getTotalElements());

        return ResponseEntity.ok(cases);
    }

    /**
     * Get counseling case by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get counseling case by ID", description = "Retrieve counseling case information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case found"),
        @ApiResponse(responseCode = "404", description = "Case not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingCaseResponse> getCaseById(@PathVariable("id") @NotNull Long caseId) {
        logger.debug("Fetching counseling case by ID: {}", caseId);

        Optional<CounselingCaseResponse> caseResponse = caseService.getCaseById(caseId);
        if (caseResponse.isPresent()) {
            logger.debug("Counseling case found with ID: {}", caseId);
            return ResponseEntity.ok(caseResponse.get());
        } else {
            logger.debug("Counseling case not found with ID: {}", caseId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get cases by student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get cases by student", description = "Get all counseling cases for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cases retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingCaseResponse>> getCasesByStudent(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling cases by student ID: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedDate"));
        Page<CounselingCaseResponse> cases = caseService.getCasesByStudent(studentId, pageable);

        logger.debug("Retrieved {} counseling cases for student: {}", cases.getTotalElements(), studentId);
        return ResponseEntity.ok(cases);
    }

    /**
     * Get cases by counselor
     */
    @GetMapping("/counselor/{counselorId}")
    @Operation(summary = "Get cases by counselor", description = "Get all counseling cases handled by a specific counselor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cases retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingCaseResponse>> getCasesByCounselor(
            @PathVariable("counselorId") @NotNull Long counselorId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling cases by counselor ID: {}", counselorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedDate"));
        Page<CounselingCaseResponse> cases = caseService.getCasesByCounselor(counselorId, pageable);

        logger.debug("Retrieved {} counseling cases for counselor: {}", cases.getTotalElements(), counselorId);
        return ResponseEntity.ok(cases);
    }

    /**
     * Update case status
     */
    @PostMapping("/{id}/status/{status}")
    @Operation(summary = "Update case status", description = "Update the status of a counseling case")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Case status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Case not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingCaseResponse> updateCaseStatus(
            @PathVariable("id") @NotNull Long caseId,
            @PathVariable("status") String status) {

        logger.info("Updating counseling case {} status to: {}", caseId, status);

        try {
            CounselingCaseResponse response = caseService.updateCaseStatus(caseId, status);
            logger.info("Successfully updated counseling case {} status to: {}", caseId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update counseling case {} status to: {}", caseId, status, e);
            throw e;
        }
    }

    /**
     * Get active cases
     */
    @GetMapping("/active")
    @Operation(summary = "Get active cases", description = "Get all active counseling cases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active cases retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<List<CounselingCaseResponse>> getActiveCases() {
        logger.debug("Fetching active counseling cases");

        List<CounselingCaseResponse> cases = caseService.getActiveCases();
        logger.debug("Retrieved {} active counseling cases", cases.size());

        return ResponseEntity.ok(cases);
    }

    /**
     * Get high priority cases
     */
    @GetMapping("/high-priority")
    @Operation(summary = "Get high priority cases", description = "Get all high priority counseling cases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "High priority cases retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<List<CounselingCaseResponse>> getHighPriorityCases() {
        logger.debug("Fetching high priority counseling cases");

        List<CounselingCaseResponse> cases = caseService.getHighPriorityCases();
        logger.debug("Retrieved {} high priority counseling cases", cases.size());

        return ResponseEntity.ok(cases);
    }

    /**
     * Get case statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get case statistics", description = "Get counseling case statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Map<String, Object>> getCaseStatistics() {
        logger.debug("Fetching counseling case statistics");

        try {
            Map<String, Object> statistics = caseService.getCaseStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved counseling case statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get counseling case statistics", e);
            throw e;
        }
    }

    /**
     * Advanced search for cases
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for counseling cases with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingCaseResponse>> advancedSearch(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long counselorId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String caseCategory,
            @RequestParam(required = false) String severityLevel,
            @RequestParam(required = false) String caseStatus,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for counseling cases with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedDate"));
        Page<CounselingCaseResponse> cases = caseService.advancedSearch(
                studentId, counselorId, startDate, endDate, caseCategory, severityLevel, caseStatus, pageable);

        logger.debug("Found {} counseling cases matching search criteria", cases.getTotalElements());
        return ResponseEntity.ok(cases);
    }
}