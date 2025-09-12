package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "student_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "permission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "permission_date", nullable = false)
    private LocalDate permissionDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "expected_return_time")
    private LocalTime expectedReturnTime;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "accompanied_by")
    private String accompaniedBy;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "parent_approval")
    private Boolean parentApproval = false;

    @Column(name = "parent_approval_date")
    private LocalDateTime parentApprovalDate;

    @Column(name = "duty_teacher_approval")
    private Boolean dutyTeacherApproval = false;

    @Column(name = "duty_teacher_approval_date")
    private LocalDateTime dutyTeacherApprovalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_duty_teacher_id")
    private User approvedByDutyTeacher;

    @Column(name = "permission_status")
    @Enumerated(EnumType.STRING)
    private PermissionStatus permissionStatus = PermissionStatus.PENDING;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "actual_return_time")
    private LocalTime actualReturnTime;

    @Column(name = "return_status")
    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus = ReturnStatus.PENDING;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

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

    public enum PermissionType {
        ENTRY_LATE, EXIT_EARLY, MEDICAL, FAMILY_MATTER, OTHER
    }

    public enum PermissionStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }

    public enum ReturnStatus {
        PENDING, RETURNED_ON_TIME, RETURNED_LATE, NOT_RETURNED
    }
}