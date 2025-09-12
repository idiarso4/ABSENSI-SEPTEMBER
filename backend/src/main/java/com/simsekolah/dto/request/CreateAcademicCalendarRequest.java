package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAcademicCalendarRequest {

    @NotBlank(message = "Event title is required")
    private String eventTitle;

    private String eventDescription;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    @NotBlank(message = "Event type is required")
    private String eventType;

    private String academicYear;

    private Integer semester;

    @Builder.Default
    private Boolean isHoliday = false;

    @Builder.Default
    private Boolean isExamPeriod = false;

    @Builder.Default
    private Boolean isTeachingPeriod = false;
}