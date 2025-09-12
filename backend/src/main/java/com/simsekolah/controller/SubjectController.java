package com.simsekolah.controller;

import com.simsekolah.entity.Subject;
import com.simsekolah.service.SubjectService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Subject Management", description = "Subject management endpoints")
@RestController
@RequestMapping("/api/v1/subjects")
@Validated
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create subject", description = "Create a new subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subject created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Subject code already exists")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        logger.info("Creating new subject with code: {}", subject.getCode());
        try {
            Subject createdSubject = subjectService.createSubject(subject);
            logger.info("Successfully created subject with ID: {}", createdSubject.getId());
            return ResponseEntity.status(201).body(createdSubject);
        } catch (Exception e) {
            logger.error("Failed to create subject with code: {}", subject.getCode(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieve all subjects with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<Subject>> getAllSubjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.debug("Fetching all subjects - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Subject> subjects = subjectService.getAllSubjects(pageable);
        logger.debug("Retrieved {} subjects", subjects.getTotalElements());
        
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieve subject information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject found"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Subject> getSubjectById(@PathVariable("id") @NotNull Long subjectId) {
        logger.debug("Fetching subject by ID: {}", subjectId);
        
        Optional<Subject> subject = subjectService.getSubjectById(subjectId);
        if (subject.isPresent()) {
            logger.debug("Subject found with ID: {}", subjectId);
            return ResponseEntity.ok(subject.get());
        } else {
            logger.debug("Subject not found with ID: {}", subjectId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get subject by code", description = "Retrieve subject information by code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject found"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Subject> getSubjectByCode(@PathVariable("code") String code) {
        logger.debug("Fetching subject by code: {}", code);
        
        Optional<Subject> subject = subjectService.getSubjectByCode(code);
        if (subject.isPresent()) {
            logger.debug("Subject found with code: {}", code);
            return ResponseEntity.ok(subject.get());
        } else {
            logger.debug("Subject not found with code: {}", code);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", description = "Update subject information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject updated successfully"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable("id") @NotNull Long subjectId,
            @Valid @RequestBody Subject subjectDetails) {
        
        logger.info("Updating subject with ID: {}", subjectId);
        
        try {
            Subject updatedSubject = subjectService.updateSubject(subjectId, subjectDetails);
            logger.info("Successfully updated subject with ID: {}", subjectId);
            return ResponseEntity.ok(updatedSubject);
        } catch (Exception e) {
            logger.error("Failed to update subject with ID: {}", subjectId, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", description = "Delete subject record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Subject deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Subject not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable("id") @NotNull Long subjectId) {
        logger.info("Deleting subject with ID: {}", subjectId);
        
        try {
            subjectService.deleteSubject(subjectId);
            logger.info("Successfully deleted subject with ID: {}", subjectId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete subject with ID: {}", subjectId, e);
            throw e;
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search subjects", description = "Search subjects by name or code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<Subject>> searchSubjects(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Searching subjects with query: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Subject> subjects = subjectService.searchSubjects(query, pageable);
        
        logger.debug("Found {} subjects matching query: {}", subjects.getTotalElements(), query);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get subjects by type", description = "Retrieve subjects by type (THEORY, PRACTICE, MIXED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subjects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Subject>> getSubjectsByType(@PathVariable Subject.SubjectType type) {
        logger.debug("Fetching subjects by type: {}", type);
        
        List<Subject> subjects = subjectService.getSubjectsByType(type);
        logger.debug("Retrieved {} subjects of type: {}", subjects.size(), type);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get subject statistics", description = "Retrieve subject count and distribution statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getSubjectStatistics() {
        logger.debug("Fetching subject statistics");
        
        try {
            long activeCount = subjectService.countActiveSubjects();
            Map<String, Object> stats = new HashMap<>();
            stats.put("activeCount", activeCount);
            stats.put("totalCount", subjectService.getAllSubjects(PageRequest.of(0, 1)).getTotalElements());
            stats.put("timestamp", System.currentTimeMillis());
            
            logger.debug("Retrieved subject statistics");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Failed to get subject statistics", e);
            throw e;
        }
    }
}