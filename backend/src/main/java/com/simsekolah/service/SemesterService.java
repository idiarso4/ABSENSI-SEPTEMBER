package com.simsekolah.service;

import com.simsekolah.dto.request.CreateSemesterRequest;
import com.simsekolah.dto.response.SemesterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SemesterService {

    SemesterResponse createSemester(CreateSemesterRequest request);

    Page<SemesterResponse> getAllSemesters(Pageable pageable);

    Optional<SemesterResponse> getSemesterById(Long semesterId);

    List<SemesterResponse> getSemestersByAcademicYear(String academicYear);

    Optional<SemesterResponse> getCurrentSemester();

    List<SemesterResponse> getUpcomingSemesters();

    List<SemesterResponse> getSemestersWithOpenRegistration();

    List<SemesterResponse> getSemestersInExamPeriod();

    List<String> getAcademicYears();

    SemesterResponse activateSemester(Long semesterId);

    SemesterResponse completeSemester(Long semesterId);

    Map<String, Object> getSemesterStatistics();

    Optional<SemesterResponse> getSemesterByAcademicYearAndNumber(String academicYear, Integer semesterNumber);

    Page<SemesterResponse> advancedSearch(String academicYear, Integer semesterNumber, String status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<SemesterResponse> getCurrentSemesters();

    List<SemesterResponse> getCompletedSemesters();

    SemesterResponse updateSemesterStatus(Long semesterId, String status);

    void deleteSemester(Long semesterId);

    List<SemesterResponse> getSemestersContainingDate(LocalDate date);

    boolean isRegistrationOpen(String academicYear, Integer semesterNumber);

    boolean isExamPeriod(String academicYear, Integer semesterNumber);
}