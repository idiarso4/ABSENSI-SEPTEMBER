package com.simsekolah.dto.response;

import com.simsekolah.entity.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Response DTO for assessment data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponse {

    private Long id;
    private String title;
    private String description;
    private AssessmentType type;
    private String typeDescription;

    // Related entities
    private SubjectInfo subject;
    private ClassRoomInfo classRoom;
    private TeacherInfo teacher;

    private BigDecimal maxScore;
    private BigDecimal weight;
    private LocalDate dueDate;

    // Fields from TestService (optional, for test-type assessments)
    private LocalTime assessmentTime;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private Double passingScore;
    private Boolean isPublished;
    private LocalDateTime publishDate;
    private String status; // e.g., DRAFT, PUBLISHED, COMPLETED

    private String academicYear;
    private Integer semester;
    private Boolean isActive;
    private String instructions;

    // Statistics
    private AssessmentStatistics statistics;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectInfo {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassRoomInfo {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssessmentStatistics {
        private Long totalStudents;
        private Long submittedCount;
        private Long gradedCount;
        private Long pendingCount;
        
        private BigDecimal averageScore;
        private BigDecimal highestScore;
        private BigDecimal lowestScore;
        private BigDecimal medianScore;
        
        private Double submissionRate;
        private Double gradingRate;
        
        private List<GradeDistribution> gradeDistribution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradeDistribution {
        private String grade;
        private Long count;
        private Double percentage;
    }
}
