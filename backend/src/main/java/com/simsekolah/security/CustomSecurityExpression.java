package com.simsekolah.security;

import com.simsekolah.entity.AttendanceHistory;
import com.simsekolah.entity.TeachingActivity;
import com.simsekolah.entity.User;
import com.simsekolah.repository.AttendanceHistoryRepository;
import com.simsekolah.repository.TeachingActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Contains custom security expressions for use with @PreAuthorize.
 */
@Component("customSecurity")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final TeachingActivityRepository teachingActivityRepository;
    private final AttendanceHistoryRepository attendanceHistoryRepository;

    /**
     * Checks if the currently authenticated user is the teacher assigned to the specified TeachingActivity.
     *
     * @param authentication The Spring Security Authentication object.
     * @param activityId     The ID of the TeachingActivity to check.
     * @return true if the user is the teacher for the activity, false otherwise.
     */
    public boolean isTeacherForActivity(Authentication authentication, Long activityId) {
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        Optional<TeachingActivity> activityOpt = teachingActivityRepository.findById(activityId);

        return activityOpt.map(activity -> activity.getTeacher() != null &&
                        activity.getTeacher().getId().equals(currentUserId))
                .orElse(false); // If activity not found, deny access.
    }

    /**
     * Checks if the currently authenticated user is the teacher for the TeachingActivity
     * associated with a given AttendanceHistory record.
     *
     * @param authentication The Spring Security Authentication object.
     * @param historyId      The ID of the AttendanceHistory to check.
     * @return true if the user is the teacher for the activity, false otherwise.
     */
    public boolean isTeacherForAttendanceHistory(Authentication authentication, Long historyId) {
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        Optional<AttendanceHistory> historyOpt = attendanceHistoryRepository.findById(historyId);

        return historyOpt.map(history -> {
            TeachingActivity activity = history.getAttendance().getTeachingActivity();
            return activity.getTeacher() != null &&
                   activity.getTeacher().getId().equals(currentUserId);
        }).orElse(false); // If history not found, deny access.
    }
}