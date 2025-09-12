package com.simsekolah.repository;

import com.simsekolah.entity.PklVisit;
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
public interface PklVisitRepository extends JpaRepository<PklVisit, Long> {

    List<PklVisit> findByStudentId(Long studentId);

    List<PklVisit> findByStudentIdOrderByVisitDateDesc(Long studentId);

    List<PklVisit> findBySupervisingTeacherId(Long teacherId);

    List<PklVisit> findByVisitDate(LocalDate visitDate);

    List<PklVisit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    List<PklVisit> findByStudentIdAndVisitDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<PklVisit> findByCompanyNameContainingIgnoreCase(String companyName);

    List<PklVisit> findByVisitStatus(PklVisit.VisitStatus visitStatus);

    Optional<PklVisit> findByStudentIdAndVisitDate(Long studentId, LocalDate visitDate);

    @Query("SELECT p FROM PklVisit p WHERE p.student.id = :studentId AND p.visitDate BETWEEN :startDate AND :endDate ORDER BY p.visitDate DESC")
    List<PklVisit> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM PklVisit p WHERE p.supervisingTeacher.id = :teacherId AND p.visitDate BETWEEN :startDate AND :endDate")
    List<PklVisit> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM PklVisit p WHERE p.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(p) FROM PklVisit p WHERE p.supervisingTeacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT p FROM PklVisit p WHERE p.followUpRequired = true AND p.followUpDate <= :currentDate")
    List<PklVisit> findVisitsRequiringFollowUp(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM PklVisit p WHERE p.visitStatus = 'PLANNED' AND p.visitDate <= :currentDate")
    List<PklVisit> findOverduePlannedVisits(@Param("currentDate") LocalDate currentDate);

    // Pagination support
    Page<PklVisit> findByStudentId(Long studentId, Pageable pageable);

    Page<PklVisit> findBySupervisingTeacherId(Long teacherId, Pageable pageable);

    Page<PklVisit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<PklVisit> findByVisitStatus(PklVisit.VisitStatus visitStatus, Pageable pageable);

    @Query("SELECT p FROM PklVisit p WHERE " +
           "(:studentId IS NULL OR p.student.id = :studentId) AND " +
           "(:teacherId IS NULL OR p.supervisingTeacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR p.visitDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.visitDate <= :endDate) AND " +
           "(:companyName IS NULL OR LOWER(p.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:visitStatus IS NULL OR p.visitStatus = :visitStatus)")
    Page<PklVisit> advancedSearch(@Param("studentId") Long studentId,
                                 @Param("teacherId") Long teacherId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 @Param("companyName") String companyName,
                                 @Param("visitStatus") PklVisit.VisitStatus visitStatus,
                                 Pageable pageable);

    @Query("SELECT p.visitStatus as status, COUNT(p) as count FROM PklVisit p GROUP BY p.visitStatus")
    List<Object[]> getVisitStatisticsByStatus();

    @Query("SELECT DATE_FORMAT(p.visitDate, '%Y-%m') as month, COUNT(p) as count FROM PklVisit p GROUP BY DATE_FORMAT(p.visitDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyVisitStatistics();

    @Query("SELECT p FROM PklVisit p WHERE p.visitStatus = 'COMPLETED' ORDER BY p.visitDate DESC")
    List<PklVisit> findCompletedVisitsOrderByDateDesc(Pageable pageable);
}