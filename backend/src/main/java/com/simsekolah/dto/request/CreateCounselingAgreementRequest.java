package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCounselingAgreementRequest {

    @NotNull(message = "Counseling case ID is required")
    private Long counselingCaseId;

    private String agreementDescription;

    private String agreementType;

    private String termsAndConditions;

    private String parentSignature;

    private String counselorSignature;
}