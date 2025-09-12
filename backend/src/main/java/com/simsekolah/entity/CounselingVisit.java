package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "counseling_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_case_id", nullable = false)
    private CounselingCase counselingCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visit_start_time", nullable = false)
    private LocalTime visitStartTime;

    @Column(name = "visit_end_time")
    private LocalTime visitEndTime;

    @Column(name = "visit_type")
    @Enumerated(EnumType.STRING)
    private VisitType visitType = VisitType.INDIVIDUAL;

    @Column(name = "visit_objective", columnDefinition = "TEXT")
    private String visitObjective;

    @Column(name = "discussion_summary", columnDefinition = "TEXT")
    private String discussionSummary;

    @Column(name = "student_response", columnDefinition = "TEXT")
    private String studentResponse;

    @Column(name = "counselor_assessment", columnDefinition = "TEXT")
    private String counselorAssessment;

    @Column(name = "action_plan", columnDefinition = "TEXT")
    private String actionPlan;

    @Column(name = "next_visit_date")
    private LocalDate nextVisitDate;

    @Column(name = "visit_status")
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus = VisitStatus.SCHEDULED;

    @Column(name = "student_mood_rating")
    private Integer studentMoodRating; // 1-5 scale

    @Column(name = "progress_rating")
    private Integer progressRating; // 1-5 scale

    @Column(name = "confidentiality_maintained")
    private Boolean confidentialityMaintained = true;

    @Column(name = "parent_involved")
    private Boolean parentInvolved = false;

    @Column(name = "parent_feedback", columnDefinition = "TEXT")
    private String parentFeedback;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_notes", columnDefinition = "TEXT")
    private String followUpNotes;

    @Column(name = "attachments")
    private String attachments; // JSON array of file URLs

    @Column(name = "location")
    private String location; // Room or location of counseling

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
    }

    public enum VisitType {
        INDIVIDUAL, GROUP, FAMILY, CRISIS
    }

    public enum VisitStatus {
        SCHEDULED, COMPLETED, CANCELLED, NO_SHOW, POSTPONED
    }
}