package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AssessmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private ClassRoom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "max_score", precision = 10, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Legacy fields for backward compatibility
    @Column(name = "student_id", insertable = false, updatable = false)
    private Long studentId;

    @Column(name = "subject_id_legacy")
    private Long subjectId;

    @Column(name = "assessment_type_legacy")
    private String assessmentType;

    @Column(name = "assessment_date")
    private LocalDateTime assessmentDate;

    @Column(name = "score", precision = 10, scale = 2)
    private BigDecimal score;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods for backward compatibility
    public String getAssessmentType() {
        return assessmentType != null ? assessmentType : (type != null ? type.name() : null);
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
        // Also set the enum type if possible
        if (assessmentType != null) {
            try {
                this.type = AssessmentType.valueOf(assessmentType.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Keep the string value if enum conversion fails
            }
        }
    }

    public Long getSubjectId() {
        return subjectId != null ? subjectId : (subject != null ? subject.getId() : null);
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classroom = classRoom;
    }
}