package com.simsekolah.service;

import com.simsekolah.dto.request.CreateStudentPermissionRequest;
import com.simsekolah.dto.request.UpdateStudentPermissionRequest;
import com.simsekolah.dto.response.StudentPermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for student permission management operations
 */
public interface StudentPermissionService {

    /**
     * Create a new student permission request
     */
    StudentPermissionResponse createPermission(CreateStudentPermissionRequest request);

    /**
     * Update an existing student permission
     */
    StudentPermissionResponse updatePermission(Long permissionId, UpdateStudentPermissionRequest request);

    /**
     * Get permission by ID
     */
    Optional<StudentPermissionResponse> getPermissionById(Long permissionId);

    /**
     * Get all permissions with pagination
     */
    Page<StudentPermissionResponse> getAllPermissions(Pageable pageable);

    /**
     * Get permissions by student ID
     */
    Page<StudentPermissionResponse> getPermissionsByStudent(Long studentId, Pageable pageable);

    /**
     * Get permissions by teacher ID
     */
    Page<StudentPermissionResponse> getPermissionsByTeacher(Long teacherId, Pageable pageable);

    /**
     * Get permissions by date range
     */
    Page<StudentPermissionResponse> getPermissionsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get permissions by status
     */
    Page<StudentPermissionResponse> getPermissionsByStatus(String status, Pageable pageable);

    /**
     * Get permissions by return status
     */
    Page<StudentPermissionResponse> getPermissionsByReturnStatus(String returnStatus, Pageable pageable);

    /**
     * Delete permission by ID
     */
    void deletePermission(Long permissionId);

    /**
     * Approve permission by duty teacher
     */
    StudentPermissionResponse approvePermission(Long permissionId, Long teacherId, String approvalNotes);

    /**
     * Reject permission by duty teacher
     */
    StudentPermissionResponse rejectPermission(Long permissionId, Long teacherId, String rejectionReason);

    /**
     * Mark student as returned
     */
    StudentPermissionResponse markAsReturned(Long permissionId, LocalDate returnTime);

    /**
     * Get pending permissions for duty teacher approval
     */
    List<StudentPermissionResponse> getPendingPermissions();

    /**
     * Get overdue permissions
     */
    List<StudentPermissionResponse> getOverduePermissions();

    /**
     * Get unreturned permissions
     */
    List<StudentPermissionResponse> getUnreturnedPermissions();

    /**
     * Get permission statistics
     */
    Map<String, Object> getPermissionStatistics();

    /**
     * Get student permission summary
     */
    Map<String, Object> getStudentPermissionSummary(Long studentId, LocalDate startDate, LocalDate endDate);

    /**
     * Check if permission exists for student on date
     */
    boolean existsByStudentAndDate(Long studentId, LocalDate date);

    /**
     * Bulk approve permissions
     */
    List<StudentPermissionResponse> bulkApprovePermissions(List<Long> permissionIds, Long teacherId);

    /**
     * Get monthly permission statistics
     */
    List<Map<String, Object>> getMonthlyPermissionStatistics();

    /**
     * Advanced search for permissions
     */
    Page<StudentPermissionResponse> advancedSearch(Long studentId, Long teacherId, LocalDate startDate,
                                                  LocalDate endDate, String permissionType, String permissionStatus,
                                                  String returnStatus, Pageable pageable);
}