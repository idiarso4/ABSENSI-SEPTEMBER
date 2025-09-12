package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCounselingVisitRequest {

    @NotNull(message = "Counseling case ID is required")
    private Long counselingCaseId;

    private LocalDateTime visitDateTime;

    private String visitType;

    private String visitDescription;

    private Integer durationMinutes;

    private String location;
}