package com.simsekolah.repository;

import com.simsekolah.entity.TeachingActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TeachingActivityRepository extends JpaRepository<TeachingActivity, Long> {

    boolean existsByScheduleIdAndDate(Long scheduleId, LocalDate date);

    Page<TeachingActivity> findByTeacherIdAndIsCompletedFalseOrderByDateDescStartTimeDesc(Long teacherId, Pageable pageable);

    /**
     * Counts the number of distinct teaching days for a classroom within a specific month and year.
     */
    @Query("SELECT COUNT(DISTINCT ta.date) FROM TeachingActivity ta " +
           "WHERE ta.classRoom.id = :classRoomId AND YEAR(ta.date) = :year AND MONTH(ta.date) = :month")
    Long countDistinctTeachingDaysByClassRoomAndMonth(
            @Param("classRoomId") Long classRoomId,
            @Param("year") int year,
            @Param("month") int month
    );
}