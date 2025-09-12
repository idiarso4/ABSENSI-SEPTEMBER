package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pkl_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PklVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervising_teacher_id", nullable = false)
    private User supervisingTeacher;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "visit_purpose", columnDefinition = "TEXT")
    private String visitPurpose;

    @Column(name = "activities_observed", columnDefinition = "TEXT")
    private String activitiesObserved;

    @Column(name = "student_performance", columnDefinition = "TEXT")
    private String studentPerformance;

    @Column(name = "issues_found", columnDefinition = "TEXT")
    private String issuesFound;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "visit_status")
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus = VisitStatus.PLANNED;

    @Column(name = "location_latitude")
    private Double locationLatitude;

    @Column(name = "location_longitude")
    private Double locationLongitude;

    @Column(name = "photos_attached")
    private String photosAttached; // JSON array of photo URLs

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

    public enum VisitStatus {
        PLANNED, COMPLETED, CANCELLED, POSTPONED
    }
}