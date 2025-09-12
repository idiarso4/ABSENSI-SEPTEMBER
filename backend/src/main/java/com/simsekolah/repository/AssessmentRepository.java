package com.simsekolah.repository;

import com.simsekolah.entity.Assessment;
import com.simsekolah.enums.AssessmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByStudentId(Long studentId);
    List<Assessment> findBySubjectId(Long subjectId);
    List<Assessment> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    // Additional methods for AssessmentServiceImpl
    List<Assessment> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    Page<Assessment> findByType(AssessmentType type, Pageable pageable);
    Page<Assessment> findByTeacherId(Long teacherId, Pageable pageable);
    Page<Assessment> findByClassroomId(Long classroomId, Pageable pageable);
    Page<Assessment> findBySubjectId(Long subjectId, Pageable pageable);
    Page<Assessment> findByAcademicYearAndSemester(String academicYear, Integer semester, Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.dueDate > :currentDate ORDER BY a.dueDate ASC")
    Page<Assessment> findUpcomingAssessments(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.dueDate < :currentDate AND a.isActive = true")
    Page<Assessment> findOverdueAssessments(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.teacher.id = :teacherId AND a.isActive = true AND " +
           "NOT EXISTS (SELECT sa FROM StudentAssessment sa WHERE sa.assessment = a)")
    Page<Assessment> findUngradedAssessmentsByTeacher(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.isPublished = true")
    Page<Assessment> findPublishedAssessments(Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.academicYear = :academicYear AND a.semester = :semester AND a.isActive = true")
    List<Assessment> findByAcademicYearAndSemesterAndIsActiveTrue(@Param("academicYear") String academicYear,
                                                                 @Param("semester") Integer semester);
}