package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateScheduleRequest;
import com.simsekolah.dto.request.UpdateScheduleRequest;
import com.simsekolah.dto.response.ScheduleResponse;
import com.simsekolah.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        // Use search with empty criteria to get all schedules
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<ScheduleResponse> page = scheduleService.searchSchedules(null, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
        try {
            ScheduleResponse schedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody CreateScheduleRequest request) {
        try {
            ScheduleResponse createdSchedule = scheduleService.createSchedule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id, @Valid @RequestBody UpdateScheduleRequest request) {
        try {
            ScheduleResponse updatedSchedule = scheduleService.updateSchedule(id, request);
            return ResponseEntity.ok(updatedSchedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByClass(
            @PathVariable Long classId,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByClassRoom(classId, academicYear, semester);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByTeacher(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByTeacher(teacherId, academicYear, semester);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/class/{classId}/day/{dayOfWeek}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByClassAndDay(
            @PathVariable Long classId,
            @PathVariable java.time.DayOfWeek dayOfWeek,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        // Use getSchedulesByClassRoom and filter by day
        List<ScheduleResponse> allSchedules = scheduleService.getSchedulesByClassRoom(classId, academicYear, semester);
        List<ScheduleResponse> filteredSchedules = allSchedules.stream()
                .filter(schedule -> schedule.getDayOfWeek().equals(dayOfWeek))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(filteredSchedules);
    }

    @GetMapping("/teacher/{teacherId}/day/{dayOfWeek}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByTeacherAndDay(
            @PathVariable Long teacherId,
            @PathVariable java.time.DayOfWeek dayOfWeek,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        // Use getSchedulesByTeacher and filter by day
        List<ScheduleResponse> allSchedules = scheduleService.getSchedulesByTeacher(teacherId, academicYear, semester);
        List<ScheduleResponse> filteredSchedules = allSchedules.stream()
                .filter(schedule -> schedule.getDayOfWeek().equals(dayOfWeek))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(filteredSchedules);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesBySubject(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesBySubject(subjectId, academicYear, semester);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/day/{dayOfWeek}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByDay(
            @PathVariable java.time.DayOfWeek dayOfWeek,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        // Use search with day filter
        var searchRequest = new com.simsekolah.dto.request.ScheduleSearchRequest();
        searchRequest.setDayOfWeek(dayOfWeek);
        searchRequest.setAcademicYear(academicYear);
        searchRequest.setSemester(semester);
        Page<ScheduleResponse> page = scheduleService.searchSchedules(searchRequest, PageRequest.of(0, Integer.MAX_VALUE));
        return ResponseEntity.ok(page.getContent());
    }

    // Conflict checking endpoints
    @PostMapping("/check-conflicts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<String> checkScheduleConflicts(@Valid @RequestBody CreateScheduleRequest request) {
        try {
            // Check for conflicts without creating the schedule
            List<Map<String, Object>> conflicts = scheduleService.checkScheduleConflicts(request);
            if (conflicts.isEmpty()) {
                return ResponseEntity.ok("No conflicts detected");
            } else {
                return ResponseEntity.badRequest().body("Conflict detected: " + conflicts.size() + " conflicts found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking conflicts: " + e.getMessage());
        }
    }

    @GetMapping("/class/{classId}/weekly")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getWeeklyScheduleByClass(
            @PathVariable Long classId,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        // Get all schedules for the class (all days)
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByClassRoom(classId, academicYear, semester);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/teacher/{teacherId}/weekly")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<ScheduleResponse>> getWeeklyScheduleByTeacher(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "2024/2025") String academicYear,
            @RequestParam(defaultValue = "1") Integer semester) {
        // Get all schedules for the teacher (all days)
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByTeacher(teacherId, academicYear, semester);
        return ResponseEntity.ok(schedules);
    }
}
