package com.simsekolah.service;

import com.simsekolah.dto.request.CreateDutyTeacherReportRequest;
import com.simsekolah.dto.response.DutyTeacherReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for duty teacher report management operations
 */
public interface DutyTeacherReportService {

    /**
     * Create a new duty teacher report
     */
    DutyTeacherReportResponse createReport(CreateDutyTeacherReportRequest request);

    /**
     * Update an existing duty teacher report
     */
    DutyTeacherReportResponse updateReport(Long reportId, CreateDutyTeacherReportRequest request);

    /**
     * Get report by ID
     */
    Optional<DutyTeacherReportResponse> getReportById(Long reportId);

    /**
     * Get all reports with pagination
     */
    Page<DutyTeacherReportResponse> getAllReports(Pageable pageable);

    /**
     * Get reports by duty teacher ID
     */
    Page<DutyTeacherReportResponse> getReportsByTeacher(Long teacherId, Pageable pageable);

    /**
     * Get reports by date range
     */
    Page<DutyTeacherReportResponse> getReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get reports by status
     */
    Page<DutyTeacherReportResponse> getReportsByStatus(String status, Pageable pageable);

    /**
     * Delete report by ID
     */
    void deleteReport(Long reportId);

    /**
     * Submit report for approval
     */
    DutyTeacherReportResponse submitReport(Long reportId);

    /**
     * Approve report by admin
     */
    DutyTeacherReportResponse approveReport(Long reportId, Long adminId, String approvalNotes);

    /**
     * Reject report by admin
     */
    DutyTeacherReportResponse rejectReport(Long reportId, Long adminId, String rejectionReason);

    /**
     * Get pending approval reports
     */
    List<DutyTeacherReportResponse> getPendingApprovalReports();

    /**
     * Get approved reports
     */
    List<DutyTeacherReportResponse> getApprovedReports();

    /**
     * Get rejected reports
     */
    List<DutyTeacherReportResponse> getRejectedReports();

    /**
     * Get report statistics
     */
    Map<String, Object> getReportStatistics();

    /**
     * Get today's report for a teacher
     */
    Optional<DutyTeacherReportResponse> getTodaysReport(Long teacherId);

    /**
     * Check if report exists for teacher on date
     */
    boolean existsByTeacherAndDate(Long teacherId, LocalDate date);

    /**
     * Get monthly report statistics
     */
    List<Map<String, Object>> getMonthlyReportStatistics();

    /**
     * Advanced search for reports
     */
    Page<DutyTeacherReportResponse> advancedSearch(Long teacherId, Long adminId, LocalDate startDate,
                                                  LocalDate endDate, String status, Pageable pageable);
}