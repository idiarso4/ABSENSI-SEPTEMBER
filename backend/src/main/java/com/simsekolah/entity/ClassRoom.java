package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class_rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "grade_level")
    private Integer gradeLevel;

    @Column(name = "class_status")
    private String classStatus;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "current_students")
    @Builder.Default
    private Integer currentStudents = 0;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private Integer semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private User homeroomTeacher;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "building")
    private String building;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

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
    public boolean isFull() {
        return capacity != null && currentStudents != null && currentStudents >= capacity;
    }

    public boolean hasAvailableSpace() {
        return !isFull();
    }

    public int getAvailableSpaces() {
        if (capacity == null || currentStudents == null) {
            return 0;
        }
        return Math.max(0, capacity - currentStudents);
    }

    public double getOccupancyRate() {
        if (capacity == null || capacity == 0 || currentStudents == null) {
            return 0.0;
        }
        return (double) currentStudents / capacity * 100;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (className != null) {
            fullName.append(className);
        }
        if (gradeLevel != null) {
            if (fullName.length() > 0) {
                fullName.append(" - ");
            }
            fullName.append("Grade ").append(gradeLevel);
        }
        return fullName.toString();
    }

    public String getLocation() {
        StringBuilder location = new StringBuilder();
        if (building != null) {
            location.append(building);
        }
        if (floor != null) {
            if (location.length() > 0) {
                location.append(" - ");
            }
            location.append("Floor ").append(floor);
        }
        if (roomNumber != null) {
            if (location.length() > 0) {
                location.append(" - ");
            }
            location.append("Room ").append(roomNumber);
        }
        return location.toString();
    }

    public String getHomeroomTeacherName() {
        return homeroomTeacher != null ? homeroomTeacher.getFullName() : null;
    }

    public User getWaliKelas() {
        return homeroomTeacher;
    }

    public String getMajorName() {
        return major != null ? major.getName() : null;
    }

    public boolean isActive() {
        return isActive != null && isActive;
    }

    public void updateStudentCount() {
        if (students != null) {
            this.currentStudents = students.size();
        }
    }

    // Additional methods for backward compatibility
    public String getName() {
        return className;
    }

    public Integer getGrade() {
        return gradeLevel;
    }

    public String getClassName() {
        return className;
    }

    // Additional methods for compatibility
    public String getCode() {
        return className; // Using className as code for now
    }

    public Integer getCurrentEnrollment() {
        return currentStudents;
    }

    public void setName(String name) {
        this.className = name;
    }

    public void setGrade(Integer grade) {
        this.gradeLevel = grade;
    }

    public void setCurrentEnrollment(Integer enrollment) {
        this.currentStudents = enrollment;
    }
}
