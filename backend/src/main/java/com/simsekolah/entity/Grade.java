package com.simsekolah.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades", indexes = {
    @Index(name = "idx_grade_student", columnList = "student_id"),
    @Index(name = "idx_grade_subject", columnList = "subject_id"),
    @Index(name = "idx_grade_teacher", columnList = "teacher_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @NotNull
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @NotNull
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @NotNull
    private User teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type")
    @NotNull
    private GradeType gradeType;

    @NotNull
    private BigDecimal score;

    @Column(name = "semester")
    private String semester;

    @Column(name = "academic_year")
    private Integer academicYear;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum GradeType {
        ASSIGNMENT, QUIZ, MIDTERM, FINAL, PROJECT
    }
}