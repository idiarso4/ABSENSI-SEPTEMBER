package com.simsekolah.controller;

import com.simsekolah.service.TeachingActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Contains scheduled tasks (cron jobs) for the application.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final TeachingActivityService teachingActivityService;

    /**
     * Automatically generates teaching activities for the current day based on active schedules.
     * This job runs every day at 1 AM server time.
     * The cron expression is "0 0 1 * * ?": second, minute, hour, day of month, month, day of week.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateTodaysTeachingActivities() {
        log.info("CRON JOB: Starting generation of today's teaching activities.");
        Map<String, Object> result = teachingActivityService.generateTodaysActivities();
        log.info("CRON JOB: Finished generating today's teaching activities. Result: {}", result);
    }
}