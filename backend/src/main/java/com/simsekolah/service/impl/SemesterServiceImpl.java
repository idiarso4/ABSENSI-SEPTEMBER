package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateSemesterRequest;
import com.simsekolah.dto.response.SemesterResponse;
import com.simsekolah.entity.Semester;
import com.simsekolah.enums.SemesterStatus;
import com.simsekolah.repository.SemesterRepository;
import com.simsekolah.service.SemesterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    @Override
    public SemesterResponse createSemester(CreateSemesterRequest request) {
        log.info("Creating semester: {} - {}", request.getAcademicYear(), request.getSemesterNumber());
        
        // Check if semester already exists
        Optional<Semester> existingSemester = semesterRepository.findByAcademicYearAndSemesterNumber(
                request.getAcademicYear(), request.getSemesterNumber());
        
        if (existingSemester.isPresent()) {
            throw new IllegalArgumentException("Semester already exists for academic year " + 
                    request.getAcademicYear() + " semester " + request.getSemesterNumber());
        }
        
        // Calculate total weeks if not provided
        Integer totalWeeks = request.getTotalWeeks();
        if (totalWeeks == null && request.getStartDate() != null && request.getEndDate() != null) {
            totalWeeks = (int) ChronoUnit.WEEKS.between(request.getStartDate(), request.getEndDate());
        }
        
        Semester semester = Semester.builder()
                .academicYear(request.getAcademicYear())
                .semesterName(request.getSemesterName())
                .startDate(request.getStartDate().atStartOfDay())
                .endDate(request.getEndDate().atStartOfDay())
                .registrationStartDate(request.getRegistrationStartDate() != null ? request.getRegistrationStartDate().atStartOfDay() : null)
                .registrationEndDate(request.getRegistrationEndDate() != null ? request.getRegistrationEndDate().atStartOfDay() : null)
                .examStartDate(request.getExamStartDate() != null ? request.getExamStartDate().atStartOfDay() : null)
                .examEndDate(request.getExamEndDate() != null ? request.getExamEndDate().atStartOfDay() : null)
                .status(request.getStatus() != null ? 
                        SemesterStatus.valueOf(request.getStatus().toUpperCase()) : 
                        SemesterStatus.PLANNED)
                .description(request.getDescription())
                .totalWeeks(totalWeeks)
                .teachingWeeks(request.getTeachingWeeks())
                .holidayWeeks(request.getHolidayWeeks())
                .build();

        Semester savedSemester = semesterRepository.save(semester);
        log.info("Successfully created semester with ID: {}", savedSemester.getId());
        
        return mapToResponse(savedSemester);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SemesterResponse> getAllSemesters(Pageable pageable) {
        log.debug("Fetching all semesters with pagination");
        Page<Semester> semesters = semesterRepository.findAll(pageable);
        return semesters.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterResponse> getSemesterById(Long semesterId) {
        log.debug("Fetching semester by ID: {}", semesterId);
        return semesterRepository.findById(semesterId)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersByAcademicYear(String academicYear) {
        log.debug("Fetching semesters by academic year: {}", academicYear);
        List<Semester> semesters = semesterRepository.findByAcademicYearOrderBySemesterNumber(academicYear);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterResponse> getSemesterByAcademicYearAndNumber(String academicYear, Integer semesterNumber) {
        log.debug("Fetching semester by academic year: {} and number: {}", academicYear, semesterNumber);
        return semesterRepository.findByAcademicYearAndSemesterNumber(academicYear, semesterNumber)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getCurrentSemesters() {
        log.debug("Fetching current semesters");
        LocalDate currentDate = LocalDate.now();
        List<Semester> semesters = semesterRepository.findCurrentSemesters(currentDate);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getUpcomingSemesters() {
        log.debug("Fetching upcoming semesters");
        LocalDate currentDate = LocalDate.now();
        List<Semester> semesters = semesterRepository.findUpcomingSemesters(currentDate);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getCompletedSemesters() {
        log.debug("Fetching completed semesters");
        LocalDate currentDate = LocalDate.now();
        List<Semester> semesters = semesterRepository.findCompletedSemesters(currentDate);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersWithOpenRegistration() {
        log.debug("Fetching semesters with open registration");
        LocalDate currentDate = LocalDate.now();
        List<Semester> semesters = semesterRepository.findSemestersWithOpenRegistration(currentDate);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersInExamPeriod() {
        log.debug("Fetching semesters in exam period");
        LocalDate currentDate = LocalDate.now();
        List<Semester> semesters = semesterRepository.findSemestersInExamPeriod(currentDate);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAcademicYears() {
        log.debug("Fetching distinct academic years");
        return semesterRepository.findDistinctAcademicYears();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSemesterStatistics() {
        log.debug("Fetching semester statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        // Total semesters
        long totalSemesters = semesterRepository.count();
        statistics.put("totalSemesters", totalSemesters);
        
        // Semesters by status
        List<Object[]> statusStats = semesterRepository.getSemesterStatisticsByStatus();
        Map<String, Long> statusStatistics = new HashMap<>();
        for (Object[] row : statusStats) {
            statusStatistics.put(row[0].toString(), (Long) row[1]);
        }
        statistics.put("semestersByStatus", statusStatistics);
        
        // Semesters by academic year
        List<Object[]> yearStats = semesterRepository.getSemesterStatisticsByAcademicYear();
        Map<String, Long> yearStatistics = new HashMap<>();
        for (Object[] row : yearStats) {
            yearStatistics.put((String) row[0], (Long) row[1]);
        }
        statistics.put("semestersByAcademicYear", yearStatistics);
        
        // Semesters by semester number
        List<Object[]> numberStats = semesterRepository.getSemesterStatisticsBySemesterNumber();
        Map<String, Long> numberStatistics = new HashMap<>();
        for (Object[] row : numberStats) {
            numberStatistics.put("Semester " + row[0], (Long) row[1]);
        }
        statistics.put("semestersBySemesterNumber", numberStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SemesterResponse> advancedSearch(String academicYear, Integer semesterNumber, String status,
                                               LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Advanced search for semesters with filters");
        
        SemesterStatus statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = SemesterStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid semester status: {}", status);
            }
        }
        
        Page<Semester> semesters = semesterRepository.advancedSearch(
                academicYear, semesterNumber, statusEnum, startDate, endDate, pageable);
        
        return semesters.map(this::mapToResponse);
    }

    @Override
    public SemesterResponse updateSemesterStatus(Long semesterId, String status) {
        log.info("Updating semester status for ID: {} to {}", semesterId, status);
        
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found with ID: " + semesterId));
        
        try {
            String newStatus = status.toUpperCase();
            semester.setStatus(newStatus);
            
            Semester updatedSemester = semesterRepository.save(semester);
            log.info("Successfully updated semester status for ID: {}", semesterId);
            
            return mapToResponse(updatedSemester);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid semester status: " + status);
        }
    }

    @Override
    public void deleteSemester(Long semesterId) {
        log.info("Deleting semester with ID: {}", semesterId);
        
        if (!semesterRepository.existsById(semesterId)) {
            throw new IllegalArgumentException("Semester not found with ID: " + semesterId);
        }
        
        semesterRepository.deleteById(semesterId);
        log.info("Successfully deleted semester with ID: {}", semesterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersContainingDate(LocalDate date) {
        log.debug("Fetching semesters containing date: {}", date);
        List<Semester> semesters = semesterRepository.findSemestersContainingDate(date);
        return semesters.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRegistrationOpen(String academicYear, Integer semesterNumber) {
        log.debug("Checking if registration is open for {} semester {}", academicYear, semesterNumber);
        
        Optional<Semester> semester = semesterRepository.findByAcademicYearAndSemesterNumber(academicYear, semesterNumber);
        return semester.map(Semester::isRegistrationOpen).orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExamPeriod(String academicYear, Integer semesterNumber) {
        log.debug("Checking if it's exam period for {} semester {}", academicYear, semesterNumber);

        Optional<Semester> semester = semesterRepository.findByAcademicYearAndSemesterNumber(academicYear, semesterNumber);
        return semester.map(Semester::isExamPeriod).orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterResponse> getCurrentSemester() {
        log.debug("Fetching current semester");
        LocalDate currentDate = LocalDate.now();
        List<Semester> currentSemesters = semesterRepository.findCurrentSemesters(currentDate);

        // Return the first current semester if any exist
        return currentSemesters.stream()
                .findFirst()
                .map(this::mapToResponse);
    }

    @Override
    public SemesterResponse activateSemester(Long semesterId) {
        log.info("Activating semester: {}", semesterId);

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found with ID: " + semesterId));

        semester.setStatus(SemesterStatus.ACTIVE.name());
        semester.setUpdatedAt(java.time.LocalDateTime.now());

        Semester updatedSemester = semesterRepository.save(semester);
        log.info("Successfully activated semester: {}", semesterId);

        return mapToResponse(updatedSemester);
    }

    @Override
    public SemesterResponse completeSemester(Long semesterId) {
        log.info("Completing semester: {}", semesterId);

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalArgumentException("Semester not found with ID: " + semesterId));

        semester.setStatus(SemesterStatus.COMPLETED.name());
        semester.setUpdatedAt(java.time.LocalDateTime.now());

        Semester updatedSemester = semesterRepository.save(semester);
        log.info("Successfully completed semester: {}", semesterId);

        return mapToResponse(updatedSemester);
    }

    private SemesterResponse mapToResponse(Semester semester) {
        return SemesterResponse.builder()
                .id(semester.getId())
                .academicYear(semester.getAcademicYear())
                .semesterNumber(semester.getSemesterNumber())
                .semesterName(semester.getSemesterName())
                .startDate(semester.getStartDate().toLocalDate())
                .endDate(semester.getEndDate().toLocalDate())
                .registrationStartDate(semester.getRegistrationStartDate() != null ? semester.getRegistrationStartDate().toLocalDate() : null)
                .registrationEndDate(semester.getRegistrationEndDate() != null ? semester.getRegistrationEndDate().toLocalDate() : null)
                .examStartDate(semester.getExamStartDate() != null ? semester.getExamStartDate().toLocalDate() : null)
                .examEndDate(semester.getExamEndDate() != null ? semester.getExamEndDate().toLocalDate() : null)
                .status(semester.getStatus())
                .description(semester.getDescription())
                .totalWeeks(semester.getTotalWeeks())
                .teachingWeeks(semester.getTeachingWeeks())
                .holidayWeeks(semester.getHolidayWeeks())
                .totalDays(semester.getTotalDays())
                .remainingDays(semester.getRemainingDays())
                .isActive(semester.isActive())
                .isCompleted(semester.isCompleted())
                .isCurrentSemester(semester.isCurrentSemester())
                .isRegistrationOpen(semester.isRegistrationOpen())
                .isExamPeriod(semester.isExamPeriod())
                .createdAt(semester.getCreatedAt())
                .updatedAt(semester.getUpdatedAt())
                .build();
    }
}