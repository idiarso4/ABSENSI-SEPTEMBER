package com.simsekolah.dto.response;

import com.simsekolah.entity.PklAttendance;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for PKL attendance records
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PklAttendanceResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentNis;
    private String companyName;
    private String companyAddress;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private PklAttendance.AttendanceStatus status;
    private String notes;
    private Double locationLatitude;
    private Double locationLongitude;
    private Boolean verifiedByTeacher;
    private Long supervisingTeacherId;
    private String supervisingTeacherName;
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

    public Boolean getVerifiedByTeacher() {
        return verifiedByTeacher;
    }

    public void setVerifiedByTeacher(Boolean verifiedByTeacher) {
        this.verifiedByTeacher = verifiedByTeacher;
    }

    public Long getSupervisingTeacherId() {
        return supervisingTeacherId;
    }

    public void setSupervisingTeacherId(Long supervisingTeacherId) {
        this.supervisingTeacherId = supervisingTeacherId;
    }

    public String getSupervisingTeacherName() {
        return supervisingTeacherName;
    }

    public void setSupervisingTeacherName(String supervisingTeacherName) {
        this.supervisingTeacherName = supervisingTeacherName;
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