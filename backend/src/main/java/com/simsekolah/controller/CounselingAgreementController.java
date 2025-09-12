package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateCounselingAgreementRequest;
import com.simsekolah.dto.response.CounselingAgreementResponse;
import com.simsekolah.service.CounselingAgreementService;
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
 * REST controller for counseling agreement management operations
 * Provides endpoints for surat-kesepakatan (counseling agreement letters)
 */
@RestController
@RequestMapping({"/api/v1/bk/surat-kesepakatan", "/api/bk/surat-kesepakatan"})
@Tag(name = "Counseling Agreement Management", description = "Counseling agreement management endpoints")
@Validated
public class CounselingAgreementController {

    private static final Logger logger = LoggerFactory.getLogger(CounselingAgreementController.class);

    @Autowired
    private CounselingAgreementService agreementService;

    /**
     * Create a new counseling agreement
     */
    @PostMapping
    @Operation(summary = "Create counseling agreement", description = "Create a new counseling agreement letter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agreement created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingAgreementResponse> createAgreement(@Valid @RequestBody CreateCounselingAgreementRequest request) {
        logger.info("Creating counseling agreement for case: {}", request.getCounselingCaseId());

        try {
            CounselingAgreementResponse response = agreementService.createAgreement(request);
            logger.info("Successfully created counseling agreement with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create counseling agreement for case: {}", request.getCounselingCaseId(), e);
            throw e;
        }
    }

    /**
     * Get all counseling agreements with pagination
     */
    @GetMapping
    @Operation(summary = "Get all counseling agreements", description = "Retrieve all counseling agreements with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agreements retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingAgreementResponse>> getAllAgreements(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "effectiveDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all counseling agreements - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CounselingAgreementResponse> agreements = agreementService.getAllAgreements(pageable);
        logger.debug("Retrieved {} counseling agreements", agreements.getTotalElements());

        return ResponseEntity.ok(agreements);
    }

    /**
     * Get counseling agreement by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get counseling agreement by ID", description = "Retrieve counseling agreement information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agreement found"),
        @ApiResponse(responseCode = "404", description = "Agreement not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingAgreementResponse> getAgreementById(@PathVariable("id") @NotNull Long agreementId) {
        logger.debug("Fetching counseling agreement by ID: {}", agreementId);

        Optional<CounselingAgreementResponse> agreement = agreementService.getAgreementById(agreementId);
        if (agreement.isPresent()) {
            logger.debug("Counseling agreement found with ID: {}", agreementId);
            return ResponseEntity.ok(agreement.get());
        } else {
            logger.debug("Counseling agreement not found with ID: {}", agreementId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get agreements by counseling case
     */
    @GetMapping("/case/{caseId}")
    @Operation(summary = "Get agreements by case", description = "Get all counseling agreements for a specific case")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agreements retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingAgreementResponse>> getAgreementsByCase(
            @PathVariable("caseId") @NotNull Long caseId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling agreements by case ID: {}", caseId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "effectiveDate"));
        Page<CounselingAgreementResponse> agreements = agreementService.getAgreementsByCase(caseId, pageable);

        logger.debug("Retrieved {} counseling agreements for case: {}", agreements.getTotalElements(), caseId);
        return ResponseEntity.ok(agreements);
    }

    /**
     * Get agreements by counselor
     */
    @GetMapping("/counselor/{counselorId}")
    @Operation(summary = "Get agreements by counselor", description = "Get all counseling agreements created by a specific counselor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agreements retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingAgreementResponse>> getAgreementsByCounselor(
            @PathVariable("counselorId") @NotNull Long counselorId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching counseling agreements by counselor ID: {}", counselorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "effectiveDate"));
        Page<CounselingAgreementResponse> agreements = agreementService.getAgreementsByCounselor(counselorId, pageable);

        logger.debug("Retrieved {} counseling agreements for counselor: {}", agreements.getTotalElements(), counselorId);
        return ResponseEntity.ok(agreements);
    }

    /**
     * Update agreement status
     */
    @PostMapping("/{id}/status/{status}")
    @Operation(summary = "Update agreement status", description = "Update the status of a counseling agreement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agreement status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Agreement not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<CounselingAgreementResponse> updateAgreementStatus(
            @PathVariable("id") @NotNull Long agreementId,
            @PathVariable("status") String status) {

        logger.info("Updating counseling agreement {} status to: {}", agreementId, status);

        try {
            CounselingAgreementResponse response = agreementService.updateAgreementStatus(agreementId, status);
            logger.info("Successfully updated counseling agreement {} status to: {}", agreementId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update counseling agreement {} status to: {}", agreementId, status, e);
            throw e;
        }
    }

    /**
     * Get active agreements
     */
    @GetMapping("/active")
    @Operation(summary = "Get active agreements", description = "Get all active counseling agreements")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active agreements retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingAgreementResponse>> getActiveAgreements(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching active counseling agreements");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "effectiveDate"));
        Page<CounselingAgreementResponse> agreements = agreementService.getActiveAgreements(pageable);

        logger.debug("Retrieved {} active counseling agreements", agreements.getTotalElements());
        return ResponseEntity.ok(agreements);
    }

    /**
     * Get pending signatures
     */
    @GetMapping("/pending-signatures")
    @Operation(summary = "Get pending signatures", description = "Get all agreements pending signatures")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending signatures retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<List<CounselingAgreementResponse>> getPendingSignatures() {
        logger.debug("Fetching counseling agreements pending signatures");

        List<CounselingAgreementResponse> agreements = agreementService.getPendingSignatures();
        logger.debug("Retrieved {} counseling agreements pending signatures", agreements.size());

        return ResponseEntity.ok(agreements);
    }

    /**
     * Get agreement statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get agreement statistics", description = "Get counseling agreement statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Map<String, Object>> getAgreementStatistics() {
        logger.debug("Fetching counseling agreement statistics");

        try {
            Map<String, Object> statistics = agreementService.getAgreementStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved counseling agreement statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get counseling agreement statistics", e);
            throw e;
        }
    }

    /**
     * Advanced search for agreements
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for counseling agreements with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('COUNSELOR')")
    public ResponseEntity<Page<CounselingAgreementResponse>> advancedSearch(
            @RequestParam(required = false) Long caseId,
            @RequestParam(required = false) Long counselorId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String agreementType,
            @RequestParam(required = false) String agreementStatus,
            @RequestParam(required = false) String complianceStatus,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for counseling agreements with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "effectiveDate"));
        Page<CounselingAgreementResponse> agreements = agreementService.advancedSearch(
                caseId, counselorId, startDate, endDate, agreementType, agreementStatus, complianceStatus, pageable);

        logger.debug("Found {} counseling agreements matching search criteria", agreements.getTotalElements());
        return ResponseEntity.ok(agreements);
    }
}