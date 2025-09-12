package com.simsekolah.repository;

import com.simsekolah.entity.AcademicCalendar;
import com.simsekolah.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {

    List<AcademicCalendar> findByEventDate(LocalDateTime eventDate);

    List<AcademicCalendar> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<AcademicCalendar> findByEventType(EventType eventType);

    List<AcademicCalendar> findByAcademicYear(String academicYear);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.semester.id = :semesterId")
    List<AcademicCalendar> findBySemesterId(@Param("semesterId") Long semesterId);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'HOLIDAY'")
    List<AcademicCalendar> findByIsHoliday(Boolean isHoliday);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'EXAM'")
    List<AcademicCalendar> findByIsExamPeriod(Boolean isExamPeriod);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'TEACHING_PERIOD'")
    List<AcademicCalendar> findByIsTeachingPeriod(Boolean isTeachingPeriod);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.academicYear = :academicYear AND a.semester.id = :semesterId")
    List<AcademicCalendar> findByAcademicYearAndSemesterId(@Param("academicYear") String academicYear, @Param("semesterId") Long semesterId);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventDate BETWEEN :startDate AND :endDate ORDER BY a.eventDate")
    List<AcademicCalendar> findEventsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.academicYear = :academicYear AND a.eventDate BETWEEN :startDate AND :endDate ORDER BY a.eventDate")
    List<AcademicCalendar> findEventsByAcademicYearAndDateRange(@Param("academicYear") String academicYear,
                                                               @Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'HOLIDAY' AND a.eventDate BETWEEN :startDate AND :endDate ORDER BY a.eventDate")
    List<AcademicCalendar> findHolidaysInDateRange(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'EXAM' AND a.eventDate BETWEEN :startDate AND :endDate ORDER BY a.eventDate")
    List<AcademicCalendar> findExamPeriodsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventType = 'TEACHING_PERIOD' AND a.eventDate BETWEEN :startDate AND :endDate ORDER BY a.eventDate")
    List<AcademicCalendar> findTeachingPeriodsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) FROM AcademicCalendar a WHERE a.academicYear = :academicYear")
    long countByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT COUNT(a) FROM AcademicCalendar a WHERE a.academicYear = :academicYear AND a.eventType = 'HOLIDAY'")
    long countHolidaysByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT COUNT(a) FROM AcademicCalendar a WHERE a.academicYear = :academicYear AND a.eventType = 'EXAM'")
    long countExamPeriodsByAcademicYear(@Param("academicYear") String academicYear);

    // Pagination support
    Page<AcademicCalendar> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<AcademicCalendar> findByAcademicYear(String academicYear, Pageable pageable);

    Page<AcademicCalendar> findByEventType(EventType eventType, Pageable pageable);

    @Query("SELECT a FROM AcademicCalendar a WHERE " +
           "(:eventType IS NULL OR a.eventType = :eventType) AND " +
           "(:academicYear IS NULL OR a.academicYear = :academicYear) AND " +
           "(:semesterId IS NULL OR a.semester.id = :semesterId) AND " +
           "(:startDate IS NULL OR a.eventDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.eventDate <= :endDate)")
    Page<AcademicCalendar> advancedSearch(@Param("eventType") EventType eventType,
                                         @Param("academicYear") String academicYear,
                                         @Param("semesterId") Long semesterId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         Pageable pageable);

    @Query("SELECT a.eventType as type, COUNT(a) as count FROM AcademicCalendar a GROUP BY a.eventType")
    List<Object[]> getEventStatisticsByType();

    @Query("SELECT CASE WHEN a.eventType = 'HOLIDAY' THEN true ELSE false END as holiday, COUNT(a) as count FROM AcademicCalendar a GROUP BY CASE WHEN a.eventType = 'HOLIDAY' THEN true ELSE false END")
    List<Object[]> getHolidayStatistics();

    @Query("SELECT CASE WHEN a.eventType = 'EXAM' THEN true ELSE false END as examPeriod, COUNT(a) as count FROM AcademicCalendar a GROUP BY CASE WHEN a.eventType = 'EXAM' THEN true ELSE false END")
    List<Object[]> getExamPeriodStatistics();

    @Query(value = "SELECT DATE_FORMAT(a.event_date, '%Y-%m') as month, COUNT(*) as count FROM academic_calendar a GROUP BY DATE_FORMAT(a.event_date, '%Y-%m') ORDER BY month DESC", nativeQuery = true)
    List<Object[]> getMonthlyEventStatistics();

    @Query("SELECT DISTINCT a.academicYear FROM AcademicCalendar a ORDER BY a.academicYear DESC")
    List<String> findDistinctAcademicYears();

    @Query("SELECT DISTINCT a.semester.id FROM AcademicCalendar a WHERE a.academicYear = :academicYear ORDER BY a.semester.id")
    List<Long> findDistinctSemesterIdsByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventDate = :currentDate")
    List<AcademicCalendar> findEventsForDate(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT a FROM AcademicCalendar a WHERE a.eventDate >= :currentDate AND a.eventDate <= :futureDate ORDER BY a.eventDate")
    List<AcademicCalendar> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate,
                                             @Param("futureDate") LocalDateTime futureDate);
}