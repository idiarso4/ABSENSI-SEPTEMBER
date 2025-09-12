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
public class CreateSemesterRequest {

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotNull(message = "Semester number is required")
    private Integer semesterNumber;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private LocalDate registrationStartDate;

    private LocalDate registrationEndDate;

    private LocalDate examStartDate;

    private LocalDate examEndDate;

    private String semesterName;

    private String description;

    private Integer totalWeeks;

    private Integer teachingWeeks;

    private Integer holidayWeeks;

    private String status;
}