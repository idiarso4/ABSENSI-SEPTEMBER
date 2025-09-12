package com.simsekolah.service;

import com.simsekolah.dto.request.*;
import com.simsekolah.dto.response.ScheduleResponse;
import com.simsekolah.dto.response.TimetableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    ScheduleResponse createSchedule(CreateScheduleRequest request);
    ScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest request);
    ScheduleResponse getScheduleById(Long scheduleId);
    void deleteSchedule(Long scheduleId);
    Page<ScheduleResponse> searchSchedules(ScheduleSearchRequest request, Pageable pageable);
    List<ScheduleResponse> createBulkSchedules(BulkScheduleRequest request);
    List<ScheduleResponse> getSchedulesByClassRoom(Long classRoomId, String academicYear, Integer semester);
    List<ScheduleResponse> getSchedulesByTeacher(Long teacherId, String academicYear, Integer semester);
    List<ScheduleResponse> getSchedulesBySubject(Long subjectId, String academicYear, Integer semester);
    TimetableResponse generateClassTimetable(Long classRoomId, String academicYear, Integer semester);
    TimetableResponse generateTeacherTimetable(Long teacherId, String academicYear, Integer semester);
    TimetableResponse generateSubjectTimetable(Long subjectId, String academicYear, Integer semester);
    List<Map<String, Object>> checkScheduleConflicts(CreateScheduleRequest request);
    List<Map<String, Object>> checkScheduleConflicts(Long scheduleId, UpdateScheduleRequest request);
    List<Map<String, Object>> detectExistingConflicts(String academicYear, Integer semester);
    List<Map<String, Object>> getTeacherWeeklySchedule(Long teacherId, String academicYear, Integer semester);
    List<Map<String, Object>> getClassroomWeeklySchedule(Long classRoomId, String academicYear, Integer semester);
    List<Map<String, Object>> getAvailableTimeSlots(Long teacherId, DayOfWeek dayOfWeek, String academicYear, Integer semester);
    List<Map<String, Object>> getAvailableClassroomTimeSlots(Long classRoomId, DayOfWeek dayOfWeek, String academicYear, Integer semester);
    List<ScheduleResponse> generateScheduleSuggestions(Long classRoomId, Long subjectId, Long teacherId, String academicYear, Integer semester);
    List<ScheduleResponse> resolveScheduleConflicts(List<Long> conflictingScheduleIds, String resolutionStrategy);
    Map<String, Object> getScheduleStatistics(String academicYear, Integer semester);
    Map<String, Object> getTeacherWorkloadAnalysis(Long teacherId, String academicYear, Integer semester);
    Map<String, Object> getClassroomUtilizationAnalysis(Long classRoomId, String academicYear, Integer semester);
    Map<String, Object> generateScheduleOptimizationReport(String academicYear, Integer semester);
    List<ScheduleResponse> cloneSchedule(String fromAcademicYear, Integer fromSemester, String toAcademicYear, Integer toSemester);
    List<ScheduleResponse> cloneClassSchedule(Long fromClassRoomId, Long toClassRoomId, String academicYear, Integer semester);
    void archiveOldSchedules(String academicYear);
    Map<String, Object> getScheduleConflictsSummary(String academicYear, Integer semester);
    void generateScheduleChangeNotifications(Long scheduleId, String changeType, String changeDescription);
    List<Map<String, Object>> getFreePeriods(Long classRoomId, DayOfWeek dayOfWeek, String academicYear, Integer semester);
    List<Map<String, Object>> getTeacherFreePeriods(Long teacherId, DayOfWeek dayOfWeek, String academicYear, Integer semester);
    Map<String, Object> generateScheduleDensityReport(String academicYear, Integer semester);
    Map<String, Object> getPeakHoursAnalysis(String academicYear, Integer semester);
    Map<String, Object> generateScheduleEfficiencyMetrics(String academicYear, Integer semester);
    List<Map<String, Object>> getScheduleChangeHistory(Long scheduleId);
    List<Map<String, Object>> getScheduleTemplates();
    List<ScheduleResponse> createScheduleFromTemplate(Long templateId, Long classRoomId, String academicYear, Integer semester);
    byte[] exportSchedule(Long classRoomId, String academicYear, Integer semester, String format);
    List<ScheduleResponse> importSchedule(byte[] fileData, String academicYear, Integer semester);
    Map<String, Object> generateScheduleComparisonReport(String academicYear1, Integer semester1, String academicYear2, Integer semester2);
}
