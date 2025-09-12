package com.simsekolah.controller;

import com.simsekolah.service.AttendanceCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST controller for attendance calendar management operations
 * Provides endpoints for attendance-calendar (attendance calendar views)
 */
@RestController
@RequestMapping({"/api/v1/attendance-calendar", "/api/attendance-calendar"})
@Tag(name = "Attendance Calendar Management", description = "Attendance calendar management endpoints")
@Validated
public class AttendanceCalendarController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceCalendarController.class);

    @Autowired
    private AttendanceCalendarService calendarService;

    /**
     * Get attendance calendar for a specific month
     */
    @GetMapping("/month/{year}/{month}")
    @Operation(summary = "Get attendance calendar for month", description = "Get attendance calendar data for a specific month and year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calendar data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceCalendarForMonth(
            @PathVariable("year") @NotNull Integer year,
            @PathVariable("month") @NotNull @Min(1) @Parameter(description = "Month (1-12)") Integer month) {

        logger.debug("Fetching attendance calendar for {}/{}", year, month);

        try {
            Map<String, Object> calendarData = calendarService.getAttendanceCalendarForMonth(year, month);
            calendarData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar for {}/{}", year, month);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get student attendance calendar
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student attendance calendar", description = "Get attendance calendar for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student calendar retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> getStudentAttendanceCalendar(
            @PathVariable("studentId") @NotNull Long studentId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar for student: {} for {}/{}", studentId, year, month);

        try {
            Map<String, Object> calendarData = calendarService.getStudentAttendanceCalendar(studentId, year, month);
            calendarData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar for student: {}", studentId);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar for student: {}", studentId, e);
            throw e;
        }
    }

    /**
     * Get class attendance calendar
     */
    @GetMapping("/class/{classRoomId}")
    @Operation(summary = "Get class attendance calendar", description = "Get attendance calendar for a specific class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Class calendar retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getClassAttendanceCalendar(
            @PathVariable("classRoomId") @NotNull Long classRoomId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar for class: {} for {}/{}", classRoomId, year, month);

        try {
            Map<String, Object> calendarData = calendarService.getClassAttendanceCalendar(classRoomId, year, month);
            calendarData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar for class: {}", classRoomId);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar for class: {}", classRoomId, e);
            throw e;
        }
    }

    /**
     * Get teacher attendance calendar
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get teacher attendance calendar", description = "Get attendance calendar for a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teacher calendar retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getTeacherAttendanceCalendar(
            @PathVariable("teacherId") @NotNull Long teacherId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar for teacher: {} for {}/{}", teacherId, year, month);

        try {
            Map<String, Object> calendarData = calendarService.getTeacherAttendanceCalendar(teacherId, year, month);
            calendarData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar for teacher: {}", teacherId);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar for teacher: {}", teacherId, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get attendance calendar summary", description = "Get attendance calendar summary for current month")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceCalendarSummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar summary for {}/{}", year, month);

        try {
            Map<String, Object> summary = calendarService.getAttendanceCalendarSummary(year, month);
            summary.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar summary for {}/{}", year, month);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar summary for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance trends for calendar
     */
    @GetMapping("/trends")
    @Operation(summary = "Get attendance trends", description = "Get attendance trends data for calendar visualization")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trends retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceTrends(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month,
            @RequestParam(defaultValue = "30") @Min(1) Integer days) {

        logger.debug("Fetching attendance trends for {}/{} ({} days)", year, month, days);

        try {
            Map<String, Object> trends = calendarService.getAttendanceTrends(year, month, days);
            trends.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance trends for {}/{}", year, month);
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            logger.error("Failed to get attendance trends for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar heatmap data
     */
    @GetMapping("/heatmap")
    @Operation(summary = "Get attendance heatmap", description = "Get attendance data formatted for calendar heatmap")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Heatmap data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceHeatmap(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance heatmap for {}/{}", year, month);

        try {
            Map<String, Object> heatmap = calendarService.getAttendanceHeatmap(year, month);
            heatmap.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance heatmap for {}/{}", year, month);
            return ResponseEntity.ok(heatmap);
        } catch (Exception e) {
            logger.error("Failed to get attendance heatmap for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get attendance by date range", description = "Get attendance calendar data for a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Date range data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        logger.debug("Fetching attendance calendar for date range: {} to {}", startDate, endDate);

        try {
            Map<String, Object> calendarData = calendarService.getAttendanceByDateRange(startDate, endDate);
            calendarData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar for date range");
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar for date range: {} to {}", startDate, endDate, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get attendance calendar statistics", description = "Get attendance calendar statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceCalendarStatistics(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar statistics for {}/{}", year, month);

        try {
            Map<String, Object> statistics = calendarService.getAttendanceCalendarStatistics(year, month);
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar statistics for {}/{}", year, month);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar statistics for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance patterns for calendar
     */
    @GetMapping("/patterns")
    @Operation(summary = "Get attendance patterns", description = "Get attendance patterns for calendar analysis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patterns retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendancePatterns(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance patterns for {}/{}", year, month);

        try {
            Map<String, Object> patterns = calendarService.getAttendancePatterns(year, month);
            patterns.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance patterns for {}/{}", year, month);
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            logger.error("Failed to get attendance patterns for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar export data
     */
    @GetMapping("/export")
    @Operation(summary = "Get attendance calendar export", description = "Get attendance calendar data formatted for export")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Export data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceCalendarExport(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month,
            @RequestParam(defaultValue = "EXCEL") String format) {

        logger.debug("Fetching attendance calendar export for {}/{} in format: {}", year, month, format);

        try {
            Map<String, Object> exportData = calendarService.getAttendanceCalendarExport(year, month, format);
            exportData.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved attendance calendar export for {}/{}", year, month);
            return ResponseEntity.ok(exportData);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar export for {}/{}", year, month, e);
            throw e;
        }
    }

    /**
     * Get attendance calendar alerts
     */
    @GetMapping("/alerts")
    @Operation(summary = "Get attendance calendar alerts", description = "Get attendance alerts for calendar display")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Map<String, Object>>> getAttendanceCalendarAlerts(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") @Min(1) Integer month) {

        logger.debug("Fetching attendance calendar alerts for {}/{}", year, month);

        try {
            List<Map<String, Object>> alerts = calendarService.getAttendanceCalendarAlerts(year, month);

            logger.debug("Retrieved {} attendance calendar alerts for {}/{}", alerts.size(), year, month);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            logger.error("Failed to get attendance calendar alerts for {}/{}", year, month, e);
            throw e;
        }
    }
}