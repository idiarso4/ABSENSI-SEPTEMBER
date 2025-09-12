package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestRequest {

    @NotBlank(message = "Test title is required")
    private String testTitle;

    private String testDescription;

    @NotNull(message = "Test date is required")
    private LocalDate testDate;

    private LocalTime testTime;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Class room ID is required")
    private Long classRoomId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotBlank(message = "Test type is required")
    private String testType;

    private Integer durationMinutes;

    private Integer totalQuestions;

    private Double passingScore;
}