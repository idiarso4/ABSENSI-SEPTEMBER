package com.simsekolah.repository;

import com.simsekolah.entity.Semester;
import com.simsekolah.enums.SemesterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByAcademicYear(String academicYear);

    @Query("SELECT s FROM Semester s WHERE s.academicYear = :academicYear ORDER BY s.semesterName")
    List<Semester> findByAcademicYearOrderBySemesterNumber(@Param("academicYear") String academicYear);

    @Query("SELECT s FROM Semester s WHERE s.semesterName LIKE CONCAT('%', :semesterNumber, '%')")
    List<Semester> findBySemesterNumber(@Param("semesterNumber") Integer semesterNumber);

    List<Semester> findByStatus(SemesterStatus status);

    @Query("SELECT s FROM Semester s WHERE s.academicYear = :academicYear AND s.semesterName LIKE CONCAT('%', :semesterNumber, '%')")
    Optional<Semester> findByAcademicYearAndSemesterNumber(@Param("academicYear") String academicYear, @Param("semesterNumber") Integer semesterNumber);

    List<Semester> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Semester> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM Semester s WHERE s.startDate <= :currentDate AND s.endDate >= :currentDate AND s.status = 'ACTIVE'")
    List<Semester> findCurrentSemesters(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.startDate > :currentDate AND s.status = 'PLANNED' ORDER BY s.startDate")
    List<Semester> findUpcomingSemesters(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.endDate < :currentDate AND s.status = 'ACTIVE' ORDER BY s.endDate DESC")
    List<Semester> findCompletedSemesters(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.registrationStartDate <= :currentDate AND s.registrationEndDate >= :currentDate")
    List<Semester> findSemestersWithOpenRegistration(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.examStartDate <= :currentDate AND s.examEndDate >= :currentDate")
    List<Semester> findSemestersInExamPeriod(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT DISTINCT s.academicYear FROM Semester s ORDER BY s.academicYear DESC")
    List<String> findDistinctAcademicYears();

    @Query("SELECT s FROM Semester s WHERE s.academicYear = :academicYear AND s.semesterName LIKE CONCAT('%', :semesterNumber, '%')")
    Optional<Semester> findByAcademicYearAndSemester(@Param("academicYear") String academicYear,
                                                     @Param("semesterNumber") Integer semesterNumber);

    // Pagination support
    Page<Semester> findByAcademicYear(String academicYear, Pageable pageable);

    Page<Semester> findByStatus(SemesterStatus status, Pageable pageable);

    Page<Semester> findByAcademicYearAndStatus(String academicYear, SemesterStatus status, Pageable pageable);

    @Query("SELECT s FROM Semester s WHERE " +
           "(:academicYear IS NULL OR s.academicYear = :academicYear) AND " +
           "(:semesterNumber IS NULL OR s.semesterName LIKE CONCAT('%', :semesterNumber, '%')) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:startDate IS NULL OR s.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR s.endDate <= :endDate)")
    Page<Semester> advancedSearch(@Param("academicYear") String academicYear,
                                 @Param("semesterNumber") Integer semesterNumber,
                                 @Param("status") SemesterStatus status,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 Pageable pageable);

    @Query("SELECT COUNT(s) FROM Semester s WHERE s.academicYear = :academicYear")
    long countByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT COUNT(s) FROM Semester s WHERE s.academicYear = :academicYear AND s.status = :status")
    long countByAcademicYearAndStatus(@Param("academicYear") String academicYear,
                                     @Param("status") SemesterStatus status);

    @Query("SELECT s.status as status, COUNT(s) as count FROM Semester s GROUP BY s.status")
    List<Object[]> getSemesterStatisticsByStatus();

    @Query("SELECT s.academicYear as academicYear, COUNT(s) as count FROM Semester s GROUP BY s.academicYear ORDER BY s.academicYear DESC")
    List<Object[]> getSemesterStatisticsByAcademicYear();

    @Query("SELECT s.semesterName as semesterName, COUNT(s) as count FROM Semester s GROUP BY s.semesterName ORDER BY s.semesterName")
    List<Object[]> getSemesterStatisticsBySemesterNumber();

    @Query("SELECT s FROM Semester s WHERE s.status = 'ACTIVE' ORDER BY s.startDate DESC")
    List<Semester> findActiveSemestersOrderByStartDateDesc();

    @Query("SELECT s FROM Semester s WHERE s.academicYear = :academicYear ORDER BY s.semesterName")
    List<Semester> findByAcademicYearOrderedBySemesterNumber(@Param("academicYear") String academicYear);

    @Query("SELECT s FROM Semester s WHERE s.startDate <= :date AND s.endDate >= :date")
    List<Semester> findSemestersContainingDate(@Param("date") LocalDate date);

    @Query("SELECT s FROM Semester s WHERE s.registrationStartDate <= :date AND s.registrationEndDate >= :date")
    List<Semester> findSemestersWithRegistrationOnDate(@Param("date") LocalDate date);

    @Query("SELECT s FROM Semester s WHERE s.examStartDate <= :date AND s.examEndDate >= :date")
    List<Semester> findSemestersWithExamOnDate(@Param("date") LocalDate date);

    @Query("SELECT MIN(s.startDate) FROM Semester s WHERE s.academicYear = :academicYear")
    LocalDate findEarliestStartDateByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT MAX(s.endDate) FROM Semester s WHERE s.academicYear = :academicYear")
    LocalDate findLatestEndDateByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT s FROM Semester s WHERE s.status = 'PLANNED' AND s.startDate > :currentDate ORDER BY s.startDate")
    List<Semester> findPlannedSemestersAfterDate(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.status = 'COMPLETED' AND s.endDate < :currentDate ORDER BY s.endDate DESC")
    List<Semester> findCompletedSemestersBeforeDate(@Param("currentDate") LocalDate currentDate);
}