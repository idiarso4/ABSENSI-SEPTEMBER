package com.simsekolah.repository;

import com.simsekolah.dto.response.AttendanceHistoryStatResponse;
import com.simsekolah.entity.AttendanceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistory, Long> {

    List<AttendanceHistory> findByAttendanceIdOrderByUpdatedAtDesc(Long attendanceId);

    Page<AttendanceHistory> findByUpdatedByIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);

    /**
     * Get change counts grouped by teacher (updatedBy).
     * Returns a page of AttendanceHistoryStatResponse containing teacherId, teacherName and changeCount.
     */
    @Query("SELECT new com.simsekolah.dto.response.AttendanceHistoryStatResponse(" +
        "u.id, CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')), COUNT(h)) " +
        "FROM AttendanceHistory h JOIN h.updatedBy u " +
        "GROUP BY u.id, u.firstName, u.lastName")
    Page<AttendanceHistoryStatResponse> getChangeCountPerTeacher(Pageable pageable);

}