package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselingVisitResponse {

    private Long id;

    private Long counselingCaseId;

    private LocalDateTime visitDateTime;

    private String visitType;

    private String visitDescription;

    private Integer durationMinutes;

    private String location;

    private String status;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}