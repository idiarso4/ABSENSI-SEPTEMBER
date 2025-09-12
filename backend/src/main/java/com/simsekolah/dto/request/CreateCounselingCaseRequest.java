package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCounselingCaseRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Case description is required")
    private String caseDescription;

    private String caseCategory;

    private String severityLevel;

    private String reportedBy;

    private Long counselorId;
}