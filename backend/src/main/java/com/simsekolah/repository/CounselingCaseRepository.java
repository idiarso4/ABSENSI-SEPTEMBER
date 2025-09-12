package com.simsekolah.repository;

import com.simsekolah.entity.CounselingCase;
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
public interface CounselingCaseRepository extends JpaRepository<CounselingCase, Long> {

    List<CounselingCase> findByStudentId(Long studentId);

    List<CounselingCase> findByStudentIdOrderByReportedDateDesc(Long studentId);

    List<CounselingCase> findByCounselorId(Long counselorId);

    List<CounselingCase> findByReportedDate(LocalDate reportedDate);

    List<CounselingCase> findByReportedDateBetween(LocalDate startDate, LocalDate endDate);

    List<CounselingCase> findByCaseStatus(CounselingCase.CaseStatus caseStatus);

    List<CounselingCase> findBySeverityLevel(CounselingCase.SeverityLevel severityLevel);

    List<CounselingCase> findByCaseCategory(CounselingCase.CaseCategory caseCategory);

    Optional<CounselingCase> findByCaseNumber(String caseNumber);

    @Query("SELECT c FROM CounselingCase c WHERE c.student.id = :studentId AND c.reportedDate BETWEEN :startDate AND :endDate ORDER BY c.reportedDate DESC")
    List<CounselingCase> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM CounselingCase c WHERE c.counselor.id = :counselorId AND c.reportedDate BETWEEN :startDate AND :endDate")
    List<CounselingCase> findByCounselorIdAndDateRange(@Param("counselorId") Long counselorId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(c) FROM CounselingCase c WHERE c.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(c) FROM CounselingCase c WHERE c.counselor.id = :counselorId")
    long countByCounselorId(@Param("counselorId") Long counselorId);

    @Query("SELECT c FROM CounselingCase c WHERE c.caseStatus = 'OPEN' OR c.caseStatus = 'IN_PROGRESS'")
    List<CounselingCase> findActiveCases();

    @Query("SELECT c FROM CounselingCase c WHERE c.followUpRequired = true AND c.followUpDate <= :currentDate")
    List<CounselingCase> findCasesRequiringFollowUp(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT c FROM CounselingCase c WHERE c.caseStatus = 'RESOLVED' OR c.caseStatus = 'CLOSED'")
    List<CounselingCase> findResolvedCases();

    @Query("SELECT c FROM CounselingCase c WHERE c.severityLevel = 'CRITICAL' OR c.severityLevel = 'HIGH'")
    List<CounselingCase> findHighPriorityCases();

    // Pagination support
    Page<CounselingCase> findByStudentId(Long studentId, Pageable pageable);

    Page<CounselingCase> findByCounselorId(Long counselorId, Pageable pageable);

    Page<CounselingCase> findByReportedDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<CounselingCase> findByCaseStatus(CounselingCase.CaseStatus caseStatus, Pageable pageable);

    Page<CounselingCase> findBySeverityLevel(CounselingCase.SeverityLevel severityLevel, Pageable pageable);

    @Query("SELECT c FROM CounselingCase c WHERE " +
           "(:studentId IS NULL OR c.student.id = :studentId) AND " +
           "(:counselorId IS NULL OR c.counselor.id = :counselorId) AND " +
           "(:startDate IS NULL OR c.reportedDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.reportedDate <= :endDate) AND " +
           "(:caseCategory IS NULL OR c.caseCategory = :caseCategory) AND " +
           "(:severityLevel IS NULL OR c.severityLevel = :severityLevel) AND " +
           "(:caseStatus IS NULL OR c.caseStatus = :caseStatus)")
    Page<CounselingCase> advancedSearch(@Param("studentId") Long studentId,
                                       @Param("counselorId") Long counselorId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("caseCategory") CounselingCase.CaseCategory caseCategory,
                                       @Param("severityLevel") CounselingCase.SeverityLevel severityLevel,
                                       @Param("caseStatus") CounselingCase.CaseStatus caseStatus,
                                       Pageable pageable);

    @Query("SELECT c.caseStatus as status, COUNT(c) as count FROM CounselingCase c GROUP BY c.caseStatus")
    List<Object[]> getCaseStatisticsByStatus();

    @Query("SELECT c.severityLevel as level, COUNT(c) as count FROM CounselingCase c GROUP BY c.severityLevel")
    List<Object[]> getCaseStatisticsBySeverity();

    @Query("SELECT c.caseCategory as category, COUNT(c) as count FROM CounselingCase c GROUP BY c.caseCategory")
    List<Object[]> getCaseStatisticsByCategory();

    @Query("SELECT DATE_FORMAT(c.reportedDate, '%Y-%m') as month, COUNT(c) as count FROM CounselingCase c GROUP BY DATE_FORMAT(c.reportedDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyCaseStatistics();
}