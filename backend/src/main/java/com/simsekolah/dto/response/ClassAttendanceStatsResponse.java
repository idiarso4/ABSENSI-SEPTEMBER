package com.simsekolah.dto.response;

import com.simsekolah.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for representing monthly attendance statistics for a specific classroom.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassAttendanceStatsResponse {
    private Long classRoomId;
    private String classRoomName;
    private String period; // e.g., "2023-10"
    private long totalStudents;
    private long totalTeachingDaysInMonth;
    private long totalPossibleAttendances; // totalStudents * totalTeachingDays
    private Map<AttendanceStatus, Long> statusCounts;
    private double attendanceRate; // (Present / (Present + Sick + Permit + Absent)) * 100
    private double presenceRate; // (Present / totalPossibleAttendances) * 100
}