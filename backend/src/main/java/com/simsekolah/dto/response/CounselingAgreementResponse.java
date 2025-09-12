package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselingAgreementResponse {

    private Long id;

    private Long counselingCaseId;

    private String agreementDescription;

    private String agreementType;

    private String termsAndConditions;

    private String parentSignature;

    private String counselorSignature;

    private LocalDate effectiveDate;

    private String status;

    private String complianceStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}