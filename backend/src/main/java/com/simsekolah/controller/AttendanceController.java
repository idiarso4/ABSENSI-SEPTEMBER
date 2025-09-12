package com.simsekolah.controller;

import com.simsekolah.dto.request.AttendanceReportRequest;
import com.simsekolah.dto.request.BulkAttendanceRequest;
import com.simsekolah.dto.request.CreateAttendanceRequest;
import com.simsekolah.dto.request.UpdateAttendanceRequest;
import com.simsekolah.dto.response.AttendanceReportResponse;
import com.simsekolah.dto.response.AttendanceResponse;
import com.simsekolah.dto.response.StudentMiniResponse;
import com.simsekolah.enums.AttendanceStatus;
import com.simsekolah.service.AttendanceReportService;
import com.simsekolah.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing student attendance and reports.
 */
@RestController
@RequestMapping({"/api/v1/attendance", "/api/attendance", "/api/v1/attendances"})
@Tag(name = "Attendance Management", description = "Endpoints for recording and managing student attendance")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceReportService attendanceReportService;

    // CRUD & Queries (as used by tests)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AttendanceResponse> recordAttendance(@Valid @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse response = attendanceService.recordAttendance(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<AttendanceResponse>> getAllAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(attendanceService.getAllAttendance(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AttendanceResponse> getAttendanceById(@PathVariable("id") Long id) {
        Optional<AttendanceResponse> resp = attendanceService.getAttendanceById(id);
        return resp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AttendanceResponse> updateAttendance(@PathVariable("id") Long id,
                                                               @Valid @RequestBody UpdateAttendanceRequest request) {
        AttendanceResponse response = attendanceService.updateAttendance(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable("id") Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<AttendanceResponse>> getAttendanceByStudent(@PathVariable("studentId") Long studentId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId, pageable));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<AttendanceResponse>> getAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(attendanceService.getAttendanceByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Page<AttendanceResponse>> getAttendanceByStatus(@PathVariable("status") AttendanceStatus status,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(attendanceService.getAttendanceByStatus(status, pageable));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> recordBulkAttendance(@Valid @RequestBody BulkAttendanceRequest request) {
        AttendanceService.BulkAttendanceResult result = attendanceService.bulkRecordAttendance(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk attendance recorded successfully");
        response.put("totalProcessed", result != null ? result.getTotalProcessed() : 0);
        response.put("successCount", result != null ? result.getSuccessCount() : 0);
        response.put("errorCount", result != null ? result.getErrorCount() : 0);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttendanceStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> stats = attendanceService.getAttendanceStatistics(startDate, endDate);
        if (stats == null) stats = new HashMap<>();
        stats.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(stats);
    }

    // Reports endpoints used by tests
    @PostMapping("/reports")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AttendanceReportResponse> generateAttendanceReport(@Valid @RequestBody AttendanceReportRequest request) {
        AttendanceReportResponse response = attendanceReportService.generateAttendanceReport(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reports/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportAttendanceReport(@Valid @RequestBody AttendanceReportRequest request) {
        ByteArrayOutputStream os = attendanceReportService.exportAttendanceReportToExcel(request);
        byte[] bytes = os != null ? os.toByteArray() : new byte[0];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "attendance_report.xlsx");
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @GetMapping("/daily-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getDailyAttendanceSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Object> summary = attendanceService.getDailyAttendanceSummary(date);
        if (summary == null) summary = new HashMap<>();
        summary.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/student/{studentId}/rate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> getStudentAttendanceRate(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double rate = attendanceService.calculateStudentAttendanceRate(studentId, startDate, endDate);
        Map<String, Object> resp = new HashMap<>();
        resp.put("attendanceRate", rate);
        resp.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(resp);
    }

    // Existing teaching activity utilities retained
    @PostMapping("/bulk-record")
    @Operation(summary = "Bulk Record Attendance", description = "Records attendance for multiple students for a specific teaching activity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance recorded successfully. The body contains details of the operation."),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have the required role.")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<AttendanceService.BulkAttendanceResult> bulkRecordAttendance(@Valid @RequestBody BulkAttendanceRequest request) {
        log.info("Bulk record attendance for teaching activity ID: {}", request.getTeachingActivityId());
        AttendanceService.BulkAttendanceResult result = attendanceService.bulkRecordAttendance(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/teaching-activity/{activityId}/pending-students")
    @Operation(summary = "Get Pending Students for Attendance", description = "Retrieves a list of students in a teaching activity who have not yet had their attendance recorded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pending students retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "403", description = "Forbidden. User does not have the required role."),
            @ApiResponse(responseCode = "404", description = "Teaching activity not found.")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<StudentMiniResponse>> getPendingStudentsForAttendance(@PathVariable Long activityId) {
        log.info("Get pending students for teaching activity ID: {}", activityId);
        List<StudentMiniResponse> pendingStudents = attendanceService.getStudentsPendingAttendance(activityId);
        return ResponseEntity.ok(pendingStudents);
    }
}
