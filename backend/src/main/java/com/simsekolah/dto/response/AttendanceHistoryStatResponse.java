package com.simsekolah.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing attendance history statistics, such as change counts per teacher.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceHistoryStatResponse {
    private Long teacherId;
    private String teacherName;
    private Long changeCount;
}