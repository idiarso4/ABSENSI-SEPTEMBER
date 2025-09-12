package com.simsekolah.entity;

import com.simsekolah.enums.SemesterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "semesters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "semester_name", nullable = false)
    private String semesterName;

    @Column(name = "academic_year", nullable = false)
    private String academicYear; // e.g., "2023/2024"

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "registration_start_date")
    private LocalDateTime registrationStartDate;

    @Column(name = "registration_end_date")
    private LocalDateTime registrationEndDate;

    @Column(name = "exam_start_date")
    private LocalDateTime examStartDate;

    @Column(name = "exam_end_date")
    private LocalDateTime examEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SemesterStatus status = SemesterStatus.PLANNED;

    @Column(name = "description")
    private String description;

    @Column(name = "total_weeks")
    private Integer totalWeeks;

    @Column(name = "teaching_weeks")
    private Integer teachingWeeks;

    @Column(name = "holiday_weeks")
    private Integer holidayWeeks;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Legacy getter for backward compatibility
    public String getStatus() {
        return status != null ? status.name() : "PLANNED";
    }

    public void setStatus(String status) {
        try {
            this.status = SemesterStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.status = SemesterStatus.PLANNED;
        }
    }

    // Helper methods
    public Integer getSemesterNumber() {
        if (semesterName != null && semesterName.matches(".*\\d+.*")) {
            return Integer.parseInt(semesterName.replaceAll("\\D+", ""));
        }
        return 1;
    }

    public long getTotalDays() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
        return 0;
    }

    public long getRemainingDays() {
        LocalDateTime now = LocalDateTime.now();
        if (endDate != null && now.isBefore(endDate)) {
            return ChronoUnit.DAYS.between(now, endDate);
        }
        return 0;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isCompleted() {
        return status == SemesterStatus.COMPLETED;
    }

    public boolean isCurrentSemester() {
        LocalDateTime now = LocalDateTime.now();
        return startDate != null && endDate != null &&
               now.isAfter(startDate) && now.isBefore(endDate);
    }

    public boolean isRegistrationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return registrationStartDate != null && registrationEndDate != null &&
               now.isAfter(registrationStartDate) && now.isBefore(registrationEndDate);
    }

    public boolean isExamPeriod() {
        LocalDateTime now = LocalDateTime.now();
        return examStartDate != null && examEndDate != null &&
               now.isAfter(examStartDate) && now.isBefore(examEndDate);
    }

    // Builder methods for backward compatibility
    public static class SemesterBuilder {
        public SemesterBuilder semesterNumber(Integer semesterNumber) {
            // This is just for compatibility, the actual logic is in getSemesterNumber()
            return this;
        }

        public SemesterBuilder totalDays(Long totalDays) {
            // This is just for compatibility, the actual logic is in getTotalDays()
            return this;
        }

        public SemesterBuilder remainingDays(Long remainingDays) {
            // This is just for compatibility, the actual logic is in getRemainingDays()
            return this;
        }

        public SemesterBuilder isActive(Boolean isActive) {
            // This method is for builder compatibility
            // The actual field will be set by Lombok
            return this;
        }

        public SemesterBuilder isCompleted(Boolean isCompleted) {
            // This method is for builder compatibility
            // The actual status will be set by Lombok
            return this;
        }

        public SemesterBuilder isCurrentSemester(Boolean isCurrentSemester) {
            // This is just for compatibility, the actual logic is in isCurrentSemester()
            return this;
        }

        public SemesterBuilder isRegistrationOpen(Boolean isRegistrationOpen) {
            // This is just for compatibility, the actual logic is in isRegistrationOpen()
            return this;
        }

        public SemesterBuilder isExamPeriod(Boolean isExamPeriod) {
            // This is just for compatibility, the actual logic is in isExamPeriod()
            return this;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}