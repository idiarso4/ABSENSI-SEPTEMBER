package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateAcademicCalendarRequest;
import com.simsekolah.dto.response.AcademicCalendarResponse;
import com.simsekolah.service.AcademicCalendarService;
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
 * REST controller for academic calendar management operations
 * Provides endpoints for academic-calendar (academic calendar management)
 */
@RestController
@RequestMapping({"/api/v1/academic-calendar", "/api/academic-calendar"})
@Tag(name = "Academic Calendar Management", description = "Academic calendar management endpoints")
@Validated
public class AcademicCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(AcademicCalendarController.class);

    @Autowired
    private AcademicCalendarService calendarService;

    /**
     * Create a new academic calendar event
     */
    @PostMapping
    @Operation(summary = "Create academic calendar event", description = "Create a new academic calendar event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AcademicCalendarResponse> createEvent(@Valid @RequestBody CreateAcademicCalendarRequest request) {
        logger.info("Creating academic calendar event: {}", request.getEventTitle());

        try {
            AcademicCalendarResponse response = calendarService.createEvent(request);
            logger.info("Successfully created academic calendar event with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create academic calendar event: {}", request.getEventTitle(), e);
            throw e;
        }
    }

    /**
     * Get all academic calendar events with pagination
     */
    @GetMapping
    @Operation(summary = "Get all academic calendar events", description = "Retrieve all academic calendar events with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<AcademicCalendarResponse>> getAllEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "eventDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all academic calendar events - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AcademicCalendarResponse> events = calendarService.getAllEvents(pageable);
        logger.debug("Retrieved {} academic calendar events", events.getTotalElements());

        return ResponseEntity.ok(events);
    }

    /**
     * Get academic calendar event by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get academic calendar event by ID", description = "Retrieve academic calendar event information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event found"),
        @ApiResponse(responseCode = "404", description = "Event not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<AcademicCalendarResponse> getEventById(@PathVariable("id") @NotNull Long eventId) {
        logger.debug("Fetching academic calendar event by ID: {}", eventId);

        Optional<AcademicCalendarResponse> event = calendarService.getEventById(eventId);
        if (event.isPresent()) {
            logger.debug("Academic calendar event found with ID: {}", eventId);
            return ResponseEntity.ok(event.get());
        } else {
            logger.debug("Academic calendar event not found with ID: {}", eventId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get events by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get events by date range", description = "Get all academic calendar events within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getEventsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        logger.debug("Fetching academic calendar events by date range: {} to {}", startDate, endDate);

        List<AcademicCalendarResponse> events = calendarService.getEventsByDateRange(startDate, endDate);
        logger.debug("Retrieved {} academic calendar events for date range", events.size());

        return ResponseEntity.ok(events);
    }

    /**
     * Get events by academic year
     */
    @GetMapping("/academic-year/{academicYear}")
    @Operation(summary = "Get events by academic year", description = "Get all academic calendar events for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<AcademicCalendarResponse>> getEventsByAcademicYear(
            @PathVariable("academicYear") String academicYear,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching academic calendar events by academic year: {}", academicYear);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        Page<AcademicCalendarResponse> events = calendarService.getEventsByAcademicYear(academicYear, pageable);

        logger.debug("Retrieved {} academic calendar events for academic year: {}", events.getTotalElements(), academicYear);
        return ResponseEntity.ok(events);
    }

    /**
     * Get holidays
     */
    @GetMapping("/holidays")
    @Operation(summary = "Get holidays", description = "Get all holiday events in the academic calendar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Holidays retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getHolidays(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        logger.debug("Fetching academic calendar holidays");

        List<AcademicCalendarResponse> holidays = calendarService.getHolidays(startDate, endDate);
        logger.debug("Retrieved {} academic calendar holidays", holidays.size());

        return ResponseEntity.ok(holidays);
    }

    /**
     * Get exam periods
     */
    @GetMapping("/exam-periods")
    @Operation(summary = "Get exam periods", description = "Get all exam period events in the academic calendar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exam periods retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getExamPeriods(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        logger.debug("Fetching academic calendar exam periods");

        List<AcademicCalendarResponse> examPeriods = calendarService.getExamPeriods(startDate, endDate);
        logger.debug("Retrieved {} academic calendar exam periods", examPeriods.size());

        return ResponseEntity.ok(examPeriods);
    }

    /**
     * Get teaching periods
     */
    @GetMapping("/teaching-periods")
    @Operation(summary = "Get teaching periods", description = "Get all teaching period events in the academic calendar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teaching periods retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getTeachingPeriods(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        logger.debug("Fetching academic calendar teaching periods");

        List<AcademicCalendarResponse> teachingPeriods = calendarService.getTeachingPeriods(startDate, endDate);
        logger.debug("Retrieved {} academic calendar teaching periods", teachingPeriods.size());

        return ResponseEntity.ok(teachingPeriods);
    }

    /**
     * Get today's events
     */
    @GetMapping("/today")
    @Operation(summary = "Get today's events", description = "Get all academic calendar events for today")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Today's events retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getTodaysEvents() {
        logger.debug("Fetching today's academic calendar events");

        List<AcademicCalendarResponse> events = calendarService.getTodaysEvents();
        logger.debug("Retrieved {} today's academic calendar events", events.size());

        return ResponseEntity.ok(events);
    }

    /**
     * Get upcoming events
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming events", description = "Get all upcoming academic calendar events")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Upcoming events retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<AcademicCalendarResponse>> getUpcomingEvents(
            @RequestParam(defaultValue = "30") int daysAhead) {

        logger.debug("Fetching upcoming academic calendar events, days ahead: {}", daysAhead);

        List<AcademicCalendarResponse> events = calendarService.getUpcomingEvents(daysAhead);
        logger.debug("Retrieved {} upcoming academic calendar events", events.size());

        return ResponseEntity.ok(events);
    }

    /**
     * Get distinct academic years
     */
    @GetMapping("/academic-years")
    @Operation(summary = "Get academic years", description = "Get all distinct academic years in the calendar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Academic years retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<String>> getAcademicYears() {
        logger.debug("Fetching distinct academic years");

        List<String> academicYears = calendarService.getAcademicYears();
        logger.debug("Retrieved {} distinct academic years", academicYears.size());

        return ResponseEntity.ok(academicYears);
    }

    /**
     * Get semesters by academic year
     */
    @GetMapping("/academic-year/{academicYear}/semesters")
    @Operation(summary = "Get semesters by academic year", description = "Get all semesters for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Semesters retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<Integer>> getSemestersByAcademicYear(@PathVariable("academicYear") String academicYear) {
        logger.debug("Fetching semesters for academic year: {}", academicYear);

        List<Integer> semesters = calendarService.getSemestersByAcademicYear(academicYear);
        logger.debug("Retrieved {} semesters for academic year: {}", semesters.size(), academicYear);

        return ResponseEntity.ok(semesters);
    }

    /**
     * Get calendar statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get calendar statistics", description = "Get academic calendar statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getCalendarStatistics() {
        logger.debug("Fetching academic calendar statistics");

        try {
            Map<String, Object> statistics = calendarService.getCalendarStatistics();
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved academic calendar statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get academic calendar statistics", e);
            throw e;
        }
    }

    /**
     * Advanced search for calendar events
     */
    @GetMapping("/search")
    @Operation(summary = "Advanced search", description = "Advanced search for academic calendar events with multiple filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Page<AcademicCalendarResponse>> advancedSearch(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean isHoliday,
            @RequestParam(required = false) Boolean isExamPeriod,
            @RequestParam(required = false) Boolean isTeachingPeriod,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Advanced search for academic calendar events with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        Page<AcademicCalendarResponse> events = calendarService.advancedSearch(
                eventType, academicYear, semester, startDate, endDate, isHoliday, isExamPeriod, isTeachingPeriod, pageable);

        logger.debug("Found {} academic calendar events matching search criteria", events.getTotalElements());
        return ResponseEntity.ok(events);
    }
}