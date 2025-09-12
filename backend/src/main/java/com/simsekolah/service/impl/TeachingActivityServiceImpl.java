package com.simsekolah.service.impl;

import com.simsekolah.dto.response.TeachingActivityResponse;
import com.simsekolah.entity.Schedule;
import com.simsekolah.entity.TeachingActivity;
import com.simsekolah.repository.ScheduleRepository;
import com.simsekolah.repository.TeachingActivityRepository;
import com.simsekolah.service.TeachingActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeachingActivityServiceImpl implements TeachingActivityService {

    private final TeachingActivityRepository teachingActivityRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public Map<String, Object> generateTodaysActivities() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        log.info("Starting generation of teaching activities for today: {} ({})", today, dayOfWeek);

        List<Schedule> todaysSchedules = scheduleRepository.findByDayOfWeekAndIsActiveTrue(dayOfWeek);
        if (todaysSchedules.isEmpty()) {
            log.info("No active schedules found for {}", dayOfWeek);
            return Map.of("message", "No active schedules for today.", "created", 0, "skipped", 0);
        }

        int createdCount = 0;
        int skippedCount = 0;

        for (Schedule schedule : todaysSchedules) {
            if (teachingActivityRepository.existsByScheduleIdAndDate(schedule.getId(), today)) {
                skippedCount++;
                continue;
            }

            TeachingActivity newActivity = new TeachingActivity();
            newActivity.setSchedule(schedule);
            newActivity.setSubject(schedule.getSubject());
            newActivity.setTeacher(schedule.getTeacher());
            newActivity.setClassRoom(schedule.getClassRoom());
            newActivity.setDate(today);
            newActivity.setStartTime(schedule.getStartTime());
            newActivity.setEndTime(schedule.getEndTime());
            newActivity.setTopic("Sesuai Silabus");
            newActivity.setIsCompleted(false);

            teachingActivityRepository.save(newActivity);
            createdCount++;
        }

        log.info("Finished generating teaching activities. Created: {}, Skipped: {}", createdCount, skippedCount);
        Map<String, Object> result = new HashMap<>();
        result.put("activitiesCreated", createdCount);
        result.put("activitiesSkipped", skippedCount);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeachingActivityResponse> getPendingActivitiesForTeacher(Long teacherId, Pageable pageable) {
        log.debug("Fetching pending activities for teacher ID: {}", teacherId);
        Page<TeachingActivity> activitiesPage = teachingActivityRepository.findByTeacherIdAndIsCompletedFalseOrderByDateDescStartTimeDesc(teacherId, pageable);
        return activitiesPage.map(this::mapToResponse);
    }

    private TeachingActivityResponse mapToResponse(TeachingActivity activity) {
        return TeachingActivityResponse.builder()
                .id(activity.getId())
                .date(activity.getDate())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .topic(activity.getTopic())
                .description(activity.getDescription())
                .isCompleted(activity.getIsCompleted())
                .subject(new TeachingActivityResponse.SubjectInfo(activity.getSubject().getId(), activity.getSubject().getName(), activity.getSubject().getCode()))
                .classRoom(new TeachingActivityResponse.ClassRoomInfo(activity.getClassRoom().getId(), activity.getClassRoom().getName(), activity.getClassRoom().getCode()))
                .build();
    }
}