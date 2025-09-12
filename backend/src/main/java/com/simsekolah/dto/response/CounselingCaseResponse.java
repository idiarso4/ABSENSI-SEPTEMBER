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
public class CounselingCaseResponse {

    private Long id;

    private Long studentId;

    private String caseCategory;

    private String caseDescription;

    private String severityLevel;

    private String reportedBy;

    private Long counselorId;

    private LocalDate reportedDate;

    private String status;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}