package com.simsekolah.service;

import com.simsekolah.dto.request.CreateCounselingAgreementRequest;
import com.simsekolah.dto.response.CounselingAgreementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounselingAgreementService {

    CounselingAgreementResponse createAgreement(CreateCounselingAgreementRequest request);

    Page<CounselingAgreementResponse> getAllAgreements(Pageable pageable);

    Optional<CounselingAgreementResponse> getAgreementById(Long agreementId);

    Page<CounselingAgreementResponse> getAgreementsByCase(Long caseId, Pageable pageable);

    Page<CounselingAgreementResponse> getAgreementsByCounselor(Long counselorId, Pageable pageable);

    CounselingAgreementResponse updateAgreementStatus(Long agreementId, String status);

    Page<CounselingAgreementResponse> getActiveAgreements(Pageable pageable);

    List<CounselingAgreementResponse> getPendingSignatures();

    Map<String, Object> getAgreementStatistics();

    Page<CounselingAgreementResponse> advancedSearch(Long caseId, Long counselorId, LocalDate startDate, LocalDate endDate,
                                                     String agreementType, String agreementStatus, String complianceStatus, Pageable pageable);
}