package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateLearningMaterialRequest;
import com.simsekolah.dto.response.LearningMaterialResponse;
import com.simsekolah.service.LearningMaterialService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for learning material management operations
 * Provides endpoints for materi (learning materials)
 */
@RestController
@RequestMapping({"/api/v1/lms/materi", "/api/lms/materi"})
@Tag(name = "Learning Material Management", description = "Learning material management endpoints")
@Validated
public class LearningMaterialController {

    private static final Logger logger = LoggerFactory.getLogger(LearningMaterialController.class);

    @Autowired
    private LearningMaterialService materialService;

    /**
     * Create a new learning material
     */
    @PostMapping
    @Operation(summary = "Create learning material", description = "Create a new learning material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Material created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LearningMaterialResponse> createMaterial(@Valid @RequestBody CreateLearningMaterialRequest request) {
        logger.info("Creating learning material: {}", request.getTitle());

        try {
            LearningMaterialResponse response = materialService.createMaterial(request);
            logger.info("Successfully created learning material with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create learning material: {}", request.getTitle(), e);
            throw e;
        }
    }

    /**
     * Get all learning materials with pagination
     */
    @GetMapping
    @Operation(summary = "Get all learning materials", description = "Retrieve all learning materials with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materials retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<LearningMaterialResponse>> getAllMaterials(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all learning materials - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<LearningMaterialResponse> materials = materialService.getAllMaterials(pageable);
        logger.debug("Retrieved {} learning materials", materials.getTotalElements());

        return ResponseEntity.ok(materials);
    }

    /**
     * Get learning material by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get learning material by ID", description = "Retrieve learning material information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material found"),
        @ApiResponse(responseCode = "404", description = "Material not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<LearningMaterialResponse> getMaterialById(@PathVariable("id") @NotNull Long materialId) {
        logger.debug("Fetching learning material by ID: {}", materialId);

        Optional<LearningMaterialResponse> material = materialService.getMaterialById(materialId);
        if (material.isPresent()) {
            logger.debug("Learning material found with ID: {}", materialId);
            return ResponseEntity.ok(material.get());
        } else {
            logger.debug("Learning material not found with ID: {}", materialId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get materials by subject
     */
    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get materials by subject", description = "Get all learning materials for a specific subject")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materials retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<LearningMaterialResponse>> getMaterialsBySubject(
            @PathVariable("subjectId") @NotNull Long subjectId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching learning materials by subject ID: {}", subjectId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "chapterNumber"));
        Page<LearningMaterialResponse> materials = materialService.getMaterialsBySubject(subjectId, pageable);

        logger.debug("Retrieved {} learning materials for subject: {}", materials.getTotalElements(), subjectId);
        return ResponseEntity.ok(materials);
    }

    /**
     * Get materials by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get materials by teacher", description = "Get all learning materials created by a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materials retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<LearningMaterialResponse>> getMaterialsByTeacher(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching learning materials by teacher ID: {}", teacherId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<LearningMaterialResponse> materials = materialService.getMaterialsByTeacher(teacherId, pageable);

        logger.debug("Retrieved {} learning materials for teacher: {}", materials.getTotalElements(), teacherId);
        return ResponseEntity.ok(materials);
    }

    /**
     * Publish material
     */
    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish material", description = "Publish a learning material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material published successfully"),
        @ApiResponse(responseCode = "404", description = "Material not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LearningMaterialResponse> publishMaterial(@PathVariable("id") @NotNull Long materialId) {
        logger.info("Publishing learning material: {}", materialId);

        try {
            LearningMaterialResponse response = materialService.publishMaterial(materialId);
            logger.info("Successfully published learning material: {}", materialId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to publish learning material: {}", materialId, e);
            throw e;
        }
    }

    /**
     * Unpublish material
     */
    @PostMapping("/{id}/unpublish")
    @Operation(summary = "Unpublish material", description = "Unpublish a learning material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material unpublished successfully"),
        @ApiResponse(responseCode = "404", description = "Material not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LearningMaterialResponse> unpublishMaterial(@PathVariable("id") @NotNull Long materialId) {
        logger.info("Unpublishing learning material: {}", materialId);

        try {
            LearningMaterialResponse response = materialService.unpublishMaterial(materialId);
            logger.info("Successfully unpublished learning material: {}", materialId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to unpublish learning material: {}", materialId, e);
            throw e;
        }
    }

    /**
     * Get published materials
     */
    @GetMapping("/published")
    @Operation(summary = "Get published materials", description = "Get all published learning materials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Published materials retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<LearningMaterialResponse>> getPublishedMaterials(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching published learning materials");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<LearningMaterialResponse> materials = materialService.getPublishedMaterials(pageable);

        logger.debug("Retrieved {} published learning materials", materials.getTotalElements());
        return ResponseEntity.ok(materials);
    }

    /**
     * Increment view count
     */
    @PostMapping("/{id}/view")
    @Operation(summary = "Increment view count", description = "Increment the view count for a learning material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "View count incremented successfully"),
        @ApiResponse(responseCode = "404", description = "Material not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> incrementViewCount(@PathVariable("id") @NotNull Long materialId) {
        logger.debug("Incrementing view count for learning material: {}", materialId);

        try {
            materialService.incrementViewCount(materialId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "View count incremented successfully");
            response.put("materialId", materialId);
            response.put("timestamp", System.currentTimeMillis());

            logger.debug("Successfully incremented view count for learning material: {}", materialId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to increment view count for learning material: {}", materialId, e);
            throw e;
        }
    }

    /**
     * Increment download count
     */
    @PostMapping("/{id}/download")
    @Operation(summary = "Increment download count", description = "Increment the download count for a learning material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Download count incremented successfully"),
        @ApiResponse(responseCode = "404", description = "Material not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> incrementDownloadCount(@PathVariable("id") @NotNull Long materialId) {
        logger.debug("Incrementing download count for learning material: {}", materialId);

        try {
            materialService.incrementDownloadCount(materialId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Download count incremented successfully");
            response.put("materialId", materialId);
            response.put("timestamp", System.currentTimeMillis());

            logger.debug("Successfully incremented download count for learning material: {}", materialId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to increment download count for learning material: {}", materialId, e);
            throw e;
        }
    }

    /**
     * Get material statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get material statistics", description = "Get learning material statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getMaterialStatistics() {
        logger.debug("Fetching learning material statistics");

        try {
            Map<String, Object> statistics = materialService.getMaterialStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved learning material statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get learning material statistics", e);
            throw e;
        }
    }

    /**
     * Get most viewed materials
     */
    @GetMapping("/most-viewed")
    @Operation(summary = "Get most viewed materials", description = "Get most viewed learning materials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Most viewed materials retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<LearningMaterialResponse>> getMostViewedMaterials(
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") @Min(1) int limit) {

        logger.debug("Fetching most viewed learning materials, limit: {}", limit);

        List<LearningMaterialResponse> materials = materialService.getMostViewedMaterials(limit);
        logger.debug("Retrieved {} most viewed learning materials", materials.size());

        return ResponseEntity.ok(materials);
    }

    /**
     * Advanced search for materials
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for learning materials with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<LearningMaterialResponse>> advancedSearch(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String materialType,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) Integer chapterNumber,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for learning materials with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<LearningMaterialResponse> materials = materialService.advancedSearch(
                subjectId, teacherId, materialType, difficultyLevel, isPublished, chapterNumber, pageable);

        logger.debug("Found {} learning materials matching search criteria", materials.getTotalElements());
        return ResponseEntity.ok(materials);
    }
}