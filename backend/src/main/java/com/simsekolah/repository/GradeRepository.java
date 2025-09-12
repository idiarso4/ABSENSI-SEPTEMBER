package com.simsekolah.repository;

import com.simsekolah.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findBySubjectId(Long subjectId);

    List<Grade> findByTeacherId(Long teacherId);

    List<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<Grade> findByGradeType(Grade.GradeType gradeType);

    List<Grade> findBySemesterAndAcademicYear(String semester, Integer academicYear);

    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.subject.id = :subjectId AND g.gradeType = :gradeType")
    Optional<Grade> findByStudentSubjectAndType(@Param("studentId") Long studentId,
                                               @Param("subjectId") Long subjectId,
                                               @Param("gradeType") Grade.GradeType gradeType);

    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester AND g.academicYear = :academicYear")
    List<Grade> findByStudentAndSemester(@Param("studentId") Long studentId,
                                        @Param("semester") String semester,
                                        @Param("academicYear") Integer academicYear);

    @Query("SELECT g FROM Grade g WHERE g.student.classRoom.id = :classId AND g.subject.id = :subjectId AND g.gradeType = :gradeType")
    List<Grade> findByClassSubjectAndType(@Param("classId") Long classId,
                                          @Param("subjectId") Long subjectId,
                                          @Param("gradeType") Grade.GradeType gradeType);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.id = :studentId AND g.subject.id = :subjectId")
    Double findAverageGradeByStudentAndSubject(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester AND g.academicYear = :academicYear")
    Double findAverageGradeByStudentAndSemester(@Param("studentId") Long studentId,
                                               @Param("semester") String semester,
                                               @Param("academicYear") Integer academicYear);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.subject.id = :subjectId AND g.gradeType = :gradeType")
    Double findAverageGradeBySubjectAndType(@Param("subjectId") Long subjectId, @Param("gradeType") Grade.GradeType gradeType);

    @Query("SELECT MAX(g.score) FROM Grade g WHERE g.subject.id = :subjectId AND g.gradeType = :gradeType")
    Double findMaxGradeBySubjectAndType(@Param("subjectId") Long subjectId, @Param("gradeType") Grade.GradeType gradeType);

    @Query("SELECT MIN(g.score) FROM Grade g WHERE g.subject.id = :subjectId AND g.gradeType = :gradeType")
    Double findMinGradeBySubjectAndType(@Param("subjectId") Long subjectId, @Param("gradeType") Grade.GradeType gradeType);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.id = :studentId AND g.score >= 75")
    Long countPassingGradesByStudent(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.id = :studentId")
    Long countTotalGradesByStudent(@Param("studentId") Long studentId);

    @Query("SELECT g FROM Grade g WHERE g.student.classRoom.id = :classId ORDER BY g.student.namaLengkap, g.subject.name")
    List<Grade> findGradesByClass(@Param("classId") Long classId);

    @Query("SELECT DISTINCT g.academicYear FROM Grade g ORDER BY g.academicYear DESC")
    List<Integer> findAllAcademicYears();

    @Query("SELECT DISTINCT g.semester FROM Grade g WHERE g.academicYear = :academicYear")
    List<String> findSemestersByAcademicYear(@Param("academicYear") Integer academicYear);
}
