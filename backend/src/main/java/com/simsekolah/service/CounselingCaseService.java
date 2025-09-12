package com.simsekolah.service;

import com.simsekolah.dto.request.CreateCounselingCaseRequest;
import com.simsekolah.dto.response.CounselingCaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounselingCaseService {

    CounselingCaseResponse createCase(CreateCounselingCaseRequest request);

    Page<CounselingCaseResponse> getAllCases(Pageable pageable);

    Optional<CounselingCaseResponse> getCaseById(Long caseId);

    Page<CounselingCaseResponse> getCasesByStudent(Long studentId, Pageable pageable);

    Page<CounselingCaseResponse> getCasesByCounselor(Long counselorId, Pageable pageable);

    CounselingCaseResponse updateCaseStatus(Long caseId, String status);

    List<CounselingCaseResponse> getActiveCases();

    List<CounselingCaseResponse> getHighPriorityCases();

    Map<String, Object> getCaseStatistics();

    Page<CounselingCaseResponse> advancedSearch(Long studentId, Long counselorId, LocalDate startDate, LocalDate endDate,
                                                 String caseCategory, String severityLevel, String caseStatus, Pageable pageable);
}