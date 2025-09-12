package com.simsekolah.dto.request;

import com.simsekolah.entity.StudentPermission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating a new student permission
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentPermissionRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Permission type is required")
    private StudentPermission.PermissionType permissionType;

    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;

    @NotNull(message = "Permission date is required")
    private LocalDate permissionDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime expectedReturnTime;

    @Size(max = 500, message = "Destination address must not exceed 500 characters")
    private String destinationAddress;

    @Size(max = 255, message = "Accompanied by must not exceed 255 characters")
    private String accompaniedBy;

    @Size(max = 50, message = "Contact number must not exceed 50 characters")
    private String contactNumber;

    @Builder.Default
    private Boolean parentApproval = false;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}