package com.simsekolah.repository;

import com.simsekolah.entity.TeacherAttendance;
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
public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {

    List<TeacherAttendance> findByTeacherId(Long teacherId);

    List<TeacherAttendance> findByTeacherIdOrderByAttendanceDateDesc(Long teacherId);

    List<TeacherAttendance> findByAttendanceDate(LocalDate attendanceDate);

    List<TeacherAttendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    List<TeacherAttendance> findByTeacherIdAndAttendanceDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);

    List<TeacherAttendance> findByAttendanceStatus(TeacherAttendance.AttendanceStatus attendanceStatus);

    List<TeacherAttendance> findByVerificationMethod(TeacherAttendance.VerificationMethod verificationMethod);

    Optional<TeacherAttendance> findByTeacherIdAndAttendanceDate(Long teacherId, LocalDate attendanceDate);

    boolean existsByTeacherIdAndAttendanceDate(Long teacherId, LocalDate attendanceDate);

    @Query("SELECT t FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceDate BETWEEN :startDate AND :endDate ORDER BY t.attendanceDate DESC")
    List<TeacherAttendance> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(t) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceStatus = 'PRESENT'")
    long countPresentDaysByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(t) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId")
    long countTotalDaysByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT SUM(t.teachingHours) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceDate BETWEEN :startDate AND :endDate")
    Integer getTotalTeachingHoursByTeacherAndDateRange(@Param("teacherId") Long teacherId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(t.officeHours) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceDate BETWEEN :startDate AND :endDate")
    Integer getTotalOfficeHoursByTeacherAndDateRange(@Param("teacherId") Long teacherId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(t.totalHours) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceDate BETWEEN :startDate AND :endDate")
    Integer getTotalHoursByTeacherAndDateRange(@Param("teacherId") Long teacherId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    // Pagination support
    Page<TeacherAttendance> findByTeacherId(Long teacherId, Pageable pageable);

    Page<TeacherAttendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<TeacherAttendance> findByAttendanceStatus(TeacherAttendance.AttendanceStatus attendanceStatus, Pageable pageable);

    @Query("SELECT t FROM TeacherAttendance t WHERE " +
           "(:teacherId IS NULL OR t.teacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR t.attendanceDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.attendanceDate <= :endDate) AND " +
           "(:attendanceStatus IS NULL OR t.attendanceStatus = :attendanceStatus) AND " +
           "(:verificationMethod IS NULL OR t.verificationMethod = :verificationMethod)")
    Page<TeacherAttendance> advancedSearch(@Param("teacherId") Long teacherId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("attendanceStatus") TeacherAttendance.AttendanceStatus attendanceStatus,
                                          @Param("verificationMethod") TeacherAttendance.VerificationMethod verificationMethod,
                                          Pageable pageable);

    @Query("SELECT t.attendanceStatus as status, COUNT(t) as count FROM TeacherAttendance t GROUP BY t.attendanceStatus")
    List<Object[]> getAttendanceStatisticsByStatus();

    @Query("SELECT t.verificationMethod as method, COUNT(t) as count FROM TeacherAttendance t GROUP BY t.verificationMethod")
    List<Object[]> getVerificationStatisticsByMethod();

    @Query("SELECT DATE_FORMAT(t.attendanceDate, '%Y-%m') as month, COUNT(t) as count FROM TeacherAttendance t WHERE t.attendanceStatus = 'PRESENT' GROUP BY DATE_FORMAT(t.attendanceDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyAttendanceStatistics();

    @Query("SELECT t FROM TeacherAttendance t WHERE t.verifiedBySystem = false")
    List<TeacherAttendance> findUnverifiedAttendances();

    @Query("SELECT AVG(t.totalHours) FROM TeacherAttendance t WHERE t.teacher.id = :teacherId AND t.attendanceDate BETWEEN :startDate AND :endDate")
    Double getAverageHoursByTeacherAndDateRange(@Param("teacherId") Long teacherId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}