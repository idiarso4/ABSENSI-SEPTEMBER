package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateStudentPermissionRequest;
import com.simsekolah.dto.request.UpdateStudentPermissionRequest;
import com.simsekolah.dto.response.StudentPermissionResponse;
import com.simsekolah.entity.Student;
import com.simsekolah.entity.StudentPermission;
import com.simsekolah.entity.User;
import com.simsekolah.repository.StudentPermissionRepository;
import com.simsekolah.repository.StudentRepository;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.service.StudentPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of StudentPermissionService
 */
@Service
@Transactional
public class StudentPermissionServiceImpl implements StudentPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(StudentPermissionServiceImpl.class);

    @Autowired
    private StudentPermissionRepository studentPermissionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public StudentPermissionResponse createPermission(CreateStudentPermissionRequest request) {
        logger.info("Creating student permission for student ID: {}", request.getStudentId());

        // Validate student exists
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + request.getStudentId()));

        // Create permission record
        StudentPermission permission = new StudentPermission();
        permission.setStudent(student);
        permission.setPermissionType(request.getPermissionType());
        permission.setReason(request.getReason());
        permission.setPermissionDate(request.getPermissionDate());
        permission.setStartTime(request.getStartTime());
        permission.setEndTime(request.getEndTime());
        permission.setExpectedReturnTime(request.getExpectedReturnTime());
        permission.setDestinationAddress(request.getDestinationAddress());
        permission.setAccompaniedBy(request.getAccompaniedBy());
        permission.setContactNumber(request.getContactNumber());
        permission.setParentApproval(request.getParentApproval());
        permission.setNotes(request.getNotes());

        StudentPermission savedPermission = studentPermissionRepository.save(permission);
        logger.info("Successfully created student permission with ID: {}", savedPermission.getId());

        return mapToResponse(savedPermission);
    }

    @Override
    public StudentPermissionResponse updatePermission(Long permissionId, UpdateStudentPermissionRequest request) {
        logger.info("Updating student permission with ID: {}", permissionId);

        StudentPermission permission = studentPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId));

        // Update fields if provided
        if (request.getPermissionType() != null) {
            permission.setPermissionType(request.getPermissionType());
        }
        if (request.getReason() != null) {
            permission.setReason(request.getReason());
        }
        if (request.getPermissionDate() != null) {
            permission.setPermissionDate(request.getPermissionDate());
        }
        if (request.getStartTime() != null) {
            permission.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            permission.setEndTime(request.getEndTime());
        }
        if (request.getExpectedReturnTime() != null) {
            permission.setExpectedReturnTime(request.getExpectedReturnTime());
        }
        if (request.getDestinationAddress() != null) {
            permission.setDestinationAddress(request.getDestinationAddress());
        }
        if (request.getAccompaniedBy() != null) {
            permission.setAccompaniedBy(request.getAccompaniedBy());
        }
        if (request.getContactNumber() != null) {
            permission.setContactNumber(request.getContactNumber());
        }
        if (request.getParentApproval() != null) {
            permission.setParentApproval(request.getParentApproval());
        }
        if (request.getNotes() != null) {
            permission.setNotes(request.getNotes());
        }

        StudentPermission updatedPermission = studentPermissionRepository.save(permission);
        logger.info("Successfully updated student permission with ID: {}", permissionId);

        return mapToResponse(updatedPermission);
    }

    @Override
    public Optional<StudentPermissionResponse> getPermissionById(Long permissionId) {
        logger.debug("Fetching student permission by ID: {}", permissionId);

        return studentPermissionRepository.findById(permissionId)
                .map(this::mapToResponse);
    }

    @Override
    public Page<StudentPermissionResponse> getAllPermissions(Pageable pageable) {
        logger.debug("Fetching all student permissions");

        return studentPermissionRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<StudentPermissionResponse> getPermissionsByStudent(Long studentId, Pageable pageable) {
        logger.debug("Fetching student permissions for student ID: {}", studentId);

        return studentPermissionRepository.findByStudentId(studentId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<StudentPermissionResponse> getPermissionsByTeacher(Long teacherId, Pageable pageable) {
        logger.debug("Fetching student permissions for teacher ID: {}", teacherId);

        return studentPermissionRepository.findByApprovedByDutyTeacherId(teacherId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<StudentPermissionResponse> getPermissionsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        logger.debug("Fetching student permissions between {} and {}", startDate, endDate);

        return studentPermissionRepository.findByPermissionDateBetween(startDate, endDate, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<StudentPermissionResponse> getPermissionsByStatus(String status, Pageable pageable) {
        logger.debug("Fetching student permissions by status: {}", status);

        try {
            StudentPermission.PermissionStatus permissionStatus = StudentPermission.PermissionStatus.valueOf(status.toUpperCase());
            return studentPermissionRepository.findByPermissionStatus(permissionStatus, pageable)
                    .map(this::mapToResponse);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid permission status: " + status);
        }
    }

    @Override
    public Page<StudentPermissionResponse> getPermissionsByReturnStatus(String returnStatus, Pageable pageable) {
        logger.debug("Fetching student permissions by return status: {}", returnStatus);

        try {
            StudentPermission.ReturnStatus status = StudentPermission.ReturnStatus.valueOf(returnStatus.toUpperCase());
            return studentPermissionRepository.findByReturnStatus(status, pageable)
                    .map(this::mapToResponse);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid return status: " + returnStatus);
        }
    }

    @Override
    public void deletePermission(Long permissionId) {
        logger.info("Deleting student permission with ID: {}", permissionId);

        if (!studentPermissionRepository.existsById(permissionId)) {
            throw new RuntimeException("Permission not found with ID: " + permissionId);
        }

        studentPermissionRepository.deleteById(permissionId);
        logger.info("Successfully deleted student permission with ID: {}", permissionId);
    }

    @Override
    public StudentPermissionResponse approvePermission(Long permissionId, Long teacherId, String approvalNotes) {
        logger.info("Approving student permission {} by teacher {}", permissionId, teacherId);

        StudentPermission permission = studentPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        permission.setDutyTeacherApproval(true);
        permission.setApprovedByDutyTeacher(teacher);
        permission.setPermissionStatus(StudentPermission.PermissionStatus.APPROVED);

        if (approvalNotes != null && !approvalNotes.trim().isEmpty()) {
            permission.setNotes((permission.getNotes() != null ? permission.getNotes() + "\n" : "") +
                              "Approval Notes: " + approvalNotes);
        }

        StudentPermission approvedPermission = studentPermissionRepository.save(permission);
        logger.info("Successfully approved student permission {} by teacher {}", permissionId, teacherId);

        return mapToResponse(approvedPermission);
    }

    @Override
    public StudentPermissionResponse rejectPermission(Long permissionId, Long teacherId, String rejectionReason) {
        logger.info("Rejecting student permission {} by teacher {}", permissionId, teacherId);

        StudentPermission permission = studentPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        permission.setDutyTeacherApproval(false);
        permission.setApprovedByDutyTeacher(teacher);
        permission.setPermissionStatus(StudentPermission.PermissionStatus.REJECTED);
        permission.setRejectionReason(rejectionReason);

        StudentPermission rejectedPermission = studentPermissionRepository.save(permission);
        logger.info("Successfully rejected student permission {} by teacher {}", permissionId, teacherId);

        return mapToResponse(rejectedPermission);
    }

    @Override
    public StudentPermissionResponse markAsReturned(Long permissionId, LocalDate returnTime) {
        logger.info("Marking student permission {} as returned", permissionId);

        StudentPermission permission = studentPermissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId));

        permission.setActualReturnTime(java.time.LocalTime.now());
        permission.setReturnStatus(StudentPermission.ReturnStatus.RETURNED_ON_TIME);

        StudentPermission updatedPermission = studentPermissionRepository.save(permission);
        logger.info("Successfully marked student permission {} as returned", permissionId);

        return mapToResponse(updatedPermission);
    }

    @Override
    public List<StudentPermissionResponse> getPendingPermissions() {
        logger.debug("Fetching pending student permissions");

        return studentPermissionRepository.findPendingDutyTeacherApproval()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentPermissionResponse> getOverduePermissions() {
        logger.debug("Fetching overdue student permissions");

        return studentPermissionRepository.findOverduePermissions(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentPermissionResponse> getUnreturnedPermissions() {
        logger.debug("Fetching unreturned student permissions");

        return studentPermissionRepository.findUnreturnedPermissions(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getPermissionStatistics() {
        logger.debug("Fetching student permission statistics");

        List<Object[]> statusStats = studentPermissionRepository.getPermissionStatisticsByStatus();
        List<Object[]> returnStats = studentPermissionRepository.getReturnStatisticsByStatus();
        List<Object[]> typeStats = studentPermissionRepository.getPermissionStatisticsByType();
        List<Object[]> monthlyStats = studentPermissionRepository.getMonthlyPermissionStatistics();

        Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("statusDistribution", statusStats);
        statistics.put("returnStatusDistribution", returnStats);
        statistics.put("typeDistribution", typeStats);
        statistics.put("monthlyTrends", monthlyStats);
        statistics.put("totalRecords", studentPermissionRepository.count());

        return statistics;
    }

    @Override
    public Map<String, Object> getStudentPermissionSummary(Long studentId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching permission summary for student {} between {} and {}", studentId, startDate, endDate);

        long totalPermissions = studentPermissionRepository.countByStudentIdAndPermissionDateBetween(studentId, startDate, endDate);
        long approvedPermissions = studentPermissionRepository.countByStudentIdAndStatus(studentId, StudentPermission.PermissionStatus.APPROVED);

        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("studentId", studentId);
        summary.put("totalPermissions", totalPermissions);
        summary.put("approvedPermissions", approvedPermissions);
        summary.put("approvalRate", totalPermissions > 0 ? (double) approvedPermissions / totalPermissions * 100 : 0);

        return summary;
    }

    @Override
    public boolean existsByStudentAndDate(Long studentId, LocalDate date) {
        return studentPermissionRepository.existsByStudentIdAndPermissionDate(studentId, date);
    }

    @Override
    public List<StudentPermissionResponse> bulkApprovePermissions(List<Long> permissionIds, Long teacherId) {
        logger.info("Bulk approving {} student permissions by teacher {}", permissionIds.size(), teacherId);

        List<StudentPermissionResponse> responses = permissionIds.stream()
                .map(id -> approvePermission(id, teacherId, "Bulk approval"))
                .collect(Collectors.toList());

        logger.info("Successfully bulk approved {} student permissions by teacher {}", responses.size(), teacherId);
        return responses;
    }

    @Override
    public List<Map<String, Object>> getMonthlyPermissionStatistics() {
        logger.debug("Fetching monthly student permission statistics");

        return studentPermissionRepository.getMonthlyPermissionStatistics()
                .stream()
                .map(row -> {
                    Map<String, Object> stat = new java.util.HashMap<>();
                    stat.put("month", row[0]);
                    stat.put("count", row[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<StudentPermissionResponse> advancedSearch(Long studentId, Long teacherId, LocalDate startDate,
                                                        LocalDate endDate, String permissionType, String permissionStatus,
                                                        String returnStatus, Pageable pageable) {
        logger.debug("Advanced search for student permissions with filters");

        StudentPermission.PermissionType type = null;
        if (permissionType != null && !permissionType.isEmpty()) {
            try {
                type = StudentPermission.PermissionType.valueOf(permissionType.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid permission type: {}", permissionType);
            }
        }

        StudentPermission.PermissionStatus status = null;
        if (permissionStatus != null && !permissionStatus.isEmpty()) {
            try {
                status = StudentPermission.PermissionStatus.valueOf(permissionStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid permission status: {}", permissionStatus);
            }
        }

        StudentPermission.ReturnStatus returnStat = null;
        if (returnStatus != null && !returnStatus.isEmpty()) {
            try {
                returnStat = StudentPermission.ReturnStatus.valueOf(returnStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid return status: {}", returnStatus);
            }
        }

        return studentPermissionRepository.advancedSearch(studentId, teacherId, startDate, endDate, type, status, returnStat, pageable)
                .map(this::mapToResponse);
    }

    private StudentPermissionResponse mapToResponse(StudentPermission permission) {
        return StudentPermissionResponse.builder()
                .id(permission.getId())
                .studentId(permission.getStudent().getId())
                .studentName(permission.getStudent().getNamaLengkap())
                .studentNis(permission.getStudent().getNis())
                .permissionType(permission.getPermissionType())
                .reason(permission.getReason())
                .permissionDate(permission.getPermissionDate())
                .startTime(permission.getStartTime())
                .endTime(permission.getEndTime())
                .expectedReturnTime(permission.getExpectedReturnTime())
                .destinationAddress(permission.getDestinationAddress())
                .accompaniedBy(permission.getAccompaniedBy())
                .contactNumber(permission.getContactNumber())
                .parentApproval(permission.getParentApproval())
                .parentApprovalDate(permission.getParentApprovalDate())
                .dutyTeacherApproval(permission.getDutyTeacherApproval())
                .dutyTeacherApprovalDate(permission.getDutyTeacherApprovalDate())
                .approvedByDutyTeacherId(permission.getApprovedByDutyTeacher() != null ? permission.getApprovedByDutyTeacher().getId() : null)
                .approvedByDutyTeacherName(permission.getApprovedByDutyTeacher() != null ? permission.getApprovedByDutyTeacher().getUsername() : null)
                .permissionStatus(permission.getPermissionStatus())
                .rejectionReason(permission.getRejectionReason())
                .actualReturnTime(permission.getActualReturnTime())
                .returnStatus(permission.getReturnStatus())
                .notes(permission.getNotes())
                .attachments(permission.getAttachments())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}