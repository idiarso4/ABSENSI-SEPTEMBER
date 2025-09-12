package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateQuestionBankRequest;
import com.simsekolah.dto.response.QuestionBankResponse;
import com.simsekolah.service.QuestionBankService;
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
 * REST controller for question bank management operations
 * Provides endpoints for daftar-soal (question bank)
 */
@RestController
@RequestMapping({"/api/v1/lms/daftar-soal", "/api/lms/daftar-soal"})
@Tag(name = "Question Bank Management", description = "Question bank management endpoints")
@Validated
public class QuestionBankController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionBankController.class);

    @Autowired
    private QuestionBankService questionService;

    /**
     * Create a new question
     */
    @PostMapping
    @Operation(summary = "Create question", description = "Create a new question in the question bank")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<QuestionBankResponse> createQuestion(@Valid @RequestBody CreateQuestionBankRequest request) {
        logger.info("Creating question: {}", request.getQuestionText());

        try {
            QuestionBankResponse response = questionService.createQuestion(request);
            logger.info("Successfully created question with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create question: {}", request.getQuestionText(), e);
            throw e;
        }
    }

    /**
     * Get all questions with pagination
     */
    @GetMapping
    @Operation(summary = "Get all questions", description = "Retrieve all questions in the question bank with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<QuestionBankResponse>> getAllQuestions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all questions - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<QuestionBankResponse> questions = questionService.getAllQuestions(pageable);
        logger.debug("Retrieved {} questions", questions.getTotalElements());

        return ResponseEntity.ok(questions);
    }

    /**
     * Get question by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID", description = "Retrieve question information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Question found"),
        @ApiResponse(responseCode = "404", description = "Question not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<QuestionBankResponse> getQuestionById(@PathVariable("id") @NotNull Long questionId) {
        logger.debug("Fetching question by ID: {}", questionId);

        Optional<QuestionBankResponse> question = questionService.getQuestionById(questionId);
        if (question.isPresent()) {
            logger.debug("Question found with ID: {}", questionId);
            return ResponseEntity.ok(question.get());
        } else {
            logger.debug("Question not found with ID: {}", questionId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get questions by subject
     */
    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get questions by subject", description = "Get all questions for a specific subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<QuestionBankResponse>> getQuestionsBySubject(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching questions by subject ID: {}", subjectId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionBankResponse> questions = questionService.getQuestionsBySubject(subjectId, pageable);

        logger.debug("Retrieved {} questions for subject: {}", questions.getTotalElements(), subjectId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get questions by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get questions by teacher", description = "Get all questions created by a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<QuestionBankResponse>> getQuestionsByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching questions by teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionBankResponse> questions = questionService.getQuestionsByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} questions for teacher: {}", questions.getTotalElements(), teacherId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get active questions
     */
    @GetMapping("/active")
    @Operation(summary = "Get active questions", description = "Get all active questions in the question bank")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<QuestionBankResponse>> getActiveQuestions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching active questions");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionBankResponse> questions = questionService.getActiveQuestions(pageable);

        logger.debug("Retrieved {} active questions", questions.getTotalElements());
        return ResponseEntity.ok(questions);
    }

    /**
     * Activate question
     */
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate question", description = "Activate a question in the question bank")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Question activated successfully"),
        @ApiResponse(responseCode = "404", description = "Question not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<QuestionBankResponse> activateQuestion(@PathVariable("id") @NotNull Long questionId) {
        logger.info("Activating question: {}", questionId);

        try {
            QuestionBankResponse response = questionService.activateQuestion(questionId);
            logger.info("Successfully activated question: {}", questionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to activate question: {}", questionId, e);
            throw e;
        }
    }

    /**
     * Deactivate question
     */
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate question", description = "Deactivate a question in the question bank")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Question deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Question not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<QuestionBankResponse> deactivateQuestion(@PathVariable("id") @NotNull Long questionId) {
        logger.info("Deactivating question: {}", questionId);

        try {
            QuestionBankResponse response = questionService.deactivateQuestion(questionId);
            logger.info("Successfully deactivated question: {}", questionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to deactivate question: {}", questionId, e);
            throw e;
        }
    }

    /**
     * Get questions by difficulty and subject
     */
    @GetMapping("/subject/{subjectId}/difficulty/{difficultyLevel}")
    @Operation(summary = "Get questions by difficulty", description = "Get questions by subject and difficulty level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<QuestionBankResponse>> getQuestionsByDifficultyAndSubject(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @PathVariable("difficultyLevel") String difficultyLevel) {

        logger.debug("Fetching questions by subject {} and difficulty {}", subjectId, difficultyLevel);

        List<QuestionBankResponse> questions = questionService.getQuestionsByDifficultyAndSubject(subjectId, difficultyLevel);
        logger.debug("Retrieved {} questions for subject {} and difficulty {}", questions.size(), subjectId, difficultyLevel);

        return ResponseEntity.ok(questions);
    }

    /**
     * Get questions by type and subject
     */
    @GetMapping("/subject/{subjectId}/type/{questionType}")
    @Operation(summary = "Get questions by type", description = "Get questions by subject and question type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<QuestionBankResponse>> getQuestionsByTypeAndSubject(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @PathVariable("questionType") String questionType) {

        logger.debug("Fetching questions by subject {} and type {}", subjectId, questionType);

        List<QuestionBankResponse> questions = questionService.getQuestionsByTypeAndSubject(subjectId, questionType);
        logger.debug("Retrieved {} questions for subject {} and type {}", questions.size(), subjectId, questionType);

        return ResponseEntity.ok(questions);
    }

    /**
     * Get question statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get question statistics", description = "Get question bank statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getQuestionStatistics() {
        logger.debug("Fetching question bank statistics");

        try {
            Map<String, Object> statistics = questionService.getQuestionStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved question bank statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get question bank statistics", e);
            throw e;
        }
    }

    /**
     * Get most used questions
     */
    @GetMapping("/most-used")
    @Operation(summary = "Get most used questions", description = "Get most used questions in the question bank")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Most used questions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<QuestionBankResponse>> getMostUsedQuestions(
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") @Min(1) int limit) {

        logger.debug("Fetching most used questions, limit: {}", limit);

        List<QuestionBankResponse> questions = questionService.getMostUsedQuestions(limit);
        logger.debug("Retrieved {} most used questions", questions.size());

        return ResponseEntity.ok(questions);
    }

    /**
     * Advanced search for questions
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for questions with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<QuestionBankResponse>> advancedSearch(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String chapterTopic,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for questions with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionBankResponse> questions = questionService.advancedSearch(
                subjectId, teacherId, questionType, difficultyLevel, isActive, chapterTopic, pageable);

        logger.debug("Found {} questions matching search criteria", questions.getTotalElements());
        return ResponseEntity.ok(questions);
    }
}