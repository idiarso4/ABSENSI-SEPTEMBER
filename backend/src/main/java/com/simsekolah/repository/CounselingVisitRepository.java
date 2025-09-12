package com.simsekolah.repository;

import com.simsekolah.entity.CounselingVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CounselingVisitRepository extends JpaRepository<CounselingVisit, Long> {

    List<CounselingVisit> findByCounselingCaseId(Long counselingCaseId);

    List<CounselingVisit> findByCounselingCaseIdOrderByVisitDateDesc(Long counselingCaseId);

    List<CounselingVisit> findByCounselorId(Long counselorId);

    List<CounselingVisit> findByVisitDate(LocalDate visitDate);

    List<CounselingVisit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    List<CounselingVisit> findByVisitStatus(CounselingVisit.VisitStatus visitStatus);

    List<CounselingVisit> findByVisitType(CounselingVisit.VisitType visitType);

    @Query("SELECT v FROM CounselingVisit v WHERE v.counselingCase.id = :caseId AND v.visitDate BETWEEN :startDate AND :endDate ORDER BY v.visitDate DESC")
    List<CounselingVisit> findByCaseIdAndDateRange(@Param("caseId") Long caseId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM CounselingVisit v WHERE v.counselor.id = :counselorId AND v.visitDate BETWEEN :startDate AND :endDate")
    List<CounselingVisit> findByCounselorIdAndDateRange(@Param("counselorId") Long counselorId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(v) FROM CounselingVisit v WHERE v.counselingCase.id = :caseId")
    long countByCaseId(@Param("caseId") Long caseId);

    @Query("SELECT COUNT(v) FROM CounselingVisit v WHERE v.counselor.id = :counselorId")
    long countByCounselorId(@Param("counselorId") Long counselorId);

    @Query("SELECT v FROM CounselingVisit v WHERE v.visitStatus = 'SCHEDULED' AND v.visitDate = :currentDate")
    List<CounselingVisit> findTodaysScheduledVisits(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT v FROM CounselingVisit v WHERE v.visitStatus = 'SCHEDULED' AND v.visitDate < :currentDate")
    List<CounselingVisit> findOverdueScheduledVisits(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT v FROM CounselingVisit v WHERE v.followUpRequired = true")
    List<CounselingVisit> findVisitsRequiringFollowUp();

    @Query("SELECT v FROM CounselingVisit v WHERE v.visitStatus = 'COMPLETED'")
    List<CounselingVisit> findCompletedVisits();

    @Query("SELECT AVG(v.studentMoodRating) FROM CounselingVisit v WHERE v.counselingCase.id = :caseId AND v.studentMoodRating IS NOT NULL")
    Double getAverageMoodRatingByCase(@Param("caseId") Long caseId);

    @Query("SELECT AVG(v.progressRating) FROM CounselingVisit v WHERE v.counselingCase.id = :caseId AND v.progressRating IS NOT NULL")
    Double getAverageProgressRatingByCase(@Param("caseId") Long caseId);

    // Pagination support
    Page<CounselingVisit> findByCounselingCaseId(Long counselingCaseId, Pageable pageable);

    Page<CounselingVisit> findByCounselorId(Long counselorId, Pageable pageable);

    Page<CounselingVisit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<CounselingVisit> findByVisitStatus(CounselingVisit.VisitStatus visitStatus, Pageable pageable);

    Page<CounselingVisit> findByVisitType(CounselingVisit.VisitType visitType, Pageable pageable);

    @Query("SELECT v FROM CounselingVisit v WHERE " +
           "(:caseId IS NULL OR v.counselingCase.id = :caseId) AND " +
           "(:counselorId IS NULL OR v.counselor.id = :counselorId) AND " +
           "(:startDate IS NULL OR v.visitDate >= :startDate) AND " +
           "(:endDate IS NULL OR v.visitDate <= :endDate) AND " +
           "(:visitType IS NULL OR v.visitType = :visitType) AND " +
           "(:visitStatus IS NULL OR v.visitStatus = :visitStatus)")
    Page<CounselingVisit> advancedSearch(@Param("caseId") Long caseId,
                                        @Param("counselorId") Long counselorId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("visitType") CounselingVisit.VisitType visitType,
                                        @Param("visitStatus") CounselingVisit.VisitStatus visitStatus,
                                        Pageable pageable);

    @Query("SELECT v.visitStatus as status, COUNT(v) as count FROM CounselingVisit v GROUP BY v.visitStatus")
    List<Object[]> getVisitStatisticsByStatus();

    @Query("SELECT v.visitType as type, COUNT(v) as count FROM CounselingVisit v GROUP BY v.visitType")
    List<Object[]> getVisitStatisticsByType();

    @Query("SELECT DATE_FORMAT(v.visitDate, '%Y-%m') as month, COUNT(v) as count FROM CounselingVisit v GROUP BY DATE_FORMAT(v.visitDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyVisitStatistics();

    @Query("SELECT v FROM CounselingVisit v WHERE v.visitStatus = 'COMPLETED' ORDER BY v.visitDate DESC")
    List<CounselingVisit> findCompletedVisitsOrderByDateDesc(Pageable pageable);

    @Query("SELECT v FROM CounselingVisit v WHERE v.counselingCase.student.id = :studentId ORDER BY v.visitDate DESC")
    List<CounselingVisit> findByStudentId(@Param("studentId") Long studentId);
}