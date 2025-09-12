package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "counseling_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(name = "case_number", unique = true)
    private String caseNumber;

    @Column(name = "case_title", nullable = false)
    private String caseTitle;

    @Column(name = "case_description", columnDefinition = "TEXT", nullable = false)
    private String caseDescription;

    @Column(name = "case_category")
    @Enumerated(EnumType.STRING)
    private CaseCategory caseCategory;

    @Column(name = "severity_level")
    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel = SeverityLevel.MEDIUM;

    @Column(name = "reported_date", nullable = false)
    private LocalDate reportedDate;

    @Column(name = "reported_by")
    private String reportedBy;

    @Column(name = "case_status")
    @Enumerated(EnumType.STRING)
    private CaseStatus caseStatus = CaseStatus.OPEN;

    @Column(name = "resolution_summary", columnDefinition = "TEXT")
    private String resolutionSummary;

    @Column(name = "resolution_date")
    private LocalDate resolutionDate;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "confidentiality_level")
    @Enumerated(EnumType.STRING)
    private ConfidentialityLevel confidentialityLevel = ConfidentialityLevel.CONFIDENTIAL;

    @Column(name = "involves_parent")
    private Boolean involvesParent = false;

    @Column(name = "parent_notified")
    private Boolean parentNotified = false;

    @Column(name = "parent_notification_date")
    private LocalDateTime parentNotificationDate;

    @Column(name = "referral_required")
    private Boolean referralRequired = false;

    @Column(name = "referral_details", columnDefinition = "TEXT")
    private String referralDetails;

    @Column(name = "attachments")
    private String attachments; // JSON array of file URLs

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (caseNumber == null) {
            caseNumber = "CASE-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CaseCategory {
        ACADEMIC, BEHAVIORAL, PERSONAL, SOCIAL, HEALTH, OTHER
    }

    public enum SeverityLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum CaseStatus {
        OPEN, IN_PROGRESS, RESOLVED, CLOSED, REFERRED
    }

    public enum ConfidentialityLevel {
        PUBLIC, INTERNAL, CONFIDENTIAL, HIGHLY_CONFIDENTIAL
    }
}