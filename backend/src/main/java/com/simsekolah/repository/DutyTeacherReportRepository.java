package com.simsekolah.repository;

import com.simsekolah.entity.DutyTeacherReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DutyTeacherReportRepository extends JpaRepository<DutyTeacherReport, Long> {

    List<DutyTeacherReport> findByDutyTeacherId(Long teacherId);

    List<DutyTeacherReport> findByDutyTeacherIdOrderByReportDateDesc(Long teacherId);

    List<DutyTeacherReport> findByReportDate(LocalDate reportDate);

    List<DutyTeacherReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);

    List<DutyTeacherReport> findByDutyTeacherIdAndReportDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);

    List<DutyTeacherReport> findByReportStatus(DutyTeacherReport.ReportStatus reportStatus);

    List<DutyTeacherReport> findByApprovedByAdminId(Long adminId);

    Optional<DutyTeacherReport> findByDutyTeacherIdAndReportDate(Long teacherId, LocalDate reportDate);

    boolean existsByDutyTeacherIdAndReportDate(Long teacherId, LocalDate reportDate);

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.dutyTeacher.id = :teacherId AND d.reportDate BETWEEN :startDate AND :endDate ORDER BY d.reportDate DESC")
    List<DutyTeacherReport> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(d) FROM DutyTeacherReport d WHERE d.dutyTeacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(d) FROM DutyTeacherReport d WHERE d.approvedByAdmin.id = :adminId")
    long countByAdminId(@Param("adminId") Long adminId);

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.reportStatus = 'SUBMITTED' AND d.approvedAt IS NULL")
    List<DutyTeacherReport> findPendingApprovalReports();

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.reportStatus = 'APPROVED'")
    List<DutyTeacherReport> findApprovedReports();

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.reportStatus = 'REJECTED'")
    List<DutyTeacherReport> findRejectedReports();

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.reportStatus = 'REVISION_REQUIRED'")
    List<DutyTeacherReport> findReportsRequiringRevision();

    @Query("SELECT SUM(d.totalStudentsPresent) FROM DutyTeacherReport d WHERE d.reportDate BETWEEN :startDate AND :endDate AND d.reportStatus = 'APPROVED'")
    Long getTotalStudentsPresentByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(d.totalStudentsAbsent) FROM DutyTeacherReport d WHERE d.reportDate BETWEEN :startDate AND :endDate AND d.reportStatus = 'APPROVED'")
    Long getTotalStudentsAbsentByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(d.totalPermissionsApproved) FROM DutyTeacherReport d WHERE d.reportDate BETWEEN :startDate AND :endDate AND d.reportStatus = 'APPROVED'")
    Long getTotalPermissionsApprovedByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Pagination support
    Page<DutyTeacherReport> findByDutyTeacherId(Long teacherId, Pageable pageable);

    Page<DutyTeacherReport> findByApprovedByAdminId(Long adminId, Pageable pageable);

    Page<DutyTeacherReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<DutyTeacherReport> findByReportStatus(DutyTeacherReport.ReportStatus reportStatus, Pageable pageable);

    @Query("SELECT d FROM DutyTeacherReport d WHERE " +
           "(:teacherId IS NULL OR d.dutyTeacher.id = :teacherId) AND " +
           "(:adminId IS NULL OR d.approvedByAdmin.id = :adminId) AND " +
           "(:startDate IS NULL OR d.reportDate >= :startDate) AND " +
           "(:endDate IS NULL OR d.reportDate <= :endDate) AND " +
           "(:reportStatus IS NULL OR d.reportStatus = :reportStatus)")
    Page<DutyTeacherReport> advancedSearch(@Param("teacherId") Long teacherId,
                                          @Param("adminId") Long adminId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("reportStatus") DutyTeacherReport.ReportStatus reportStatus,
                                          Pageable pageable);

    @Query("SELECT d.reportStatus as status, COUNT(d) as count FROM DutyTeacherReport d GROUP BY d.reportStatus")
    List<Object[]> getReportStatisticsByStatus();

    @Query("SELECT DATE_FORMAT(d.reportDate, '%Y-%m') as month, COUNT(d) as count FROM DutyTeacherReport d GROUP BY DATE_FORMAT(d.reportDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyReportStatistics();

    @Query("SELECT d FROM DutyTeacherReport d WHERE d.reportStatus = 'APPROVED' ORDER BY d.reportDate DESC")
    List<DutyTeacherReport> findApprovedReportsOrderByDateDesc(Pageable pageable);

    @Query("SELECT AVG(d.totalStudentsPresent) FROM DutyTeacherReport d WHERE d.reportStatus = 'APPROVED' AND d.reportDate BETWEEN :startDate AND :endDate")
    Double getAverageStudentsPresentByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}