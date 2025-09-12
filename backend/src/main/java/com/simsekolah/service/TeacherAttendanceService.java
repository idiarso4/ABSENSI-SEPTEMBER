package com.simsekolah.service;

import com.simsekolah.dto.request.CreateTeacherAttendanceRequest;
import com.simsekolah.dto.response.TeacherAttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeacherAttendanceService {

    TeacherAttendanceResponse createAttendance(CreateTeacherAttendanceRequest request);

    Page<TeacherAttendanceResponse> getAllAttendances(Pageable pageable);

    Optional<TeacherAttendanceResponse> getAttendanceById(Long attendanceId);

    Page<TeacherAttendanceResponse> getAttendancesByTeacher(Long teacherId, Pageable pageable);

    Page<TeacherAttendanceResponse> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    TeacherAttendanceResponse updateAttendanceStatus(Long attendanceId, String status);

    TeacherAttendanceResponse verifyAttendance(Long attendanceId);

    List<TeacherAttendanceResponse> getUnverifiedAttendances();

    Map<String, Object> getAttendanceStatistics();

    Map<String, Object> getTeacherAttendanceSummary(Long teacherId, LocalDate startDate, LocalDate endDate);

    Page<TeacherAttendanceResponse> advancedSearch(Long teacherId, String status, String attendanceType, LocalDate startDate, LocalDate endDate, Pageable pageable);
}