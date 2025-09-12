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
public class QuestionBankResponse {

    private Long id;

    private String questionText;

    private String questionType;

    private String difficultyLevel;

    private Long subjectId;

    private Long teacherId;

    private String chapterTopic;

    private String correctAnswer;

    private String explanation;

    private Integer points;

    private Boolean isActive;

    private Integer usageCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}