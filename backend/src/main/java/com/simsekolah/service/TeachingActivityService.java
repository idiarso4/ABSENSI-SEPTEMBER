package com.simsekolah.service;

import com.simsekolah.dto.response.TeachingActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface TeachingActivityService {

    /**
     * Generates teaching activities for the current day based on active schedules.
     */
    Map<String, Object> generateTodaysActivities();

    /**
     * Gets a paginated list of teaching activities for a specific teacher that are pending attendance/completion.
     */
    Page<TeachingActivityResponse> getPendingActivitiesForTeacher(Long teacherId, Pageable pageable);
}