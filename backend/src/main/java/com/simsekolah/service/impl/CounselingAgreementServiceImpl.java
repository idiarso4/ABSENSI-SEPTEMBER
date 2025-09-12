package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateCounselingAgreementRequest;
import com.simsekolah.dto.response.CounselingAgreementResponse;
import com.simsekolah.service.CounselingAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CounselingAgreementServiceImpl implements CounselingAgreementService {

    @Override
    public CounselingAgreementResponse createAgreement(CreateCounselingAgreementRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Page<CounselingAgreementResponse> getAllAgreements(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Optional<CounselingAgreementResponse> getAgreementById(Long agreementId) {
        return Optional.empty();
    }

    @Override
    public Page<CounselingAgreementResponse> getAgreementsByCase(Long caseId, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Page<CounselingAgreementResponse> getAgreementsByCounselor(Long counselorId, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public CounselingAgreementResponse updateAgreementStatus(Long agreementId, String status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Page<CounselingAgreementResponse> getActiveAgreements(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public List<CounselingAgreementResponse> getPendingSignatures() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getAgreementStatistics() {
        return Collections.emptyMap();
    }

    @Override
    public Page<CounselingAgreementResponse> advancedSearch(Long caseId, Long counselorId, LocalDate startDate, LocalDate endDate, String agreementType, String agreementStatus, String complianceStatus, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}
