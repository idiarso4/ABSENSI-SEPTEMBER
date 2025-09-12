package com.simsekolah.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating a new duty teacher report
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDutyTeacherReportRequest {

    @NotNull(message = "Duty teacher ID is required")
    private Long dutyTeacherId;

    @NotNull(message = "Report date is required")
    private LocalDate reportDate;

    @NotNull(message = "Shift start time is required")
    private LocalTime shiftStartTime;

    @NotNull(message = "Shift end time is required")
    private LocalTime shiftEndTime;

    @Builder.Default
    private Integer totalStudentsPresent = 0;

    @Builder.Default
    private Integer totalStudentsAbsent = 0;

    @Builder.Default
    private Integer totalPermissionsApproved = 0;

    @Builder.Default
    private Integer totalPermissionsRejected = 0;

    @Builder.Default
    private Integer totalLateArrivals = 0;

    @Builder.Default
    private Integer totalEarlyDepartures = 0;

    @Size(max = 2000, message = "Incidents reported must not exceed 2000 characters")
    private String incidentsReported;

    @Size(max = 2000, message = "Actions taken must not exceed 2000 characters")
    private String actionsTaken;

    @Size(max = 2000, message = "School condition notes must not exceed 2000 characters")
    private String schoolConditionNotes;

    @Size(max = 1000, message = "Handover notes must not exceed 1000 characters")
    private String handoverNotes;

    @Size(max = 255, message = "Next duty teacher must not exceed 255 characters")
    private String nextDutyTeacher;

    // Getters and Setters
    public Long getDutyTeacherId() {
        return dutyTeacherId;
    }

    public void setDutyTeacherId(Long dutyTeacherId) {
        this.dutyTeacherId = dutyTeacherId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public Integer getTotalStudentsPresent() {
        return totalStudentsPresent;
    }

    public void setTotalStudentsPresent(Integer totalStudentsPresent) {
        this.totalStudentsPresent = totalStudentsPresent;
    }

    public Integer getTotalStudentsAbsent() {
        return totalStudentsAbsent;
    }

    public void setTotalStudentsAbsent(Integer totalStudentsAbsent) {
        this.totalStudentsAbsent = totalStudentsAbsent;
    }

    public Integer getTotalPermissionsApproved() {
        return totalPermissionsApproved;
    }

    public void setTotalPermissionsApproved(Integer totalPermissionsApproved) {
        this.totalPermissionsApproved = totalPermissionsApproved;
    }

    public Integer getTotalPermissionsRejected() {
        return totalPermissionsRejected;
    }

    public void setTotalPermissionsRejected(Integer totalPermissionsRejected) {
        this.totalPermissionsRejected = totalPermissionsRejected;
    }

    public Integer getTotalLateArrivals() {
        return totalLateArrivals;
    }

    public void setTotalLateArrivals(Integer totalLateArrivals) {
        this.totalLateArrivals = totalLateArrivals;
    }

    public Integer getTotalEarlyDepartures() {
        return totalEarlyDepartures;
    }

    public void setTotalEarlyDepartures(Integer totalEarlyDepartures) {
        this.totalEarlyDepartures = totalEarlyDepartures;
    }

    public String getIncidentsReported() {
        return incidentsReported;
    }

    public void setIncidentsReported(String incidentsReported) {
        this.incidentsReported = incidentsReported;
    }

    public String getActionsTaken() {
        return actionsTaken;
    }

    public void setActionsTaken(String actionsTaken) {
        this.actionsTaken = actionsTaken;
    }

    public String getSchoolConditionNotes() {
        return schoolConditionNotes;
    }

    public void setSchoolConditionNotes(String schoolConditionNotes) {
        this.schoolConditionNotes = schoolConditionNotes;
    }

    public String getHandoverNotes() {
        return handoverNotes;
    }

    public void setHandoverNotes(String handoverNotes) {
        this.handoverNotes = handoverNotes;
    }

    public String getNextDutyTeacher() {
        return nextDutyTeacher;
    }

    public void setNextDutyTeacher(String nextDutyTeacher) {
        this.nextDutyTeacher = nextDutyTeacher;
    }
}