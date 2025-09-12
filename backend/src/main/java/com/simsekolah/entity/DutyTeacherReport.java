package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "duty_teacher_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyTeacherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_teacher_id", nullable = false)
    private User dutyTeacher;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "shift_start_time", nullable = false)
    private LocalTime shiftStartTime;

    @Column(name = "shift_end_time", nullable = false)
    private LocalTime shiftEndTime;

    @Column(name = "total_students_present")
    private Integer totalStudentsPresent = 0;

    @Column(name = "total_students_absent")
    private Integer totalStudentsAbsent = 0;

    @Column(name = "total_permissions_approved")
    private Integer totalPermissionsApproved = 0;

    @Column(name = "total_permissions_rejected")
    private Integer totalPermissionsRejected = 0;

    @Column(name = "total_late_arrivals")
    private Integer totalLateArrivals = 0;

    @Column(name = "total_early_departures")
    private Integer totalEarlyDepartures = 0;

    @Column(name = "incidents_reported", columnDefinition = "TEXT")
    private String incidentsReported;

    @Column(name = "actions_taken", columnDefinition = "TEXT")
    private String actionsTaken;

    @Column(name = "school_condition_notes", columnDefinition = "TEXT")
    private String schoolConditionNotes;

    @Column(name = "handover_notes", columnDefinition = "TEXT")
    private String handoverNotes;

    @Column(name = "next_duty_teacher")
    private String nextDutyTeacher;

    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_admin_id")
    private User approvedByAdmin;

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

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
        if (reportStatus == ReportStatus.SUBMITTED && submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
        if (reportStatus == ReportStatus.APPROVED && approvedAt == null) {
            approvedAt = LocalDateTime.now();
        }
    }

    public enum ReportStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED, REVISION_REQUIRED
    }
}