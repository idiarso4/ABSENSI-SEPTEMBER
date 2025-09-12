package com.simsekolah.repository;

import com.simsekolah.entity.PklActivity;
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
public interface PklActivityRepository extends JpaRepository<PklActivity, Long> {

    List<PklActivity> findByStudentId(Long studentId);

    List<PklActivity> findByStudentIdOrderByActivityDateDesc(Long studentId);

    List<PklActivity> findBySupervisingTeacherId(Long teacherId);

    List<PklActivity> findByActivityDate(LocalDate activityDate);

    List<PklActivity> findByActivityDateBetween(LocalDate startDate, LocalDate endDate);

    List<PklActivity> findByStudentIdAndActivityDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<PklActivity> findByCompanyNameContainingIgnoreCase(String companyName);

    List<PklActivity> findByActivityStatus(PklActivity.ActivityStatus activityStatus);

    List<PklActivity> findByApprovedByTeacher(Boolean approved);

    Optional<PklActivity> findByStudentIdAndActivityDate(Long studentId, LocalDate activityDate);

    @Query("SELECT p FROM PklActivity p WHERE p.student.id = :studentId AND p.activityDate BETWEEN :startDate AND :endDate ORDER BY p.activityDate DESC")
    List<PklActivity> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM PklActivity p WHERE p.supervisingTeacher.id = :teacherId AND p.activityDate BETWEEN :startDate AND :endDate")
    List<PklActivity> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM PklActivity p WHERE p.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(p) FROM PklActivity p WHERE p.supervisingTeacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT p FROM PklActivity p WHERE p.activityStatus = 'SUBMITTED' AND p.approvedByTeacher IS NULL")
    List<PklActivity> findPendingApprovalActivities();

    @Query("SELECT p FROM PklActivity p WHERE p.activityStatus = 'APPROVED'")
    List<PklActivity> findApprovedActivities();

    @Query("SELECT p FROM PklActivity p WHERE p.activityStatus = 'REJECTED'")
    List<PklActivity> findRejectedActivities();

    @Query("SELECT SUM(p.workingHours) FROM PklActivity p WHERE p.student.id = :studentId AND p.activityDate BETWEEN :startDate AND :endDate")
    Integer getTotalWorkingHoursByStudentAndDateRange(@Param("studentId") Long studentId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    // Pagination support
    Page<PklActivity> findByStudentId(Long studentId, Pageable pageable);

    Page<PklActivity> findBySupervisingTeacherId(Long teacherId, Pageable pageable);

    Page<PklActivity> findByActivityDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<PklActivity> findByActivityStatus(PklActivity.ActivityStatus activityStatus, Pageable pageable);

    @Query("SELECT p FROM PklActivity p WHERE " +
           "(:studentId IS NULL OR p.student.id = :studentId) AND " +
           "(:teacherId IS NULL OR p.supervisingTeacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR p.activityDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.activityDate <= :endDate) AND " +
           "(:companyName IS NULL OR LOWER(p.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:activityStatus IS NULL OR p.activityStatus = :activityStatus) AND " +
           "(:approved IS NULL OR p.approvedByTeacher = :approved)")
    Page<PklActivity> advancedSearch(@Param("studentId") Long studentId,
                                    @Param("teacherId") Long teacherId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("companyName") String companyName,
                                    @Param("activityStatus") PklActivity.ActivityStatus activityStatus,
                                    @Param("approved") Boolean approved,
                                    Pageable pageable);

    @Query("SELECT p.activityStatus as status, COUNT(p) as count FROM PklActivity p GROUP BY p.activityStatus")
    List<Object[]> getActivityStatisticsByStatus();

    @Query("SELECT DATE_FORMAT(p.activityDate, '%Y-%m') as month, COUNT(p) as count FROM PklActivity p GROUP BY DATE_FORMAT(p.activityDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyActivityStatistics();

    @Query("SELECT p FROM PklActivity p WHERE p.activityStatus = 'APPROVED' ORDER BY p.activityDate DESC")
    List<PklActivity> findApprovedActivitiesOrderByDateDesc(Pageable pageable);

    @Query("SELECT AVG(p.workingHours) FROM PklActivity p WHERE p.student.id = :studentId AND p.activityStatus = 'APPROVED'")
    Double getAverageWorkingHoursByStudent(@Param("studentId") Long studentId);
}