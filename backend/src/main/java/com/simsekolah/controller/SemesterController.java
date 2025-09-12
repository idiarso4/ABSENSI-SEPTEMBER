package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateSemesterRequest;
import com.simsekolah.dto.response.SemesterResponse;
import com.simsekolah.service.SemesterService;
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
 * REST controller for semester management operations
 * Provides endpoints for semester-management (semester management)
 */
@RestController
@RequestMapping({"/api/v1/semester-management", "/api/semester-management"})
@Tag(name = "Semester Management", description = "Semester management endpoints")
@Validated
public class SemesterController {

    private static final Logger logger = LoggerFactory.getLogger(SemesterController.class);

    @Autowired
    private SemesterService semesterService;

    /**
     * Create a new semester
     */
    @PostMapping
    @Operation(summary = "Create semester", description = "Create a new semester")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Semester created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SemesterResponse> createSemester(@Valid @RequestBody CreateSemesterRequest request) {
        logger.info("Creating semester: {} - {}", request.getAcademicYear(), request.getSemesterNumber());

        try {
            SemesterResponse response = semesterService.createSemester(request);
            logger.info("Successfully created semester with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create semester: {} - {}", request.getAcademicYear(), request.getSemesterNumber(), e);
            throw e;
        }
    }

    /**
     * Get all semesters with pagination
     */
    @GetMapping
    @Operation(summary = "Get all semesters", description = "Retrieve all semesters with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<SemesterResponse>> getAllSemesters(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all semesters - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<SemesterResponse> semesters = semesterService.getAllSemesters(pageable);
        logger.debug("Retrieved {} semesters", semesters.getTotalElements());

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get semester by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get semester by ID", description = "Retrieve semester information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semester found"),
        @ApiResponse(responseCode = "404", description = "Semester not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<SemesterResponse> getSemesterById(@PathVariable("id") @NotNull Long semesterId) {
        logger.debug("Fetching semester by ID: {}", semesterId);

        Optional<SemesterResponse> semester = semesterService.getSemesterById(semesterId);
        if (semester.isPresent()) {
            logger.debug("Semester found with ID: {}", semesterId);
            return ResponseEntity.ok(semester.get());
        } else {
            logger.debug("Semester not found with ID: {}", semesterId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get semesters by academic year
     */
    @GetMapping("/academic-year/{academicYear}")
    @Operation(summary = "Get semesters by academic year", description = "Get all semesters for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<SemesterResponse>> getSemestersByAcademicYear(
            @PathVariable("academicYear") String academicYear) {

        logger.debug("Fetching semesters by academic year: {}", academicYear);

        List<SemesterResponse> semesters = semesterService.getSemestersByAcademicYear(academicYear);
        logger.debug("Retrieved {} semesters for academic year: {}", semesters.size(), academicYear);

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get current semester
     */
    @GetMapping("/current")
    @Operation(summary = "Get current semester", description = "Get the currently active semester")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Current semester retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No current semester found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<SemesterResponse> getCurrentSemester() {
        logger.debug("Fetching current semester");

        Optional<SemesterResponse> semester = semesterService.getCurrentSemester();
        if (semester.isPresent()) {
            logger.debug("Current semester found");
            return ResponseEntity.ok(semester.get());
        } else {
            logger.debug("No current semester found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get upcoming semesters
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming semesters", description = "Get all upcoming semesters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Upcoming semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<SemesterResponse>> getUpcomingSemesters() {
        logger.debug("Fetching upcoming semesters");

        List<SemesterResponse> semesters = semesterService.getUpcomingSemesters();
        logger.debug("Retrieved {} upcoming semesters", semesters.size());

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get semesters with open registration
     */
    @GetMapping("/registration-open")
    @Operation(summary = "Get semesters with open registration", description = "Get semesters that have open registration period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<SemesterResponse>> getSemestersWithOpenRegistration() {
        logger.debug("Fetching semesters with open registration");

        List<SemesterResponse> semesters = semesterService.getSemestersWithOpenRegistration();
        logger.debug("Retrieved {} semesters with open registration", semesters.size());

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get semesters in exam period
     */
    @GetMapping("/exam-period")
    @Operation(summary = "Get semesters in exam period", description = "Get semesters that are currently in exam period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<SemesterResponse>> getSemestersInExamPeriod() {
        logger.debug("Fetching semesters in exam period");

        List<SemesterResponse> semesters = semesterService.getSemestersInExamPeriod();
        logger.debug("Retrieved {} semesters in exam period", semesters.size());

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get distinct academic years
     */
    @GetMapping("/academic-years")
    @Operation(summary = "Get academic years", description = "Get all distinct academic years")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Academic years retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<String>> getAcademicYears() {
        logger.debug("Fetching distinct academic years");

        List<String> academicYears = semesterService.getAcademicYears();
        logger.debug("Retrieved {} distinct academic years", academicYears.size());

        return ResponseEntity.ok(academicYears);
    }

    /**
     * Activate semester
     */
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate semester", description = "Activate a semester")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semester activated successfully"),
        @ApiResponse(responseCode = "404", description = "Semester not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SemesterResponse> activateSemester(@PathVariable("id") @NotNull Long semesterId) {
        logger.info("Activating semester: {}", semesterId);

        try {
            SemesterResponse response = semesterService.activateSemester(semesterId);
            logger.info("Successfully activated semester: {}", semesterId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to activate semester: {}", semesterId, e);
            throw e;
        }
    }

    /**
     * Complete semester
     */
    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete semester", description = "Mark a semester as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semester completed successfully"),
        @ApiResponse(responseCode = "404", description = "Semester not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SemesterResponse> completeSemester(@PathVariable("id") @NotNull Long semesterId) {
        logger.info("Completing semester: {}", semesterId);

        try {
            SemesterResponse response = semesterService.completeSemester(semesterId);
            logger.info("Successfully completed semester: {}", semesterId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to complete semester: {}", semesterId, e);
            throw e;
        }
    }

    /**
     * Get semester statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get semester statistics", description = "Get semester statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getSemesterStatistics() {
        logger.debug("Fetching semester statistics");

        try {
            Map<String, Object> statistics = semesterService.getSemesterStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved semester statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get semester statistics", e);
            throw e;
        }
    }

    /**
     * Get semester by academic year and semester number
     */
    @GetMapping("/academic-year/{academicYear}/semester/{semesterNumber}")
    @Operation(summary = "Get semester by academic year and number", description = "Get semester by academic year and semester number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semester found"),
        @ApiResponse(responseCode = "404", description = "Semester not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<SemesterResponse> getSemesterByAcademicYearAndNumber(
            @PathVariable("academicYear") String academicYear,
            @PathVariable("semesterNumber") Integer semesterNumber) {

        logger.debug("Fetching semester by academic year {} and semester number {}", academicYear, semesterNumber);

        Optional<SemesterResponse> semester = semesterService.getSemesterByAcademicYearAndNumber(academicYear, semesterNumber);
        if (semester.isPresent()) {
            logger.debug("Semester found for academic year {} and semester number {}", academicYear, semesterNumber);
            return ResponseEntity.ok(semester.get());
        } else {
            logger.debug("Semester not found for academic year {} and semester number {}", academicYear, semesterNumber);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Advanced search for semesters
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for semesters with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<SemesterResponse>> advancedSearch(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Integer semesterNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for semesters with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<SemesterResponse> semesters = semesterService.advancedSearch(
                academicYear, semesterNumber, status, startDate, endDate, pageable);

        logger.debug("Found {} semesters matching search criteria", semesters.getTotalElements());
        return ResponseEntity.ok(semesters);
    }
}