package com.simsekolah.service;

import com.simsekolah.dto.request.CreatePklAttendanceRequest;
import com.simsekolah.dto.request.UpdatePklAttendanceRequest;
import com.simsekolah.dto.response.PklAttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for PKL attendance management operations
 */
public interface PklAttendanceService {

    /**
     * Create a new PKL attendance record
     */
    PklAttendanceResponse createAttendance(CreatePklAttendanceRequest request);

    /**
     * Update an existing PKL attendance record
     */
    PklAttendanceResponse updateAttendance(Long attendanceId, UpdatePklAttendanceRequest request);

    /**
     * Get attendance by ID
     */
    Optional<PklAttendanceResponse> getAttendanceById(Long attendanceId);

    /**
     * Get all attendances with pagination
     */
    Page<PklAttendanceResponse> getAllAttendances(Pageable pageable);

    /**
     * Get attendances by student ID
     */
    Page<PklAttendanceResponse> getAttendancesByStudent(Long studentId, Pageable pageable);

    /**
     * Get attendances by teacher ID
     */
    Page<PklAttendanceResponse> getAttendancesByTeacher(Long teacherId, Pageable pageable);

    /**
     * Get attendances by date range
     */
    Page<PklAttendanceResponse> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get attendances by status
     */
    Page<PklAttendanceResponse> getAttendancesByStatus(String status, Pageable pageable);

    /**
     * Delete attendance by ID
     */
    void deleteAttendance(Long attendanceId);

    /**
     * Verify attendance by teacher
     */
    PklAttendanceResponse verifyAttendance(Long attendanceId, Long teacherId);

    /**
     * Get unverified attendances
     */
    List<PklAttendanceResponse> getUnverifiedAttendances();

    /**
     * Get attendance statistics
     */
    Map<String, Object> getAttendanceStatistics();

    /**
     * Get student attendance summary
     */
    Map<String, Object> getStudentAttendanceSummary(Long studentId, LocalDate startDate, LocalDate endDate);

    /**
     * Check if attendance exists for student on date
     */
    boolean existsByStudentAndDate(Long studentId, LocalDate date);

    /**
     * Bulk create attendances
     */
    List<PklAttendanceResponse> bulkCreateAttendances(List<CreatePklAttendanceRequest> requests);

    /**
     * Get monthly attendance statistics
     */
    List<Map<String, Object>> getMonthlyAttendanceStatistics();

    /**
     * Advanced search for attendances
     */
    Page<PklAttendanceResponse> advancedSearch(Long studentId, Long teacherId, LocalDate startDate,
                                              LocalDate endDate, String companyName, String status, Pageable pageable);
}