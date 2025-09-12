package com.simsekolah.service;

import com.simsekolah.dto.request.CreateAcademicCalendarRequest;
import com.simsekolah.dto.response.AcademicCalendarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AcademicCalendarService {

    AcademicCalendarResponse createEvent(CreateAcademicCalendarRequest request);

    Page<AcademicCalendarResponse> getAllEvents(Pageable pageable);

    Optional<AcademicCalendarResponse> getEventById(Long eventId);

    List<AcademicCalendarResponse> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

    Page<AcademicCalendarResponse> getEventsByAcademicYear(String academicYear, Pageable pageable);

    List<AcademicCalendarResponse> getHolidays(LocalDate startDate, LocalDate endDate);

    List<AcademicCalendarResponse> getExamPeriods(LocalDate startDate, LocalDate endDate);

    List<AcademicCalendarResponse> getTeachingPeriods(LocalDate startDate, LocalDate endDate);

    List<AcademicCalendarResponse> getTodaysEvents();

    List<AcademicCalendarResponse> getUpcomingEvents(int daysAhead);

    List<String> getAcademicYears();

    List<Integer> getSemestersByAcademicYear(String academicYear);

    Map<String, Object> getCalendarStatistics();

    Page<AcademicCalendarResponse> advancedSearch(String eventType, String academicYear, Integer semester,
                                                  LocalDate startDate, LocalDate endDate, Boolean isHoliday,
                                                  Boolean isExamPeriod, Boolean isTeachingPeriod, Pageable pageable);

    /**
     * Checks which of the given dates are holidays.
     * @return A Set of LocalDates that are holidays from the input list.
     */
    java.util.Set<LocalDate> findHolidaysInDates(List<LocalDate> dates);

    /**
     * Checks if there is any holiday on a specific day of the week within a date range.
     * @param dayOfWeek The day of the week to check (e.g., MONDAY).
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return true if a holiday exists on that day of the week, false otherwise.
     */
    boolean hasHolidayOnDayOfWeek(java.time.DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate);
}