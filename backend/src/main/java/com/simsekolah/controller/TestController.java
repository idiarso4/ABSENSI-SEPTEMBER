package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateTestRequest;
import com.simsekolah.dto.response.TestResponse;
import com.simsekolah.service.TestService;
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
 * REST controller for test management operations
 * Provides endpoints for ulangan (tests/exams)
 */
@RestController
@RequestMapping({"/api/v1/lms/ulangan", "/api/lms/ulangan"})
@Tag(name = "Test Management", description = "Test management endpoints")
@Validated
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    /**
     * Create a new test
     */
    @PostMapping
    @Operation(summary = "Create test", description = "Create a new test or exam")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Test created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TestResponse> createTest(@Valid @RequestBody CreateTestRequest request) {
        logger.info("Creating test: {}", request.getTestTitle());

        try {
            TestResponse response = testService.createTest(request);
            logger.info("Successfully created test with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create test: {}", request.getTestTitle(), e);
            throw e;
        }
    }

    /**
     * Get all tests with pagination
     */
    @GetMapping
    @Operation(summary = "Get all tests", description = "Retrieve all tests with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TestResponse>> getAllTests(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "testDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all tests - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<TestResponse> tests = testService.getAllTests(pageable);
        logger.debug("Retrieved {} tests", tests.getTotalElements());

        return ResponseEntity.ok(tests);
    }

    /**
     * Get test by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get test by ID", description = "Retrieve test information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Test found"),
        @ApiResponse(responseCode = "404", description = "Test not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<TestResponse> getTestById(@PathVariable("id") @NotNull Long testId) {
        logger.debug("Fetching test by ID: {}", testId);

        Optional<TestResponse> test = testService.getTestById(testId);
        if (test.isPresent()) {
            logger.debug("Test found with ID: {}", testId);
            return ResponseEntity.ok(test.get());
        } else {
            logger.debug("Test not found with ID: {}", testId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get tests by subject
     */
    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get tests by subject", description = "Get all tests for a specific subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<TestResponse>> getTestsBySubject(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching tests by subject ID: {}", subjectId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "testDate"));
        Page<TestResponse> tests = testService.getTestsBySubject(subjectId, pageable);

        logger.debug("Retrieved {} tests for subject: {}", tests.getTotalElements(), subjectId);
        return ResponseEntity.ok(tests);
    }

    /**
     * Get tests by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get tests by teacher", description = "Get all tests created by a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<TestResponse>> getTestsByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching tests by teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "testDate"));
        Page<TestResponse> tests = testService.getTestsByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} tests for teacher: {}", tests.getTotalElements(), teacherId);
        return ResponseEntity.ok(tests);
    }

    /**
     * Get tests by class room
     */
    @GetMapping("/class/{classRoomId}")
    @Operation(summary = "Get tests by class", description = "Get all tests for a specific class room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<TestResponse>> getTestsByClassRoom(
            @PathVariable("classRoomId") @NotNull Long classRoomId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching tests by class room ID: {}", classRoomId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "testDate"));
        Page<TestResponse> tests = testService.getTestsByClassRoom(classRoomId, pageable);

        logger.debug("Retrieved {} tests for class room: {}", tests.getTotalElements(), classRoomId);
        return ResponseEntity.ok(tests);
    }

    /**
     * Publish test
     */
    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish test", description = "Publish a test so students can access it")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Test published successfully"),
        @ApiResponse(responseCode = "404", description = "Test not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TestResponse> publishTest(@PathVariable("id") @NotNull Long testId) {
        logger.info("Publishing test: {}", testId);

        try {
            TestResponse response = testService.publishTest(testId);
            logger.info("Successfully published test: {}", testId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to publish test: {}", testId, e);
            throw e;
        }
    }

    /**
     * Get published tests
     */
    @GetMapping("/published")
    @Operation(summary = "Get published tests", description = "Get all published tests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Published tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<TestResponse>> getPublishedTests(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching published tests");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<TestResponse> tests = testService.getPublishedTests(pageable);

        logger.debug("Retrieved {} published tests", tests.getTotalElements());
        return ResponseEntity.ok(tests);
    }

    /**
     * Get today's tests
     */
    @GetMapping("/today")
    @Operation(summary = "Get today's tests", description = "Get all tests scheduled for today")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Today's tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<TestResponse>> getTodaysTests() {
        logger.debug("Fetching today's tests");

        List<TestResponse> tests = testService.getTodaysTests();
        logger.debug("Retrieved {} today's tests", tests.size());

        return ResponseEntity.ok(tests);
    }

    /**
     * Get upcoming tests
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming tests", description = "Get all upcoming tests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Upcoming tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<TestResponse>> getUpcomingTests() {
        logger.debug("Fetching upcoming tests");

        List<TestResponse> tests = testService.getUpcomingTests();
        logger.debug("Retrieved {} upcoming tests", tests.size());

        return ResponseEntity.ok(tests);
    }

    /**
     * Get test statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get test statistics", description = "Get test statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getTestStatistics() {
        logger.debug("Fetching test statistics");

        try {
            Map<String, Object> statistics = testService.getTestStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved test statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get test statistics", e);
            throw e;
        }
    }

    /**
     * Get tests by subject and type
     */
    @GetMapping("/subject/{subjectId}/type/{testType}")
    @Operation(summary = "Get tests by type", description = "Get tests by subject and test type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tests retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<TestResponse>> getTestsBySubjectAndType(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @PathVariable("testType") String testType) {

        logger.debug("Fetching tests by subject {} and type {}", subjectId, testType);

        List<TestResponse> tests = testService.getTestsBySubjectAndType(subjectId, testType);
        logger.debug("Retrieved {} tests for subject {} and type {}", tests.size(), subjectId, testType);

        return ResponseEntity.ok(tests);
    }

    /**
     * Advanced search for tests
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for tests with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<TestResponse>> advancedSearch(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long classRoomId,
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) String testStatus,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for tests with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "testDate"));
        Page<TestResponse> tests = testService.advancedSearch(
                subjectId, teacherId, classRoomId, testType, testStatus, isPublished, startDate, endDate, pageable);

        logger.debug("Found {} tests matching search criteria", tests.getTotalElements());
        return ResponseEntity.ok(tests);
    }
}
