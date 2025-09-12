package com.simsekolah.dto.response;

import com.simsekolah.entity.StudentPermission;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response DTO for student permission records
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentPermissionResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentNis;
    private StudentPermission.PermissionType permissionType;
    private String reason;
    private LocalDate permissionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime expectedReturnTime;
    private String destinationAddress;
    private String accompaniedBy;
    private String contactNumber;
    private Boolean parentApproval;
    private LocalDateTime parentApprovalDate;
    private Boolean dutyTeacherApproval;
    private LocalDateTime dutyTeacherApprovalDate;
    private Long approvedByDutyTeacherId;
    private String approvedByDutyTeacherName;
    private StudentPermission.PermissionStatus permissionStatus;
    private String rejectionReason;
    private LocalTime actualReturnTime;
    private StudentPermission.ReturnStatus returnStatus;
    private String notes;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNis() {
        return studentNis;
    }

    public void setStudentNis(String studentNis) {
        this.studentNis = studentNis;
    }

    public StudentPermission.PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(StudentPermission.PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getPermissionDate() {
        return permissionDate;
    }

    public void setPermissionDate(LocalDate permissionDate) {
        this.permissionDate = permissionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(LocalTime expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getAccompaniedBy() {
        return accompaniedBy;
    }

    public void setAccompaniedBy(String accompaniedBy) {
        this.accompaniedBy = accompaniedBy;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Boolean getParentApproval() {
        return parentApproval;
    }

    public void setParentApproval(Boolean parentApproval) {
        this.parentApproval = parentApproval;
    }

    public LocalDateTime getParentApprovalDate() {
        return parentApprovalDate;
    }

    public void setParentApprovalDate(LocalDateTime parentApprovalDate) {
        this.parentApprovalDate = parentApprovalDate;
    }

    public Boolean getDutyTeacherApproval() {
        return dutyTeacherApproval;
    }

    public void setDutyTeacherApproval(Boolean dutyTeacherApproval) {
        this.dutyTeacherApproval = dutyTeacherApproval;
    }

    public LocalDateTime getDutyTeacherApprovalDate() {
        return dutyTeacherApprovalDate;
    }

    public void setDutyTeacherApprovalDate(LocalDateTime dutyTeacherApprovalDate) {
        this.dutyTeacherApprovalDate = dutyTeacherApprovalDate;
    }

    public Long getApprovedByDutyTeacherId() {
        return approvedByDutyTeacherId;
    }

    public void setApprovedByDutyTeacherId(Long approvedByDutyTeacherId) {
        this.approvedByDutyTeacherId = approvedByDutyTeacherId;
    }

    public String getApprovedByDutyTeacherName() {
        return approvedByDutyTeacherName;
    }

    public void setApprovedByDutyTeacherName(String approvedByDutyTeacherName) {
        this.approvedByDutyTeacherName = approvedByDutyTeacherName;
    }

    public StudentPermission.PermissionStatus getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(StudentPermission.PermissionStatus permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalTime getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(LocalTime actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public StudentPermission.ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(StudentPermission.ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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