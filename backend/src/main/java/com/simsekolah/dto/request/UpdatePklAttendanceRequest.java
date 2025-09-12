package com.simsekolah.dto.request;

import com.simsekolah.entity.PklAttendance;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing PKL attendance record
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePklAttendanceRequest {

    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    @Size(max = 500, message = "Company address must not exceed 500 characters")
    private String companyAddress;

    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private PklAttendance.AttendanceStatus status;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private Double locationLatitude;

    private Double locationLongitude;

    private Long supervisingTeacherId;

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public PklAttendance.AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(PklAttendance.AttendanceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Long getSupervisingTeacherId() {
        return supervisingTeacherId;
    }

    public void setSupervisingTeacherId(Long supervisingTeacherId) {
        this.supervisingTeacherId = supervisingTeacherId;
    }
}