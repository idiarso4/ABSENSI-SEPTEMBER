package com.simsekolah.repository;

import com.simsekolah.entity.StudentPermission;
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
public interface StudentPermissionRepository extends JpaRepository<StudentPermission, Long> {

    List<StudentPermission> findByStudentId(Long studentId);

    List<StudentPermission> findByStudentIdOrderByPermissionDateDesc(Long studentId);

    List<StudentPermission> findByPermissionDate(LocalDate permissionDate);

    List<StudentPermission> findByPermissionDateBetween(LocalDate startDate, LocalDate endDate);

    List<StudentPermission> findByStudentIdAndPermissionDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<StudentPermission> findByPermissionType(StudentPermission.PermissionType permissionType);

    List<StudentPermission> findByPermissionStatus(StudentPermission.PermissionStatus permissionStatus);

    List<StudentPermission> findByReturnStatus(StudentPermission.ReturnStatus returnStatus);

    List<StudentPermission> findByApprovedByDutyTeacherId(Long teacherId);

    Optional<StudentPermission> findByStudentIdAndPermissionDate(Long studentId, LocalDate permissionDate);

    @Query("SELECT p FROM StudentPermission p WHERE p.student.id = :studentId AND p.permissionDate BETWEEN :startDate AND :endDate ORDER BY p.permissionDate DESC")
    List<StudentPermission> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM StudentPermission p WHERE p.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(p) FROM StudentPermission p WHERE p.student.id = :studentId AND p.permissionDate BETWEEN :startDate AND :endDate")
    long countByStudentIdAndPermissionDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM StudentPermission p WHERE p.student.id = :studentId AND p.permissionStatus = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") StudentPermission.PermissionStatus status);

    boolean existsByStudentIdAndPermissionDate(Long studentId, LocalDate permissionDate);

    @Query("SELECT COUNT(p) FROM StudentPermission p WHERE p.approvedByDutyTeacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT p FROM StudentPermission p WHERE p.permissionStatus = 'PENDING' AND p.parentApproval = true")
    List<StudentPermission> findPendingDutyTeacherApproval();

    @Query("SELECT p FROM StudentPermission p WHERE p.returnStatus = 'PENDING' AND p.permissionDate < :currentDate")
    List<StudentPermission> findOverduePermissions(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM StudentPermission p WHERE p.returnStatus = 'NOT_RETURNED' AND p.permissionDate < :currentDate")
    List<StudentPermission> findUnreturnedPermissions(@Param("currentDate") LocalDate currentDate);

    // Pagination support
    Page<StudentPermission> findByStudentId(Long studentId, Pageable pageable);

    Page<StudentPermission> findByApprovedByDutyTeacherId(Long teacherId, Pageable pageable);

    Page<StudentPermission> findByPermissionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<StudentPermission> findByPermissionStatus(StudentPermission.PermissionStatus permissionStatus, Pageable pageable);

    Page<StudentPermission> findByReturnStatus(StudentPermission.ReturnStatus returnStatus, Pageable pageable);

    @Query("SELECT p FROM StudentPermission p WHERE " +
           "(:studentId IS NULL OR p.student.id = :studentId) AND " +
           "(:teacherId IS NULL OR p.approvedByDutyTeacher.id = :teacherId) AND " +
           "(:startDate IS NULL OR p.permissionDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.permissionDate <= :endDate) AND " +
           "(:permissionType IS NULL OR p.permissionType = :permissionType) AND " +
           "(:permissionStatus IS NULL OR p.permissionStatus = :permissionStatus) AND " +
           "(:returnStatus IS NULL OR p.returnStatus = :returnStatus)")
    Page<StudentPermission> advancedSearch(@Param("studentId") Long studentId,
                                          @Param("teacherId") Long teacherId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("permissionType") StudentPermission.PermissionType permissionType,
                                          @Param("permissionStatus") StudentPermission.PermissionStatus permissionStatus,
                                          @Param("returnStatus") StudentPermission.ReturnStatus returnStatus,
                                          Pageable pageable);

    @Query("SELECT p.permissionStatus as status, COUNT(p) as count FROM StudentPermission p GROUP BY p.permissionStatus")
    List<Object[]> getPermissionStatisticsByStatus();

    @Query("SELECT p.returnStatus as status, COUNT(p) as count FROM StudentPermission p GROUP BY p.returnStatus")
    List<Object[]> getReturnStatisticsByStatus();

    @Query("SELECT p.permissionType as type, COUNT(p) as count FROM StudentPermission p GROUP BY p.permissionType")
    List<Object[]> getPermissionStatisticsByType();

    @Query("SELECT DATE_FORMAT(p.permissionDate, '%Y-%m') as month, COUNT(p) as count FROM StudentPermission p GROUP BY DATE_FORMAT(p.permissionDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyPermissionStatistics();
}