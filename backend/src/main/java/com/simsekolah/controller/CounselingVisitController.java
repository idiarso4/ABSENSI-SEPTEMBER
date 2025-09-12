package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateCounselingVisitRequest;
import com.simsekolah.dto.response.CounselingVisitResponse;
import com.simsekolah.service.CounselingVisitService;
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
 * REST controller for counseling visit management operations
 * Provides endpoints for daftar-kunjungan (counseling visit list)
 */
@RestController
@RequestMapping({"/api/v1/bk/daftar-kunjungan", "/api/bk/daftar-kunjungan"})
@Tag(name = "Counseling Visit Management", description = "Counseling visit management endpoints")
@Validated
public class CounselingVisitController {

    private static final Logger logger = LoggerFactory.getLogger(CounselingVisitController.class);

    @Autowired
    private CounselingVisitService visitService;

    /**
     * Create a new counseling visit
     */
    @PostMapping
    @Operation(summary = "Create counseling visit", description = "Create a new counseling visit record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Visit created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingVisitResponse> createVisit(@Valid @RequestBody CreateCounselingVisitRequest request) {
        logger.info("Creating counseling visit for case: {}", request.getCounselingCaseId());

        try {
            CounselingVisitResponse response = visitService.createVisit(request);
            logger.info("Successfully created counseling visit with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create counseling visit for case: {}", request.getCounselingCaseId(), e);
            throw e;
        }
    }

    /**
     * Get all counseling visits with pagination
     */
    @GetMapping
    @Operation(summary = "Get all counseling visits", description = "Retrieve all counseling visits with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingVisitResponse>> getAllVisits(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "visitDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all counseling visits - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CounselingVisitResponse> visits = visitService.getAllVisits(pageable);
        logger.debug("Retrieved {} counseling visits", visits.getTotalElements());

        return ResponseEntity.ok(visits);
    }

    /**
     * Get counseling visit by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get counseling visit by ID", description = "Retrieve counseling visit information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visit found"),
        @ApiResponse(responseCode = "404", description = "Visit not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingVisitResponse> getVisitById(@PathVariable("id") @NotNull Long visitId) {
        logger.debug("Fetching counseling visit by ID: {}", visitId);

        Optional<CounselingVisitResponse> visit = visitService.getVisitById(visitId);
        if (visit.isPresent()) {
            logger.debug("Counseling visit found with ID: {}", visitId);
            return ResponseEntity.ok(visit.get());
        } else {
            logger.debug("Counseling visit not found with ID: {}", visitId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get visits by counseling case
     */
    @GetMapping("/case/{caseId}")
    @Operation(summary = "Get visits by case", description = "Get all counseling visits for a specific case")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingVisitResponse>> getVisitsByCase(
            @PathVariable("caseId") @NotNull Long caseId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling visits by case ID: {}", caseId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitDate"));
        Page<CounselingVisitResponse> visits = visitService.getVisitsByCase(caseId, pageable);

        logger.debug("Retrieved {} counseling visits for case: {}", visits.getTotalElements(), caseId);
        return ResponseEntity.ok(visits);
    }

    /**
     * Get visits by counselor
     */
    @GetMapping("/counselor/{counselorId}")
    @Operation(summary = "Get visits by counselor", description = "Get all counseling visits conducted by a specific counselor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visits retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingVisitResponse>> getVisitsByCounselor(
            @PathVariable("counselorId") @NotNull Long counselorId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling visits by counselor ID: {}", counselorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitDate"));
        Page<CounselingVisitResponse> visits = visitService.getVisitsByCounselor(counselorId, pageable);

        logger.debug("Retrieved {} counseling visits for counselor: {}", visits.getTotalElements(), counselorId);
        return ResponseEntity.ok(visits);
    }

    /**
     * Update visit status
     */
    @PostMapping("/{id}/status/{status}")
    @Operation(summary = "Update visit status", description = "Update the status of a counseling visit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Visit status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Visit not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingVisitResponse> updateVisitStatus(
            @PathVariable("id") @NotNull Long visitId,
            @PathVariable("status") String status) {

        logger.info("Updating counseling visit {} status to: {}", visitId, status);

        try {
            CounselingVisitResponse response = visitService.updateVisitStatus(visitId, status);
            logger.info("Successfully updated counseling visit {} status to: {}", visitId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update counseling visit {} status to: {}", visitId, status, e);
            throw e;
        }
    }

    /**
     * Get today's scheduled visits
     */
    @GetMapping("/today")
    @Operation(summary = "Get today's visits", description = "Get all counseling visits scheduled for today")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Today's visits retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<List<CounselingVisitResponse>> getTodaysVisits() {
        logger.debug("Fetching today's scheduled counseling visits");

        List<CounselingVisitResponse> visits = visitService.getTodaysScheduledVisits();
        logger.debug("Retrieved {} today's scheduled counseling visits", visits.size());

        return ResponseEntity.ok(visits);
    }

    /**
     * Get completed visits
     */
    @GetMapping("/completed")
    @Operation(summary = "Get completed visits", description = "Get all completed counseling visits")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Completed visits retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingVisitResponse>> getCompletedVisits(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching completed counseling visits");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitDate"));
        Page<CounselingVisitResponse> visits = visitService.getCompletedVisits(pageable);

        logger.debug("Retrieved {} completed counseling visits", visits.getTotalElements());
        return ResponseEntity.ok(visits);
    }

    /**
     * Get visit statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get visit statistics", description = "Get counseling visit statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Map<String, Object>> getVisitStatistics() {
        logger.debug("Fetching counseling visit statistics");

        try {
            Map<String, Object> statistics = visitService.getVisitStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved counseling visit statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get counseling visit statistics", e);
            throw e;
        }
    }

    /**
     * Advanced search for visits
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for counseling visits with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingVisitResponse>> advancedSearch(
            @RequestParam(required = false) Long caseId,
            @RequestParam(required = false) Long counselorId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String visitType,
            @RequestParam(required = false) String visitStatus,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for counseling visits with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitDate"));
        Page<CounselingVisitResponse> visits = visitService.advancedSearch(
                caseId, counselorId, startDate, endDate, visitType, visitStatus, pageable);

        logger.debug("Found {} counseling visits matching search criteria", visits.getTotalElements());
        return ResponseEntity.ok(visits);
    }
}