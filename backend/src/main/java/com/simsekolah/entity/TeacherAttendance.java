package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "teacher_attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "scheduled_start_time")
    private LocalTime scheduledStartTime;

    @Column(name = "scheduled_end_time")
    private LocalTime scheduledEndTime;

    @Column(name = "actual_check_in_time")
    private LocalDateTime actualCheckInTime;

    @Column(name = "actual_check_out_time")
    private LocalDateTime actualCheckOutTime;

    @Column(name = "attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus = AttendanceStatus.PRESENT;

    @Column(name = "teaching_hours")
    private Integer teachingHours = 0;

    @Column(name = "office_hours")
    private Integer officeHours = 0;

    @Column(name = "total_hours")
    private Integer totalHours = 0;

    @Column(name = "subjects_taught")
    private String subjectsTaught; // JSON array of subject IDs

    @Column(name = "classes_taught")
    private String classesTaught; // JSON array of class IDs

    @Column(name = "activities_performed", columnDefinition = "TEXT")
    private String activitiesPerformed;

    @Column(name = "location_latitude")
    private Double locationLatitude;

    @Column(name = "location_longitude")
    private Double locationLongitude;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "verified_by_system")
    private Boolean verifiedBySystem = false;

    @Column(name = "verification_method")
    @Enumerated(EnumType.STRING)
    private VerificationMethod verificationMethod = VerificationMethod.MANUAL;

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
        // Calculate total hours if not set
        if (totalHours == 0 && teachingHours != null && officeHours != null) {
            totalHours = teachingHours + officeHours;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Recalculate total hours
        if (teachingHours != null && officeHours != null) {
            totalHours = teachingHours + officeHours;
        }
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EARLY_DEPARTURE, ON_LEAVE, SICK
    }

    public enum VerificationMethod {
        MANUAL, BIOMETRIC, GPS, QR_CODE, NFC, SYSTEM_AUTO
    }
}