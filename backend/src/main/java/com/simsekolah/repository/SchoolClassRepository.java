package com.simsekolah.repository;

import com.simsekolah.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolClassRepository extends JpaRepository<ClassRoom, Long> {

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.gradeLevel = :gradeLevel")
    List<ClassRoom> findByGradeLevel(@Param("gradeLevel") Integer gradeLevel);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.academicYear = :academicYear")
    List<ClassRoom> findByAcademicYear(@Param("academicYear") String academicYear);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.gradeLevel = :gradeLevel AND cr.academicYear = :academicYear")
    List<ClassRoom> findByGradeLevelAndAcademicYear(@Param("gradeLevel") Integer gradeLevel, @Param("academicYear") String academicYear);

    @Query("SELECT cr FROM ClassRoom cr WHERE LOWER(cr.className) LIKE LOWER(CONCAT('%', :className, '%'))")
    List<ClassRoom> findByClassNameContainingIgnoreCase(@Param("className") String className);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.homeroomTeacher.id = :teacherId AND cr.isActive = true")
    List<ClassRoom> findByWaliKelasId(@Param("teacherId") Long teacherId);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.isActive = true ORDER BY cr.gradeLevel, cr.className")
    List<ClassRoom> findAllActiveClasses();

    @Query("SELECT COUNT(cr) FROM ClassRoom cr WHERE cr.gradeLevel = :gradeLevel AND cr.academicYear = :academicYear AND cr.isActive = true")
    Long countClassesByGradeAndYear(@Param("gradeLevel") Integer gradeLevel, @Param("academicYear") String academicYear);
}
