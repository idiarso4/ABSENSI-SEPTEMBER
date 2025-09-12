package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateDutyTeacherReportRequest;
import com.simsekolah.dto.response.DutyTeacherReportResponse;
import com.simsekolah.entity.DutyTeacherReport;
import com.simsekolah.entity.User;
import com.simsekolah.repository.DutyTeacherReportRepository;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.service.DutyTeacherReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of DutyTeacherReportService
 */
@Service
@Transactional
public class DutyTeacherReportServiceImpl implements DutyTeacherReportService {

    private static final Logger logger = LoggerFactory.getLogger(DutyTeacherReportServiceImpl.class);

    @Autowired
    private DutyTeacherReportRepository dutyTeacherReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DutyTeacherReportResponse createReport(CreateDutyTeacherReportRequest request) {
        logger.info("Creating duty teacher report for teacher ID: {}", request.getDutyTeacherId());

        // Validate teacher exists
        User teacher = userRepository.findById(request.getDutyTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + request.getDutyTeacherId()));

        // Check if report already exists for this teacher on this date
        if (dutyTeacherReportRepository.existsByDutyTeacherIdAndReportDate(request.getDutyTeacherId(), request.getReportDate())) {
            throw new RuntimeException("Report already exists for teacher on date: " + request.getReportDate());
        }

        // Create report record
        DutyTeacherReport report = new DutyTeacherReport();
        report.setDutyTeacher(teacher);
        report.setReportDate(request.getReportDate());
        report.setShiftStartTime(request.getShiftStartTime());
        report.setShiftEndTime(request.getShiftEndTime());
        report.setTotalStudentsPresent(request.getTotalStudentsPresent());
        report.setTotalStudentsAbsent(request.getTotalStudentsAbsent());
        report.setTotalPermissionsApproved(request.getTotalPermissionsApproved());
        report.setTotalPermissionsRejected(request.getTotalPermissionsRejected());
        report.setTotalLateArrivals(request.getTotalLateArrivals());
        report.setTotalEarlyDepartures(request.getTotalEarlyDepartures());
        report.setIncidentsReported(request.getIncidentsReported());
        report.setActionsTaken(request.getActionsTaken());
        report.setSchoolConditionNotes(request.getSchoolConditionNotes());
        report.setHandoverNotes(request.getHandoverNotes());
        report.setNextDutyTeacher(request.getNextDutyTeacher());

        DutyTeacherReport savedReport = dutyTeacherReportRepository.save(report);
        logger.info("Successfully created duty teacher report with ID: {}", savedReport.getId());

        return mapToResponse(savedReport);
    }

    @Override
    public DutyTeacherReportResponse updateReport(Long reportId, CreateDutyTeacherReportRequest request) {
        logger.info("Updating duty teacher report with ID: {}", reportId);

        DutyTeacherReport report = dutyTeacherReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));

        // Update fields if provided
        if (request.getReportDate() != null) {
            report.setReportDate(request.getReportDate());
        }
        if (request.getShiftStartTime() != null) {
            report.setShiftStartTime(request.getShiftStartTime());
        }
        if (request.getShiftEndTime() != null) {
            report.setShiftEndTime(request.getShiftEndTime());
        }
        if (request.getTotalStudentsPresent() != null) {
            report.setTotalStudentsPresent(request.getTotalStudentsPresent());
        }
        if (request.getTotalStudentsAbsent() != null) {
            report.setTotalStudentsAbsent(request.getTotalStudentsAbsent());
        }
        if (request.getTotalPermissionsApproved() != null) {
            report.setTotalPermissionsApproved(request.getTotalPermissionsApproved());
        }
        if (request.getTotalPermissionsRejected() != null) {
            report.setTotalPermissionsRejected(request.getTotalPermissionsRejected());
        }
        if (request.getTotalLateArrivals() != null) {
            report.setTotalLateArrivals(request.getTotalLateArrivals());
        }
        if (request.getTotalEarlyDepartures() != null) {
            report.setTotalEarlyDepartures(request.getTotalEarlyDepartures());
        }
        if (request.getIncidentsReported() != null) {
            report.setIncidentsReported(request.getIncidentsReported());
        }
        if (request.getActionsTaken() != null) {
            report.setActionsTaken(request.getActionsTaken());
        }
        if (request.getSchoolConditionNotes() != null) {
            report.setSchoolConditionNotes(request.getSchoolConditionNotes());
        }
        if (request.getHandoverNotes() != null) {
            report.setHandoverNotes(request.getHandoverNotes());
        }
        if (request.getNextDutyTeacher() != null) {
            report.setNextDutyTeacher(request.getNextDutyTeacher());
        }

        DutyTeacherReport updatedReport = dutyTeacherReportRepository.save(report);
        logger.info("Successfully updated duty teacher report with ID: {}", reportId);

        return mapToResponse(updatedReport);
    }

    @Override
    public Optional<DutyTeacherReportResponse> getReportById(Long reportId) {
        logger.debug("Fetching duty teacher report by ID: {}", reportId);

        return dutyTeacherReportRepository.findById(reportId)
                .map(this::mapToResponse);
    }

    @Override
    public Page<DutyTeacherReportResponse> getAllReports(Pageable pageable) {
        logger.debug("Fetching all duty teacher reports");

        return dutyTeacherReportRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<DutyTeacherReportResponse> getReportsByTeacher(Long teacherId, Pageable pageable) {
        logger.debug("Fetching duty teacher reports for teacher ID: {}", teacherId);

        return dutyTeacherReportRepository.findByDutyTeacherId(teacherId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<DutyTeacherReportResponse> getReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        logger.debug("Fetching duty teacher reports between {} and {}", startDate, endDate);

        return dutyTeacherReportRepository.findByReportDateBetween(startDate, endDate, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<DutyTeacherReportResponse> getReportsByStatus(String status, Pageable pageable) {
        logger.debug("Fetching duty teacher reports by status: {}", status);

        try {
            DutyTeacherReport.ReportStatus reportStatus = DutyTeacherReport.ReportStatus.valueOf(status.toUpperCase());
            return dutyTeacherReportRepository.findByReportStatus(reportStatus, pageable)
                    .map(this::mapToResponse);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid report status: " + status);
        }
    }

    @Override
    public void deleteReport(Long reportId) {
        logger.info("Deleting duty teacher report with ID: {}", reportId);

        if (!dutyTeacherReportRepository.existsById(reportId)) {
            throw new RuntimeException("Report not found with ID: " + reportId);
        }

        dutyTeacherReportRepository.deleteById(reportId);
        logger.info("Successfully deleted duty teacher report with ID: {}", reportId);
    }

    @Override
    public DutyTeacherReportResponse submitReport(Long reportId) {
        logger.info("Submitting duty teacher report: {}", reportId);

        DutyTeacherReport report = dutyTeacherReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));

        if (report.getReportStatus() != DutyTeacherReport.ReportStatus.DRAFT) {
            throw new RuntimeException("Report is not in draft status");
        }

        report.setReportStatus(DutyTeacherReport.ReportStatus.SUBMITTED);

        DutyTeacherReport submittedReport = dutyTeacherReportRepository.save(report);
        logger.info("Successfully submitted duty teacher report: {}", reportId);

        return mapToResponse(submittedReport);
    }

    @Override
    public DutyTeacherReportResponse approveReport(Long reportId, Long adminId, String approvalNotes) {
        logger.info("Approving duty teacher report {} by admin {}", reportId, adminId);

        DutyTeacherReport report = dutyTeacherReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        report.setReportStatus(DutyTeacherReport.ReportStatus.APPROVED);
        report.setApprovedByAdmin(admin);
        report.setApprovalNotes(approvalNotes);

        DutyTeacherReport approvedReport = dutyTeacherReportRepository.save(report);
        logger.info("Successfully approved duty teacher report {} by admin {}", reportId, adminId);

        return mapToResponse(approvedReport);
    }

    @Override
    public DutyTeacherReportResponse rejectReport(Long reportId, Long adminId, String rejectionReason) {
        logger.info("Rejecting duty teacher report {} by admin {}", reportId, adminId);

        DutyTeacherReport report = dutyTeacherReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        report.setReportStatus(DutyTeacherReport.ReportStatus.REJECTED);
        report.setApprovedByAdmin(admin);
        report.setApprovalNotes(rejectionReason);

        DutyTeacherReport rejectedReport = dutyTeacherReportRepository.save(report);
        logger.info("Successfully rejected duty teacher report {} by admin {}", reportId, adminId);

        return mapToResponse(rejectedReport);
    }

    @Override
    public List<DutyTeacherReportResponse> getPendingApprovalReports() {
        logger.debug("Fetching pending approval duty teacher reports");

        return dutyTeacherReportRepository.findPendingApprovalReports()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DutyTeacherReportResponse> getApprovedReports() {
        logger.debug("Fetching approved duty teacher reports");

        return dutyTeacherReportRepository.findApprovedReports()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DutyTeacherReportResponse> getRejectedReports() {
        logger.debug("Fetching rejected duty teacher reports");

        return dutyTeacherReportRepository.findRejectedReports()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getReportStatistics() {
        logger.debug("Fetching duty teacher report statistics");

        List<Object[]> statusStats = dutyTeacherReportRepository.getReportStatisticsByStatus();
        List<Object[]> monthlyStats = dutyTeacherReportRepository.getMonthlyReportStatistics();

        Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("statusDistribution", statusStats);
        statistics.put("monthlyTrends", monthlyStats);
        statistics.put("totalReports", dutyTeacherReportRepository.count());

        return statistics;
    }

    @Override
    public Optional<DutyTeacherReportResponse> getTodaysReport(Long teacherId) {
        logger.debug("Fetching today's duty teacher report for teacher: {}", teacherId);

        return dutyTeacherReportRepository.findByDutyTeacherIdAndReportDate(teacherId, LocalDate.now())
                .map(this::mapToResponse);
    }

    @Override
    public boolean existsByTeacherAndDate(Long teacherId, LocalDate date) {
        return dutyTeacherReportRepository.existsByDutyTeacherIdAndReportDate(teacherId, date);
    }

    @Override
    public List<Map<String, Object>> getMonthlyReportStatistics() {
        logger.debug("Fetching monthly duty teacher report statistics");

        return dutyTeacherReportRepository.getMonthlyReportStatistics()
                .stream()
                .map(row -> {
                    Map<String, Object> stat = new java.util.HashMap<>();
                    stat.put("month", row[0]);
                    stat.put("count", row[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<DutyTeacherReportResponse> advancedSearch(Long teacherId, Long adminId, LocalDate startDate,
                                                        LocalDate endDate, String status, Pageable pageable) {
        logger.debug("Advanced search for duty teacher reports with filters");

        DutyTeacherReport.ReportStatus reportStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                reportStatus = DutyTeacherReport.ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid report status: {}", status);
            }
        }

        return dutyTeacherReportRepository.advancedSearch(teacherId, adminId, startDate, endDate, reportStatus, pageable)
                .map(this::mapToResponse);
    }

    private DutyTeacherReportResponse mapToResponse(DutyTeacherReport report) {
        return DutyTeacherReportResponse.builder()
                .id(report.getId())
                .dutyTeacherId(report.getDutyTeacher().getId())
                .dutyTeacherName(report.getDutyTeacher().getUsername())
                .reportDate(report.getReportDate())
                .shiftStartTime(report.getShiftStartTime())
                .shiftEndTime(report.getShiftEndTime())
                .totalStudentsPresent(report.getTotalStudentsPresent())
                .totalStudentsAbsent(report.getTotalStudentsAbsent())
                .totalPermissionsApproved(report.getTotalPermissionsApproved())
                .totalPermissionsRejected(report.getTotalPermissionsRejected())
                .totalLateArrivals(report.getTotalLateArrivals())
                .totalEarlyDepartures(report.getTotalEarlyDepartures())
                .incidentsReported(report.getIncidentsReported())
                .actionsTaken(report.getActionsTaken())
                .schoolConditionNotes(report.getSchoolConditionNotes())
                .handoverNotes(report.getHandoverNotes())
                .nextDutyTeacher(report.getNextDutyTeacher())
                .reportStatus(report.getReportStatus())
                .submittedAt(report.getSubmittedAt())
                .approvedAt(report.getApprovedAt())
                .approvedByAdminId(report.getApprovedByAdmin() != null ? report.getApprovedByAdmin().getId() : null)
                .approvedByAdminName(report.getApprovedByAdmin() != null ? report.getApprovedByAdmin().getUsername() : null)
                .approvalNotes(report.getApprovalNotes())
                .attachments(report.getAttachments())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}