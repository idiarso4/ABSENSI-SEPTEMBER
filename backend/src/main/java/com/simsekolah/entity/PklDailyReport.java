package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pkl_daily_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PklDailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "daily_activities", columnDefinition = "TEXT", nullable = false)
    private String dailyActivities;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "challenges", columnDefinition = "TEXT")
    private String challenges;

    @Column(name = "learning_outcomes", columnDefinition = "TEXT")
    private String learningOutcomes;

    @Column(name = "supervisor_comments", columnDefinition = "TEXT")
    private String supervisorComments;

    @Column(name = "teacher_feedback", columnDefinition = "TEXT")
    private String teacherFeedback;

    @Column(name = "working_hours")
    private Integer workingHours;

    @Column(name = "mood_rating")
    private Integer moodRating; // 1-5 scale

    @Column(name = "productivity_rating")
    private Integer productivityRating; // 1-5 scale

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervising_teacher_id")
    private User supervisingTeacher;

    @Column(name = "attachments")
    private String attachments; // JSON array of file URLs

    @Column(name = "location_latitude")
    private Double locationLatitude;

    @Column(name = "location_longitude")
    private Double locationLongitude;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (reportStatus == ReportStatus.SUBMITTED && submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
        if ((reportStatus == ReportStatus.APPROVED || reportStatus == ReportStatus.REJECTED) && reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }

    public enum ReportStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED, REVISION_REQUIRED
    }
}