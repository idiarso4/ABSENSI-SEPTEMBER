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
public class SemesterResponse {

    private Long id;

    private String academicYear;

    private Integer semesterNumber;

    private String semesterName;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate registrationStartDate;

    private LocalDate registrationEndDate;

    private LocalDate examStartDate;

    private LocalDate examEndDate;

    private String status;

    private String description;

    private Integer totalWeeks;

    private Integer teachingWeeks;

    private Integer holidayWeeks;

    private Long totalDays;

    private Long remainingDays;

    private Boolean isActive;

    private Boolean isCompleted;

    private Boolean isCurrentSemester;

    private Boolean isRegistrationOpen;

    private Boolean isExamPeriod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}