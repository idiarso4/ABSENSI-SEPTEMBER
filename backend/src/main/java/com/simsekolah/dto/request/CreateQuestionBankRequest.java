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
public class CreateQuestionBankRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    private String questionType;

    private String difficultyLevel;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    private String chapterTopic;

    private String correctAnswer;

    private String explanation;

    private Integer points;
}