package com.simsekolah.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceCalendarService {

    Map<String, Object> getAttendanceCalendarForMonth(Integer year, Integer month);

    Map<String, Object> getStudentAttendanceCalendar(Long studentId, Integer year, Integer month);

    Map<String, Object> getClassAttendanceCalendar(Long classRoomId, Integer year, Integer month);

    Map<String, Object> getTeacherAttendanceCalendar(Long teacherId, Integer year, Integer month);

    Map<String, Object> getAttendanceCalendarSummary(Integer year, Integer month);

    Map<String, Object> getAttendanceTrends(Integer year, Integer month, Integer days);

    Map<String, Object> getAttendanceHeatmap(Integer year, Integer month);

    Map<String, Object> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate);

    Map<String, Object> getAttendanceCalendarStatistics(Integer year, Integer month);

    Map<String, Object> getAttendancePatterns(Integer year, Integer month);

    Map<String, Object> getAttendanceCalendarExport(Integer year, Integer month, String format);

    List<Map<String, Object>> getAttendanceCalendarAlerts(Integer year, Integer month);
}