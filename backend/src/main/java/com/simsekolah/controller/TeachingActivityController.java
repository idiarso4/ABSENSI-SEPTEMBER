package com.simsekolah.controller;

import com.simsekolah.dto.response.TeachingActivityResponse;
import com.simsekolah.entity.User;
import com.simsekolah.service.TeachingActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/teaching-activities")
@Tag(name = "Teaching Activity Management", description = "Endpoints for managing teaching activities and journals")
@Validated
@RequiredArgsConstructor
@Slf4j
public class TeachingActivityController {

    private final TeachingActivityService teachingActivityService;

    @PostMapping("/generate-today")
    @Operation(summary = "Generate Today's Activities", description = "Automatically creates teaching activities for the current day based on active schedules. Skips existing ones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Process completed successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> generateTodaysActivities() {
        log.info("Request received to generate today's teaching activities.");
        try {
            Map<String, Object> result = teachingActivityService.generateTodaysActivities();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("An unexpected error occurred during teaching activity generation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "An internal server error occurred.", "message", e.getMessage()));
        }
    }

    @GetMapping("/me/pending")
    @Operation(summary = "Get Pending Activities for Current Teacher", description = "Retrieves a paginated list of teaching activities for the currently logged-in teacher that are not yet marked as complete (e.g., attendance not taken).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of pending activities retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Forbidden. User is not a teacher.")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Page<TeachingActivityResponse>> getMyPendingActivities(
            Authentication authentication,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") @Min(1) int size) {

        User currentUser = (User) authentication.getPrincipal();
        Long teacherId = currentUser.getId();

        log.info("Fetching pending activities for teacher ID: {}", teacherId);

        // Sorting is defined in the repository method name for consistency
        Pageable pageable = PageRequest.of(page, size);

        Page<TeachingActivityResponse> pendingActivities = teachingActivityService.getPendingActivitiesForTeacher(teacherId, pageable);

        return ResponseEntity.ok(pendingActivities);
    }
}