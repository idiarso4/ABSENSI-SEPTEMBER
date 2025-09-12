package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    private Long id;

    private String testTitle;

    private String testDescription;

    private LocalDate testDate;

    private LocalTime testTime;

    private Long subjectId;

    private Long classRoomId;

    private Long teacherId;

    private String testType;

    private Integer durationMinutes;

    private Integer totalQuestions;

    private Double passingScore;

    private Boolean isPublished;

    private LocalDateTime publishDate;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}