package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes = 0;

    @Column(name = "answers", columnDefinition = "TEXT")
    private String answers; // JSON object with question_id -> answer mappings

    @Column(name = "score")
    private Integer score = 0;

    @Column(name = "max_score")
    private Integer maxScore = 0;

    @Column(name = "percentage")
    private Double percentage = 0.0;

    @Column(name = "is_passed")
    private Boolean isPassed = false;

    @Column(name = "submission_status")
    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus = SubmissionStatus.IN_PROGRESS;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by_teacher_id")
    private User gradedByTeacher;

    @Column(name = "teacher_feedback", columnDefinition = "TEXT")
    private String teacherFeedback;

    @Column(name = "student_feedback", columnDefinition = "TEXT")
    private String studentFeedback;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "is_flagged")
    private Boolean isFlagged = false;

    @Column(name = "flag_reason", columnDefinition = "TEXT")
    private String flagReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SubmissionStatus {
        IN_PROGRESS, SUBMITTED, GRADED, REVIEWED, FLAGGED
    }
}