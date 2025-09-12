package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pkl_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PklActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "department_section")
    private String departmentSection;

    @Column(name = "activity_description", columnDefinition = "TEXT", nullable = false)
    private String activityDescription;

    @Column(name = "skills_learned", columnDefinition = "TEXT")
    private String skillsLearned;

    @Column(name = "challenges_faced", columnDefinition = "TEXT")
    private String challengesFaced;

    @Column(name = "solutions_applied", columnDefinition = "TEXT")
    private String solutionsApplied;

    @Column(name = "working_hours")
    private Integer workingHours;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;

    @Column(name = "student_reflection", columnDefinition = "TEXT")
    private String studentReflection;

    @Column(name = "activity_status")
    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus = ActivityStatus.DRAFT;

    @Column(name = "approved_by_teacher")
    private Boolean approvedByTeacher = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervising_teacher_id")
    private User supervisingTeacher;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "attachments")
    private String attachments; // JSON array of file URLs

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

    public enum ActivityStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED, REVISION_REQUIRED
    }
}