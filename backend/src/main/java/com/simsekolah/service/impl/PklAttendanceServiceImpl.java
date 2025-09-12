package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreatePklAttendanceRequest;
import com.simsekolah.dto.request.UpdatePklAttendanceRequest;
import com.simsekolah.dto.response.PklAttendanceResponse;
import com.simsekolah.entity.PklAttendance;
import com.simsekolah.entity.Student;
import com.simsekolah.entity.User;
import com.simsekolah.repository.PklAttendanceRepository;
import com.simsekolah.repository.StudentRepository;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.service.PklAttendanceService;
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
 * Implementation of PklAttendanceService
 */
@Service
@Transactional
public class PklAttendanceServiceImpl implements PklAttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(PklAttendanceServiceImpl.class);

    @Autowired
    private PklAttendanceRepository pklAttendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PklAttendanceResponse createAttendance(CreatePklAttendanceRequest request) {
        logger.info("Creating PKL attendance for student ID: {}", request.getStudentId());

        // Validate student exists
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + request.getStudentId()));

        // Check if attendance already exists for this student on this date
        if (pklAttendanceRepository.existsByStudentIdAndAttendanceDate(request.getStudentId(), request.getAttendanceDate())) {
            throw new RuntimeException("Attendance already exists for student on date: " + request.getAttendanceDate());
        }

        // Get supervising teacher if provided
        User supervisingTeacher = null;
        if (request.getSupervisingTeacherId() != null) {
            supervisingTeacher = userRepository.findById(request.getSupervisingTeacherId())
                    .orElseThrow(() -> new RuntimeException("Supervising teacher not found with ID: " + request.getSupervisingTeacherId()));
        }

        // Create attendance record
        PklAttendance attendance = new PklAttendance();
        attendance.setStudent(student);
        attendance.setCompanyName(request.getCompanyName());
        attendance.setCompanyAddress(request.getCompanyAddress());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(request.getStatus());
        attendance.setNotes(request.getNotes());
        attendance.setLocationLatitude(request.getLocationLatitude());
        attendance.setLocationLongitude(request.getLocationLongitude());
        attendance.setSupervisingTeacher(supervisingTeacher);

        PklAttendance savedAttendance = pklAttendanceRepository.save(attendance);
        logger.info("Successfully created PKL attendance with ID: {}", savedAttendance.getId());

        return mapToResponse(savedAttendance);
    }

    @Override
    public PklAttendanceResponse updateAttendance(Long attendanceId, UpdatePklAttendanceRequest request) {
        logger.info("Updating PKL attendance with ID: {}", attendanceId);

        PklAttendance attendance = pklAttendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + attendanceId));

        // Update fields if provided
        if (request.getCompanyName() != null) {
            attendance.setCompanyName(request.getCompanyName());
        }
        if (request.getCompanyAddress() != null) {
            attendance.setCompanyAddress(request.getCompanyAddress());
        }
        if (request.getAttendanceDate() != null) {
            attendance.setAttendanceDate(request.getAttendanceDate());
        }
        if (request.getCheckInTime() != null) {
            attendance.setCheckInTime(request.getCheckInTime());
        }
        if (request.getCheckOutTime() != null) {
            attendance.setCheckOutTime(request.getCheckOutTime());
        }
        if (request.getStatus() != null) {
            attendance.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            attendance.setNotes(request.getNotes());
        }
        if (request.getLocationLatitude() != null) {
            attendance.setLocationLatitude(request.getLocationLatitude());
        }
        if (request.getLocationLongitude() != null) {
            attendance.setLocationLongitude(request.getLocationLongitude());
        }
        if (request.getSupervisingTeacherId() != null) {
            User supervisingTeacher = userRepository.findById(request.getSupervisingTeacherId())
                    .orElseThrow(() -> new RuntimeException("Supervising teacher not found with ID: " + request.getSupervisingTeacherId()));
            attendance.setSupervisingTeacher(supervisingTeacher);
        }

        PklAttendance updatedAttendance = pklAttendanceRepository.save(attendance);
        logger.info("Successfully updated PKL attendance with ID: {}", attendanceId);

        return mapToResponse(updatedAttendance);
    }

    @Override
    public Optional<PklAttendanceResponse> getAttendanceById(Long attendanceId) {
        logger.debug("Fetching PKL attendance by ID: {}", attendanceId);

        return pklAttendanceRepository.findById(attendanceId)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PklAttendanceResponse> getAllAttendances(Pageable pageable) {
        logger.debug("Fetching all PKL attendances");

        return pklAttendanceRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PklAttendanceResponse> getAttendancesByStudent(Long studentId, Pageable pageable) {
        logger.debug("Fetching PKL attendances for student ID: {}", studentId);

        return pklAttendanceRepository.findByStudentId(studentId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PklAttendanceResponse> getAttendancesByTeacher(Long teacherId, Pageable pageable) {
        logger.debug("Fetching PKL attendances for teacher ID: {}", teacherId);

        return pklAttendanceRepository.findBySupervisingTeacherId(teacherId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PklAttendanceResponse> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        logger.debug("Fetching PKL attendances between {} and {}", startDate, endDate);

        return pklAttendanceRepository.findByAttendanceDateBetween(startDate, endDate, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PklAttendanceResponse> getAttendancesByStatus(String status, Pageable pageable) {
        logger.debug("Fetching PKL attendances by status: {}", status);

        try {
            PklAttendance.AttendanceStatus attendanceStatus = PklAttendance.AttendanceStatus.valueOf(status.toUpperCase());
            return pklAttendanceRepository.findByStatus(attendanceStatus, pageable)
                    .map(this::mapToResponse);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid attendance status: " + status);
        }
    }

    @Override
    public void deleteAttendance(Long attendanceId) {
        logger.info("Deleting PKL attendance with ID: {}", attendanceId);

        if (!pklAttendanceRepository.existsById(attendanceId)) {
            throw new RuntimeException("Attendance not found with ID: " + attendanceId);
        }

        pklAttendanceRepository.deleteById(attendanceId);
        logger.info("Successfully deleted PKL attendance with ID: {}", attendanceId);
    }

    @Override
    public PklAttendanceResponse verifyAttendance(Long attendanceId, Long teacherId) {
        logger.info("Verifying PKL attendance {} by teacher {}", attendanceId, teacherId);

        PklAttendance attendance = pklAttendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + attendanceId));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        attendance.setVerifiedByTeacher(true);
        attendance.setSupervisingTeacher(teacher);

        PklAttendance verifiedAttendance = pklAttendanceRepository.save(attendance);
        logger.info("Successfully verified PKL attendance {} by teacher {}", attendanceId, teacherId);

        return mapToResponse(verifiedAttendance);
    }

    @Override
    public List<PklAttendanceResponse> getUnverifiedAttendances() {
        logger.debug("Fetching unverified PKL attendances");

        return pklAttendanceRepository.findUnverifiedAttendances()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttendanceStatistics() {
        logger.debug("Fetching PKL attendance statistics");

        List<Object[]> statusStats = pklAttendanceRepository.getAttendanceStatisticsByStatus();
        List<Object[]> monthlyStats = pklAttendanceRepository.getMonthlyAttendanceStatistics();

        Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("statusDistribution", statusStats);
        statistics.put("monthlyTrends", monthlyStats);
        statistics.put("totalRecords", pklAttendanceRepository.count());

        return statistics;
    }

    @Override
    public Map<String, Object> getStudentAttendanceSummary(Long studentId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching attendance summary for student {} between {} and {}", studentId, startDate, endDate);

        long totalDays = pklAttendanceRepository.countByStudentIdAndAttendanceDateBetween(studentId, startDate, endDate);
        long presentDays = pklAttendanceRepository.countByStudentIdAndStatus(studentId, PklAttendance.AttendanceStatus.PRESENT);

        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("studentId", studentId);
        summary.put("totalDays", totalDays);
        summary.put("presentDays", presentDays);
        summary.put("absentDays", totalDays - presentDays);
        summary.put("attendanceRate", totalDays > 0 ? (double) presentDays / totalDays * 100 : 0);

        return summary;
    }

    @Override
    public boolean existsByStudentAndDate(Long studentId, LocalDate date) {
        return pklAttendanceRepository.existsByStudentIdAndAttendanceDate(studentId, date);
    }

    @Override
    public List<PklAttendanceResponse> bulkCreateAttendances(List<CreatePklAttendanceRequest> requests) {
        logger.info("Bulk creating {} PKL attendances", requests.size());

        List<PklAttendanceResponse> responses = requests.stream()
                .map(this::createAttendance)
                .collect(Collectors.toList());

        logger.info("Successfully bulk created {} PKL attendances", responses.size());
        return responses;
    }

    @Override
    public List<Map<String, Object>> getMonthlyAttendanceStatistics() {
        logger.debug("Fetching monthly PKL attendance statistics");

        return pklAttendanceRepository.getMonthlyAttendanceStatistics()
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
    public Page<PklAttendanceResponse> advancedSearch(Long studentId, Long teacherId, LocalDate startDate,
                                                      LocalDate endDate, String companyName, String status, Pageable pageable) {
        logger.debug("Advanced search for PKL attendances with filters");

        PklAttendance.AttendanceStatus attendanceStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                attendanceStatus = PklAttendance.AttendanceStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid attendance status: {}", status);
            }
        }

        return pklAttendanceRepository.advancedSearch(studentId, teacherId, startDate, endDate, companyName, attendanceStatus, pageable)
                .map(this::mapToResponse);
    }

    private PklAttendanceResponse mapToResponse(PklAttendance attendance) {
        return PklAttendanceResponse.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudent().getId())
                .studentName(attendance.getStudent().getNamaLengkap())
                .studentNis(attendance.getStudent().getNis())
                .companyName(attendance.getCompanyName())
                .companyAddress(attendance.getCompanyAddress())
                .attendanceDate(attendance.getAttendanceDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus())
                .notes(attendance.getNotes())
                .locationLatitude(attendance.getLocationLatitude())
                .locationLongitude(attendance.getLocationLongitude())
                .verifiedByTeacher(attendance.getVerifiedByTeacher())
                .supervisingTeacherId(attendance.getSupervisingTeacher() != null ? attendance.getSupervisingTeacher().getId() : null)
                .supervisingTeacherName(attendance.getSupervisingTeacher() != null ? attendance.getSupervisingTeacher().getUsername() : null)
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }
}