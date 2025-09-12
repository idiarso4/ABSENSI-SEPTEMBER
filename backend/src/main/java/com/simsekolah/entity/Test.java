package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_title", nullable = false)
    private String testTitle;

    @Column(name = "test_description", columnDefinition = "TEXT")
    private String testDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(name = "test_type")
    @Enumerated(EnumType.STRING)
    private TestType testType = TestType.QUIZ;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "total_questions")
    private Integer totalQuestions = 0;

    @Column(name = "total_points")
    private Integer totalPoints = 0;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "is_randomized")
    private Boolean isRandomized = false;

    @Column(name = "show_results_immediately")
    private Boolean showResultsImmediately = false;

    @Column(name = "allow_retake")
    private Boolean allowRetake = false;

    @Column(name = "max_attempts")
    private Integer maxAttempts = 1;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "publish_date")
    private LocalDateTime publishDate;

    @Column(name = "test_status")
    @Enumerated(EnumType.STRING)
    private TestStatus testStatus = TestStatus.DRAFT;

    @Column(name = "attempts_count")
    private Integer attemptsCount = 0;

    @Column(name = "average_score")
    private Double averageScore = 0.0;

    @Column(name = "highest_score")
    private Integer highestScore = 0;

    @Column(name = "lowest_score")
    private Integer lowestScore = 0;

    @Column(name = "completion_rate")
    private Double completionRate = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestSubmission> submissions;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TestType {
        QUIZ, EXAM, ASSIGNMENT, PRACTICE, FINAL_EXAM, MIDTERM_EXAM
    }

    public enum TestStatus {
        DRAFT, PUBLISHED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}