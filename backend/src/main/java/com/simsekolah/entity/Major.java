package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "majors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "duration_years")
    private Integer durationYears;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClassRoom> classRooms;

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
    public boolean isActive() {
        return isActive != null && isActive;
    }

    public String getFullName() {
        if (code != null && name != null) {
            return code + " - " + name;
        }
        return name != null ? name : code;
    }

    public int getTotalClassRooms() {
        return classRooms != null ? classRooms.size() : 0;
    }

    public int getActiveClassRooms() {
        if (classRooms == null) {
            return 0;
        }
        return (int) classRooms.stream()
                .filter(ClassRoom::isActive)
                .count();
    }

    // Method to add a classroom and set the major reference
    public void addClassRoom(ClassRoom classRoom) {
        if (classRooms != null) {
            classRooms.add(classRoom);
            classRoom.setMajor(this);
        }
    }

    // Method to remove a classroom
    public void removeClassRoom(ClassRoom classRoom) {
        if (classRooms != null) {
            classRooms.remove(classRoom);
            classRoom.setMajor(null);
        }
    }
}
