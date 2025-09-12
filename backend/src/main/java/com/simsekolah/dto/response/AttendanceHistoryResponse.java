package com.simsekolah.dto.response;

import com.simsekolah.entity.AttendanceHistory;
import com.simsekolah.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing an attendance history record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceHistoryResponse {

    private Long id;
    private Long attendanceId;
    private AttendanceStatus oldStatus;
    private AttendanceStatus newStatus;
    private String oldKeterangan;
    private String newKeterangan;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public static AttendanceHistoryResponse from(AttendanceHistory history) {
        if (history == null) {
            return null;
        }

        String updatedByUsername = "System";
        if (history.getUpdatedBy() != null) {
            updatedByUsername = history.getUpdatedBy().getFirstName() + " " + history.getUpdatedBy().getLastName();
        }

        return AttendanceHistoryResponse.builder()
                .id(history.getId())
                .attendanceId(history.getAttendance().getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .oldKeterangan(history.getOldKeterangan())
                .newKeterangan(history.getNewKeterangan())
                .updatedBy(updatedByUsername)
                .updatedAt(history.getUpdatedAt())
                .build();
    }
}