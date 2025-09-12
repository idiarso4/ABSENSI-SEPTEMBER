package com.simsekolah.entity;

import com.simsekolah.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the history of changes for an Attendance record.
 */
@Entity
@Table(name = "attendance_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private AttendanceStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private AttendanceStatus newStatus;

    @Column(name = "old_keterangan", length = 500)
    private String oldKeterangan;

    @Column(name = "new_keterangan", length = 500)
    private String newKeterangan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}