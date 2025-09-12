package com.simsekolah.dto.response;

import com.simsekolah.entity.DutyTeacherReport;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response DTO for duty teacher report records
 */
@Builder
public class DutyTeacherReportResponse {

    private Long id;
    private Long dutyTeacherId;
    private String dutyTeacherName;
    private LocalDate reportDate;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private Integer totalStudentsPresent;
    private Integer totalStudentsAbsent;
    private Integer totalPermissionsApproved;
    private Integer totalPermissionsRejected;
    private Integer totalLateArrivals;
    private Integer totalEarlyDepartures;
    private String incidentsReported;
    private String actionsTaken;
    private String schoolConditionNotes;
    private String handoverNotes;
    private String nextDutyTeacher;
    private DutyTeacherReport.ReportStatus reportStatus;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private Long approvedByAdminId;
    private String approvedByAdminName;
    private String approvalNotes;
    private String attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDutyTeacherId() {
        return dutyTeacherId;
    }

    public void setDutyTeacherId(Long dutyTeacherId) {
        this.dutyTeacherId = dutyTeacherId;
    }

    public String getDutyTeacherName() {
        return dutyTeacherName;
    }

    public void setDutyTeacherName(String dutyTeacherName) {
        this.dutyTeacherName = dutyTeacherName;
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

    public DutyTeacherReport.ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(DutyTeacherReport.ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Long getApprovedByAdminId() {
        return approvedByAdminId;
    }

    public void setApprovedByAdminId(Long approvedByAdminId) {
        this.approvedByAdminId = approvedByAdminId;
    }

    public String getApprovedByAdminName() {
        return approvedByAdminName;
    }

    public void setApprovedByAdminName(String approvedByAdminName) {
        this.approvedByAdminName = approvedByAdminName;
    }

    public String getApprovalNotes() {
        return approvalNotes;
    }

    public void setApprovalNotes(String approvalNotes) {
        this.approvalNotes = approvalNotes;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}