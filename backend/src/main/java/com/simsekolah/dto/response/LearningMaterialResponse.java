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
public class LearningMaterialResponse {

    private Long id;

    private String title;

    private String description;

    private Long subjectId;

    private Long teacherId;

    private String materialType;

    private String difficultyLevel;

    private Integer chapterNumber;

    private String fileUrl;

    private Boolean isPublished;

    private LocalDateTime publishDate;

    private Integer viewCount;

    private Integer downloadCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}