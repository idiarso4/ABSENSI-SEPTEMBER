package com.simsekolah.repository;

import com.simsekolah.entity.PklAttendance;
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
public interface PklAttendanceRepository extends JpaRepository<PklAttendance, Long> {

    List<PklAttendance> findByStudentId(Long studentId);

    List<PklAttendance> findByStudentIdOrderByAttendanceDateDesc(Long studentId);

    List<PklAttendance> findByAttendanceDate(LocalDate attendanceDate);

    List<PklAttendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    List<PklAttendance> findByStudentIdAndAttendanceDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<PklAttendance> findBySupervisingTeacherId(Long teacherId);

    List<PklAttendance> findByCompanyNameContainingIgnoreCase(String companyName);

    Optional<PklAttendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    boolean existsByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    @Query("SELECT p FROM PklAttendance p WHERE p.student.id = :studentId AND p.attendanceDate BETWEEN :startDate AND :endDate ORDER BY p.attendanceDate DESC")
    List<PklAttendance> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM PklAttendance p WHERE p.student.id = :studentId AND p.status = 'PRESENT'")
    long countPresentDaysByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(p) FROM PklAttendance p WHERE p.student.id = :studentId")
    long countTotalDaysByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT p FROM PklAttendance p WHERE p.verifiedByTeacher = false")
    List<PklAttendance> findUnverifiedAttendances();

    @Query("SELECT p FROM PklAttendance p WHERE p.supervisingTeacher.id = :teacherId AND p.attendanceDate BETWEEN :startDate AND :endDate")
    List<PklAttendance> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    // Pagination support
    Page<PklAttendance> findByStudentId(Long studentId, Pageable pageable);

    Page<PklAttendance> findBySupervisingTeacherId(Long teacherId, Pageable pageable);

    Page<PklAttendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<PklAttendance> findByStatus(PklAttendance.AttendanceStatus status, Pageable pageable);

    @Query("SELECT p FROM PklAttendance p WHERE " +
           "(:studentId IS NULL OR p.student.id = :studentId) AND " +
           "(:teacherId IS NULL OR p.supervisingTeacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR p.attendanceDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.attendanceDate <= :endDate) AND " +
           "(:companyName IS NULL OR LOWER(p.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<PklAttendance> advancedSearch(@Param("studentId") Long studentId,
                                      @Param("teacherId") Long teacherId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      @Param("companyName") String companyName,
                                      @Param("status") PklAttendance.AttendanceStatus status,
                                      Pageable pageable);

    @Query("SELECT COUNT(p) FROM PklAttendance p WHERE p.student.id = :studentId AND p.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") PklAttendance.AttendanceStatus status);

    @Query("SELECT COUNT(p) FROM PklAttendance p WHERE p.student.id = :studentId AND p.attendanceDate BETWEEN :startDate AND :endDate")
    long countByStudentIdAndAttendanceDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p.status as status, COUNT(p) as count FROM PklAttendance p GROUP BY p.status")
    List<Object[]> getAttendanceStatisticsByStatus();

    @Query("SELECT DATE_FORMAT(p.attendanceDate, '%Y-%m') as month, COUNT(p) as count FROM PklAttendance p WHERE p.status = 'PRESENT' GROUP BY DATE_FORMAT(p.attendanceDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyAttendanceStatistics();
}