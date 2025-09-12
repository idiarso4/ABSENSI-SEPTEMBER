package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateAcademicCalendarRequest;
import com.simsekolah.dto.response.AcademicCalendarResponse;
import com.simsekolah.entity.AcademicCalendar;
import com.simsekolah.enums.EventType;
import com.simsekolah.repository.AcademicCalendarRepository;
import com.simsekolah.service.AcademicCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AcademicCalendarServiceImpl implements AcademicCalendarService {

    private final AcademicCalendarRepository academicCalendarRepository;

    @Override
    public AcademicCalendarResponse createEvent(CreateAcademicCalendarRequest request) {
        log.info("Creating academic calendar event: {}", request.getEventTitle());
        
        AcademicCalendar event = AcademicCalendar.builder()
                .eventName(request.getEventTitle())
                .description(request.getEventDescription())
                .eventDate(request.getEventDate().atStartOfDay())
                .eventType(EventType.valueOf(request.getEventType().toUpperCase()))
                .academicYear(request.getAcademicYear())
                .build();

        AcademicCalendar savedEvent = academicCalendarRepository.save(event);
        log.info("Successfully created academic calendar event with ID: {}", savedEvent.getId());
        
        return mapToResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicCalendarResponse> getAllEvents(Pageable pageable) {
        log.debug("Fetching all academic calendar events with pagination");
        Page<AcademicCalendar> events = academicCalendarRepository.findAll(pageable);
        return events.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicCalendarResponse> getEventById(Long eventId) {
        log.debug("Fetching academic calendar event by ID: {}", eventId);
        return academicCalendarRepository.findById(eventId)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching academic calendar events by date range: {} to {}", startDate, endDate);
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : null;
        List<AcademicCalendar> events = academicCalendarRepository.findEventsInDateRange(start, end);
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicCalendarResponse> getEventsByAcademicYear(String academicYear, Pageable pageable) {
        log.debug("Fetching academic calendar events by academic year: {}", academicYear);
        Page<AcademicCalendar> events = academicCalendarRepository.findByAcademicYear(academicYear, pageable);
        return events.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getHolidays(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching academic calendar holidays");
        List<AcademicCalendar> holidays;
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            holidays = academicCalendarRepository.findHolidaysInDateRange(start, end);
        } else {
            holidays = academicCalendarRepository.findByIsHoliday(true);
        }
        
        return holidays.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getExamPeriods(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching academic calendar exam periods");
        List<AcademicCalendar> examPeriods;
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            examPeriods = academicCalendarRepository.findExamPeriodsInDateRange(start, end);
        } else {
            examPeriods = academicCalendarRepository.findByIsExamPeriod(true);
        }
        
        return examPeriods.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getTeachingPeriods(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching academic calendar teaching periods");
        List<AcademicCalendar> teachingPeriods;
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            teachingPeriods = academicCalendarRepository.findTeachingPeriodsInDateRange(start, end);
        } else {
            teachingPeriods = academicCalendarRepository.findByIsTeachingPeriod(true);
        }
        
        return teachingPeriods.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getTodaysEvents() {
        log.debug("Fetching today's academic calendar events");
        LocalDate today = LocalDate.now();
        List<AcademicCalendar> events = academicCalendarRepository.findEventsForDate(today.atStartOfDay());
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getUpcomingEvents(int daysAhead) {
        log.debug("Fetching upcoming academic calendar events, days ahead: {}", daysAhead);
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(daysAhead);
        List<AcademicCalendar> events = academicCalendarRepository.findUpcomingEvents(today.atStartOfDay(), futureDate.atTime(23, 59, 59));
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAcademicYears() {
        log.debug("Fetching distinct academic years");
        return academicCalendarRepository.findDistinctAcademicYears();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getSemestersByAcademicYear(String academicYear) {
        log.debug("Fetching semesters for academic year: {}", academicYear);
        List<Long> semesterIds = academicCalendarRepository.findDistinctSemesterIdsByAcademicYear(academicYear);
        return semesterIds.stream()
                .map(Long::intValue)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCalendarStatistics() {
        log.debug("Fetching academic calendar statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        // Total events
        long totalEvents = academicCalendarRepository.count();
        statistics.put("totalEvents", totalEvents);
        
        // Events by type
        List<Object[]> eventsByType = academicCalendarRepository.getEventStatisticsByType();
        Map<String, Long> eventTypeStats = new HashMap<>();
        for (Object[] row : eventsByType) {
            eventTypeStats.put(row[0].toString(), (Long) row[1]);
        }
        statistics.put("eventsByType", eventTypeStats);
        
        // Holiday statistics
        List<Object[]> holidayStats = academicCalendarRepository.getHolidayStatistics();
        Map<String, Long> holidayStatistics = new HashMap<>();
        for (Object[] row : holidayStats) {
            String key = (Boolean) row[0] ? "holidays" : "nonHolidays";
            holidayStatistics.put(key, (Long) row[1]);
        }
        statistics.put("holidayStatistics", holidayStatistics);
        
        // Monthly statistics
        List<Object[]> monthlyStats = academicCalendarRepository.getMonthlyEventStatistics();
        Map<String, Long> monthlyStatistics = new HashMap<>();
        for (Object[] row : monthlyStats) {
            monthlyStatistics.put((String) row[0], (Long) row[1]);
        }
        statistics.put("monthlyStatistics", monthlyStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicCalendarResponse> advancedSearch(String eventType, String academicYear, Integer semester,
                                                        LocalDate startDate, LocalDate endDate, Boolean isHoliday,
                                                        Boolean isExamPeriod, Boolean isTeachingPeriod, Pageable pageable) {
        log.debug("Advanced search for academic calendar events with filters");

        EventType eventTypeEnum = null;
        if (eventType != null && !eventType.isEmpty()) {
            try {
                eventTypeEnum = EventType.valueOf(eventType.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid event type: {}", eventType);
            }
        }

        // Convert semester Integer to Long for the new method signature
        Long semesterId = semester != null ? semester.longValue() : null;

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : null;
        Page<AcademicCalendar> events = academicCalendarRepository.advancedSearch(
                eventTypeEnum, academicYear, semesterId, start, end, pageable);

        return events.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LocalDate> findHolidaysInDates(List<LocalDate> dates) {
        log.debug("Checking which dates are holidays from {} dates", dates.size());
        
        if (dates == null || dates.isEmpty()) {
            return new HashSet<>();
        }
        
        LocalDate minDate = dates.stream().min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate maxDate = dates.stream().max(LocalDate::compareTo).orElse(LocalDate.now());
        
        List<AcademicCalendar> holidays = academicCalendarRepository.findHolidaysInDateRange(minDate.atStartOfDay(), maxDate.atTime(23, 59, 59));
        Set<LocalDate> holidayDates = holidays.stream()
                .map(event -> event.getEventDate().toLocalDate())
                .collect(Collectors.toSet());
        
        return dates.stream()
                .filter(holidayDates::contains)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasHolidayOnDayOfWeek(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate) {
        log.debug("Checking if there's a holiday on {} between {} and {}", dayOfWeek, startDate, endDate);
        
    LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
    LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : null;

    List<AcademicCalendar> holidays = academicCalendarRepository.findHolidaysInDateRange(start, end);

    return holidays.stream()
        .anyMatch(holiday -> holiday.getEventDate().getDayOfWeek() == dayOfWeek);
    }

    private AcademicCalendarResponse mapToResponse(AcademicCalendar event) {
        return AcademicCalendarResponse.builder()
                .id(event.getId())
                .eventTitle(event.getEventTitle())
                .eventDescription(event.getEventDescription())
                .eventDate(event.getEventDate().toLocalDate())
                .eventType(event.getEventType())
                .academicYear(event.getAcademicYear())
                .build();
    }
}