package com.simsekolah.repository;

import com.simsekolah.entity.PklDailyReport;
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
public interface PklDailyReportRepository extends JpaRepository<PklDailyReport, Long> {

    List<PklDailyReport> findByStudentId(Long studentId);

    List<PklDailyReport> findByStudentIdOrderByReportDateDesc(Long studentId);

    List<PklDailyReport> findBySupervisingTeacherId(Long teacherId);

    List<PklDailyReport> findByReportDate(LocalDate reportDate);

    List<PklDailyReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);

    List<PklDailyReport> findByStudentIdAndReportDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<PklDailyReport> findByCompanyNameContainingIgnoreCase(String companyName);

    List<PklDailyReport> findByReportStatus(PklDailyReport.ReportStatus reportStatus);

    Optional<PklDailyReport> findByStudentIdAndReportDate(Long studentId, LocalDate reportDate);

    boolean existsByStudentIdAndReportDate(Long studentId, LocalDate reportDate);

    @Query("SELECT p FROM PklDailyReport p WHERE p.student.id = :studentId AND p.reportDate BETWEEN :startDate AND :endDate ORDER BY p.reportDate DESC")
    List<PklDailyReport> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM PklDailyReport p WHERE p.supervisingTeacher.id = :teacherId AND p.reportDate BETWEEN :startDate AND :endDate")
    List<PklDailyReport> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM PklDailyReport p WHERE p.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(p) FROM PklDailyReport p WHERE p.supervisingTeacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT p FROM PklDailyReport p WHERE p.reportStatus = 'SUBMITTED' AND p.reviewedAt IS NULL")
    List<PklDailyReport> findPendingReviewReports();

    @Query("SELECT p FROM PklDailyReport p WHERE p.reportStatus = 'APPROVED'")
    List<PklDailyReport> findApprovedReports();

    @Query("SELECT p FROM PklDailyReport p WHERE p.reportStatus = 'REJECTED'")
    List<PklDailyReport> findRejectedReports();

    @Query("SELECT p FROM PklDailyReport p WHERE p.reportStatus = 'REVISION_REQUIRED'")
    List<PklDailyReport> findReportsRequiringRevision();

    @Query("SELECT AVG(p.moodRating) FROM PklDailyReport p WHERE p.student.id = :studentId AND p.reportDate BETWEEN :startDate AND :endDate AND p.moodRating IS NOT NULL")
    Double getAverageMoodRatingByStudentAndDateRange(@Param("studentId") Long studentId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(p.productivityRating) FROM PklDailyReport p WHERE p.student.id = :studentId AND p.reportDate BETWEEN :startDate AND :endDate AND p.productivityRating IS NOT NULL")
    Double getAverageProductivityRatingByStudentAndDateRange(@Param("studentId") Long studentId,
                                                             @Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(p.workingHours) FROM PklDailyReport p WHERE p.student.id = :studentId AND p.reportDate BETWEEN :startDate AND :endDate")
    Integer getTotalWorkingHoursByStudentAndDateRange(@Param("studentId") Long studentId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    // Pagination support
    Page<PklDailyReport> findByStudentId(Long studentId, Pageable pageable);

    Page<PklDailyReport> findBySupervisingTeacherId(Long teacherId, Pageable pageable);

    Page<PklDailyReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<PklDailyReport> findByReportStatus(PklDailyReport.ReportStatus reportStatus, Pageable pageable);

    @Query("SELECT p FROM PklDailyReport p WHERE " +
           "(:studentId IS NULL OR p.student.id = :studentId) AND " +
           "(:teacherId IS NULL OR p.supervisingTeacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR p.reportDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.reportDate <= :endDate) AND " +
           "(:companyName IS NULL OR LOWER(p.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:reportStatus IS NULL OR p.reportStatus = :reportStatus)")
    Page<PklDailyReport> advancedSearch(@Param("studentId") Long studentId,
                                       @Param("teacherId") Long teacherId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("companyName") String companyName,
                                       @Param("reportStatus") PklDailyReport.ReportStatus reportStatus,
                                       Pageable pageable);

    @Query("SELECT p.reportStatus as status, COUNT(p) as count FROM PklDailyReport p GROUP BY p.reportStatus")
    List<Object[]> getReportStatisticsByStatus();

    @Query("SELECT DATE_FORMAT(p.reportDate, '%Y-%m') as month, COUNT(p) as count FROM PklDailyReport p GROUP BY DATE_FORMAT(p.reportDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyReportStatistics();

    @Query("SELECT p FROM PklDailyReport p WHERE p.reportStatus = 'APPROVED' ORDER BY p.reportDate DESC")
    List<PklDailyReport> findApprovedReportsOrderByDateDesc(Pageable pageable);

    @Query("SELECT AVG(p.moodRating) FROM PklDailyReport p WHERE p.reportStatus = 'APPROVED' AND p.moodRating IS NOT NULL")
    Double getOverallAverageMoodRating();

    @Query("SELECT AVG(p.productivityRating) FROM PklDailyReport p WHERE p.reportStatus = 'APPROVED' AND p.productivityRating IS NOT NULL")
    Double getOverallAverageProductivityRating();
}