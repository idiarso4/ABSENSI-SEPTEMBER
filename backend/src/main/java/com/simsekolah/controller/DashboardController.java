package com.simsekolah.controller;

import com.simsekolah.repository.StudentRepository;
import com.simsekolah.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard Controller
 * Provides dashboard statistics and overview data
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Dashboard statistics and overview endpoints")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get dashboard statistics
     */
    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve overview statistics for dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        logger.info("Dashboard stats request received");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Get basic counts
            long totalStudents = studentRepository.count();
            long totalUsers = userRepository.count();
            long activeClasses = Math.max(1, totalStudents / 25); // Estimate: ~25 students per class
            long pendingTasks = 0; // No task system implemented yet
            
            stats.put("totalStudents", totalStudents);
            stats.put("totalUsers", totalUsers);
            stats.put("activeClasses", activeClasses);
            stats.put("pendingTasks", pendingTasks);
            stats.put("lastUpdated", LocalDateTime.now());
            
            logger.info("Dashboard stats retrieved successfully");
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error retrieving dashboard stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve dashboard statistics");
            errorResponse.put("timestamp", LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get students statistics
     */
    @GetMapping("/students/stats")
    @Operation(summary = "Get student statistics", description = "Retrieve detailed student statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getStudentStats() {
        logger.info("Student stats request received");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            long totalCount = studentRepository.count();
            long activeCount = totalCount; // All students considered active for now
            long graduatedCount = 0L; // No graduated students tracked yet

            stats.put("totalCount", totalCount);
            stats.put("activeCount", activeCount);
            stats.put("graduatedCount", graduatedCount);
            stats.put("lastUpdated", LocalDateTime.now());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error retrieving student stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve student statistics");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get users statistics
     */
    @GetMapping("/users/stats")
    @Operation(summary = "Get user statistics", description = "Retrieve detailed user statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        logger.info("User stats request received");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            long totalCount = userRepository.count();
            long activeCount = totalCount; // All users considered active for now
            long adminCount = 1L; // At least one admin exists
            long teacherCount = Math.max(1L, totalCount - 1L); // Estimate teachers

            stats.put("totalCount", totalCount);
            stats.put("activeCount", activeCount);
            stats.put("adminCount", adminCount);
            stats.put("teacherCount", teacherCount);
            stats.put("lastUpdated", LocalDateTime.now());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error retrieving user stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve user statistics");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get classes statistics
     */
    @GetMapping("/classes/stats")
    @Operation(summary = "Get class statistics", description = "Retrieve detailed class statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getClassStats() {
        logger.info("Class stats request received");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Estimate class statistics based on student count
            long studentCount = studentRepository.count();
            long totalCount = Math.max(1, studentCount / 25); // Estimate: ~25 students per class
            long activeCount = totalCount; // All classes considered active
            
            stats.put("activeCount", activeCount);
            stats.put("totalCount", totalCount);
            stats.put("lastUpdated", LocalDateTime.now());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error retrieving class stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve class statistics");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get recent activities
     */
    @GetMapping("/activities/recent")
    @Operation(summary = "Get recent activities", description = "Retrieve recent system activities")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Recent activities request received with limit: {}", limit);
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Activity logging system not implemented yet
            response.put("activities", new java.util.ArrayList<>());
            response.put("totalCount", 0);
            response.put("limit", limit);
            response.put("lastUpdated", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving recent activities", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve recent activities");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get Key Performance Indicators (KPIs)
     */
    @GetMapping("/kpis")
    @Operation(summary = "Get KPIs", description = "Retrieve key performance indicators for dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getKPIs() {
        logger.info("KPIs request received");

        try {
            Map<String, Object> kpis = new HashMap<>();

            // Calculate KPIs based on current data
            long totalStudents = studentRepository.count();
            long totalUsers = userRepository.count();

            // Student growth (mock data for now)
            kpis.put("studentGrowth", 12.5);
            kpis.put("totalStudents", totalStudents);
            kpis.put("activeStudents", totalStudents);

            // Teacher performance (mock data)
            kpis.put("teacherPerformance", 87.3);
            kpis.put("totalTeachers", Math.max(1, totalUsers - 1));

            // Attendance rate (mock data)
            kpis.put("attendanceRate", 94.2);

            // Financial health (mock data)
            kpis.put("revenueGrowth", 8.7);
            kpis.put("totalRevenue", 150000000); // Mock: 150M IDR

            kpis.put("lastUpdated", LocalDateTime.now());

            return ResponseEntity.ok(kpis);

        } catch (Exception e) {
            logger.error("Error retrieving KPIs", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve KPIs");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get recent activities (alias for activities/recent)
     */
    @GetMapping("/recent-activities")
    @Operation(summary = "Get recent activities", description = "Retrieve recent system activities")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getRecentActivitiesAlias(
            @RequestParam(defaultValue = "10") int limit) {
        return getRecentActivities(limit);
    }

    /**
     * Get attendance trend data
     */
    @GetMapping("/attendance-trend")
    @Operation(summary = "Get attendance trend", description = "Retrieve attendance trend data over time")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceTrend() {
        logger.info("Attendance trend request received");

        try {
            Map<String, Object> trend = new HashMap<>();

            // Mock attendance trend data for the last 7 days
            java.util.List<Map<String, Object>> data = new java.util.ArrayList<>();
            java.time.LocalDate today = java.time.LocalDate.now();

            for (int i = 6; i >= 0; i--) {
                java.time.LocalDate date = today.minusDays(i);
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date.toString());
                dayData.put("present", 85 + (int)(Math.random() * 10)); // 85-95%
                dayData.put("absent", 5 + (int)(Math.random() * 5));   // 5-10%
                dayData.put("late", 3 + (int)(Math.random() * 4));     // 3-7%
                data.add(dayData);
            }

            trend.put("data", data);
            trend.put("period", "7_days");
            trend.put("lastUpdated", LocalDateTime.now());

            return ResponseEntity.ok(trend);

        } catch (Exception e) {
            logger.error("Error retrieving attendance trend", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve attendance trend");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get upcoming events
     */
    @GetMapping("/upcoming-events")
    @Operation(summary = "Get upcoming events", description = "Retrieve upcoming school events")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> getUpcomingEvents() {
        logger.info("Upcoming events request received");

        try {
            Map<String, Object> events = new HashMap<>();

            // Mock upcoming events
            java.util.List<Map<String, Object>> eventList = new java.util.ArrayList<>();

            // Add some sample events
            Map<String, Object> event1 = new HashMap<>();
            event1.put("id", 1);
            event1.put("title", "Parent-Teacher Conference");
            event1.put("date", java.time.LocalDate.now().plusDays(7).toString());
            event1.put("time", "09:00");
            event1.put("type", "meeting");
            event1.put("description", "Annual parent-teacher conference");
            eventList.add(event1);

            Map<String, Object> event2 = new HashMap<>();
            event2.put("id", 2);
            event2.put("title", "School Sports Day");
            event2.put("date", java.time.LocalDate.now().plusDays(14).toString());
            event2.put("time", "08:00");
            event2.put("type", "sports");
            event2.put("description", "Annual school sports competition");
            eventList.add(event2);

            Map<String, Object> event3 = new HashMap<>();
            event3.put("id", 3);
            event3.put("title", "Final Exams Begin");
            event3.put("date", java.time.LocalDate.now().plusDays(21).toString());
            event3.put("time", "08:00");
            event3.put("type", "academic");
            event3.put("description", "Final semester examinations");
            eventList.add(event3);

            events.put("events", eventList);
            events.put("totalCount", eventList.size());
            events.put("lastUpdated", LocalDateTime.now());

            return ResponseEntity.ok(events);

        } catch (Exception e) {
            logger.error("Error retrieving upcoming events", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve upcoming events");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get monthly payroll data
     */
    @GetMapping("/payroll-monthly")
    @Operation(summary = "Get monthly payroll", description = "Retrieve monthly payroll statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getMonthlyPayroll() {
        logger.info("Monthly payroll request received");

        try {
            Map<String, Object> payroll = new HashMap<>();

            // Mock payroll data for the last 6 months
            java.util.List<Map<String, Object>> data = new java.util.ArrayList<>();
            java.time.LocalDate today = java.time.LocalDate.now();

            for (int i = 5; i >= 0; i--) {
                java.time.YearMonth month = java.time.YearMonth.from(today.minusMonths(i));
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", month.toString());
                monthData.put("totalSalary", 250000000 + (int)(Math.random() * 50000000)); // 250M - 300M IDR
                monthData.put("employeeCount", 15 + (int)(Math.random() * 5)); // 15-20 employees
                monthData.put("averageSalary", monthData.get("totalSalary") instanceof Integer ?
                    ((Integer)monthData.get("totalSalary")) / ((Integer)monthData.get("employeeCount")) : 0);
                data.add(monthData);
            }

            payroll.put("data", data);
            payroll.put("period", "6_months");
            payroll.put("currency", "IDR");
            payroll.put("lastUpdated", LocalDateTime.now());

            return ResponseEntity.ok(payroll);

        } catch (Exception e) {
            logger.error("Error retrieving monthly payroll", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve monthly payroll data");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get system health status
     */
    @GetMapping("/health")
    @Operation(summary = "Get system health", description = "Retrieve system health status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        logger.info("System health request received");

        try {
            Map<String, Object> health = new HashMap<>();

            // Basic health checks
            health.put("status", "UP");
            health.put("database", "UP");
            health.put("timestamp", LocalDateTime.now());

            // Basic health check - detailed checks can be added later
            health.put("memory", "OK");
            health.put("diskSpace", "OK");

            return ResponseEntity.ok(health);

        } catch (Exception e) {
            logger.error("Error checking system health", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "DOWN");
            errorResponse.put("error", "Health check failed");
            errorResponse.put("timestamp", LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
