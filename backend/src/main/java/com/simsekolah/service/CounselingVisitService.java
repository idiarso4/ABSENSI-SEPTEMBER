package com.simsekolah.service;

import com.simsekolah.dto.request.CreateCounselingVisitRequest;
import com.simsekolah.dto.response.CounselingVisitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounselingVisitService {

    CounselingVisitResponse createVisit(CreateCounselingVisitRequest request);

    Page<CounselingVisitResponse> getAllVisits(Pageable pageable);

    Optional<CounselingVisitResponse> getVisitById(Long visitId);

    Page<CounselingVisitResponse> getVisitsByCase(Long caseId, Pageable pageable);

    Page<CounselingVisitResponse> getVisitsByCounselor(Long counselorId, Pageable pageable);

    CounselingVisitResponse updateVisitStatus(Long visitId, String status);

    List<CounselingVisitResponse> getTodaysScheduledVisits();

    Page<CounselingVisitResponse> getCompletedVisits(Pageable pageable);

    Map<String, Object> getVisitStatistics();

    Page<CounselingVisitResponse> advancedSearch(Long caseId, Long counselorId, LocalDate startDate, LocalDate endDate,
                                                  String visitType, String visitStatus, Pageable pageable);
}