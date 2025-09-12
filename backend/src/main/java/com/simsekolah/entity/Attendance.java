package com.simsekolah.entity;

import com.simsekolah.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_activity_id")
    private TeachingActivity teachingActivity;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "subject")
    private String subject;

    @Column(name = "period")
    private Integer period;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private Integer semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @Column(name = "created_by")
    private Long createdBy;

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

    // Helper methods
    public boolean isPresent() {
        return status == AttendanceStatus.PRESENT;
    }

    public boolean isAbsent() {
        return status == AttendanceStatus.ABSENT;
    }

    public boolean isLate() {
        return status == AttendanceStatus.LATE;
    }

    public boolean isExcused() {
        return status == AttendanceStatus.EXCUSED || status == AttendanceStatus.SICK || 
               status == AttendanceStatus.PERMISSION || status == AttendanceStatus.PERMIT;
    }

    public String getStudentName() {
        return student != null ? student.getFullName() : null;
    }

    public String getStudentNis() {
        return student != null ? student.getNis() : null;
    }

    public String getClassName() {
        return student != null && student.getClassRoom() != null ? 
               student.getClassRoom().getClassName() : null;
    }

    public String getSubjectName() {
        return teachingActivity != null && teachingActivity.getSubject() != null ? 
               teachingActivity.getSubject().getName() : subject;
    }

    public String getTeacherName() {
        return teachingActivity != null && teachingActivity.getTeacher() != null ? 
               teachingActivity.getTeacher().getFullName() : null;
    }
}
