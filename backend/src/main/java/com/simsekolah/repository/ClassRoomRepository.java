package com.simsekolah.repository;

import com.simsekolah.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ClassRoom entity operations
 */
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    /**
     * Find class room by name (maps to field className)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.className = :name")
    Optional<ClassRoom> findByName(@Param("name") String name);

       /**
        * Find class room by name, case-insensitive (maps to field className)
        */
       @Query("SELECT c FROM ClassRoom c WHERE LOWER(c.className) = LOWER(:name)")
       Optional<ClassRoom> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find class rooms by grade (maps to field gradeLevel)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.gradeLevel = :grade")
    List<ClassRoom> findByGrade(@Param("grade") Integer grade);

    /**
     * Find class rooms by grade with pagination (maps to field gradeLevel)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.gradeLevel = :grade")
    Page<ClassRoom> findByGrade(@Param("grade") Integer grade, Pageable pageable);

    /**
     * Find class rooms by major ID
     */
    List<ClassRoom> findByMajorId(Long majorId);

    /**
     * Find class rooms by major ID with pagination
     */
    Page<ClassRoom> findByMajorId(Long majorId, Pageable pageable);

    /**
     * Find active class rooms
     */
    List<ClassRoom> findByIsActiveTrue();

    /**
     * Find active class rooms with pagination
     */
    Page<ClassRoom> findByIsActiveTrue(Pageable pageable);

    /**
     * Find class rooms by homeroom teacher
     */
    List<ClassRoom> findByHomeroomTeacherId(Long teacherId);

    /**
     * Find class rooms by academic year
     */
    List<ClassRoom> findByAcademicYear(String academicYear);

    /**
     * Find class rooms by academic year with pagination
     */
    Page<ClassRoom> findByAcademicYear(String academicYear, Pageable pageable);

    /**
     * Find class rooms by grade and major (maps to gradeLevel and major.id)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.gradeLevel = :grade AND c.major.id = :majorId")
    List<ClassRoom> findByGradeAndMajorId(@Param("grade") Integer grade, @Param("majorId") Long majorId);

    /**
     * Find class rooms by grade and major with pagination (maps to gradeLevel and major.id)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.gradeLevel = :grade AND c.major.id = :majorId")
    Page<ClassRoom> findByGradeAndMajorId(@Param("grade") Integer grade, @Param("majorId") Long majorId, Pageable pageable);

    /**
     * Find class rooms by name containing (case insensitive) (maps to field className)
     */
    @Query("SELECT c FROM ClassRoom c WHERE LOWER(c.className) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<ClassRoom> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    /**
     * Check if class name exists (maps to field className)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassRoom c WHERE LOWER(c.className) = LOWER(:name)")
    boolean existsByName(@Param("name") String name);

    // Removed invalid derived query using non-existent property "classCode" to avoid startup failure

    /**
     * Count class rooms by grade (maps to field gradeLevel)
     */
    @Query("SELECT COUNT(c) FROM ClassRoom c WHERE c.gradeLevel = :grade")
    long countByGrade(@Param("grade") Integer grade);

    /**
     * Count class rooms by major
     */
    long countByMajorId(Long majorId);

    /**
     * Count active class rooms
     */
    long countByIsActiveTrue();

    /**
     * Find class rooms with available space
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.capacity IS NULL OR c.currentStudents < c.capacity")
    List<ClassRoom> findClassRoomsWithAvailableSpace();

    /**
     * Find class rooms with available space with pagination
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.capacity IS NULL OR c.currentStudents < c.capacity")
    Page<ClassRoom> findClassRoomsWithAvailableSpace(Pageable pageable);

    /**
     * Find full class rooms
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.capacity IS NOT NULL AND c.currentStudents >= c.capacity")
    List<ClassRoom> findFullClassRooms();

    /**
     * Find class rooms by department (through major)
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.major.department.id = :departmentId")
    List<ClassRoom> findByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Find class rooms by department with pagination
     */
    @Query("SELECT c FROM ClassRoom c WHERE c.major.department.id = :departmentId")
    Page<ClassRoom> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

    /**
     * Find class rooms without homeroom teacher
     */
    List<ClassRoom> findByHomeroomTeacherIsNull();

    /**
     * Find class rooms with homeroom teacher
     */
    List<ClassRoom> findByHomeroomTeacherIsNotNull();

    /**
     * Get class room statistics
     */
    @Query("SELECT c.gradeLevel, COUNT(c), AVG(c.currentStudents), SUM(c.currentStudents) " +
           "FROM ClassRoom c WHERE c.isActive = true GROUP BY c.gradeLevel ORDER BY c.gradeLevel")
    List<Object[]> getClassRoomStatisticsByGrade();

    /**
     * Search class rooms by multiple criteria
     */
    @Query("SELECT c FROM ClassRoom c WHERE " +
           "(:name IS NULL OR LOWER(c.className) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:grade IS NULL OR c.gradeLevel = :grade) AND " +
           "(:majorId IS NULL OR c.major.id = :majorId) AND " +
           "(:academicYear IS NULL OR c.academicYear = :academicYear) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive) AND " +
           "(:hasAvailableSpace IS NULL OR " +
           "  (:hasAvailableSpace = true AND (c.capacity IS NULL OR c.currentStudents < c.capacity)) OR " +
           "  (:hasAvailableSpace = false AND c.capacity IS NOT NULL AND c.currentStudents >= c.capacity))")
    Page<ClassRoom> searchClassRooms(@Param("name") String name,
                                    @Param("grade") Integer grade,
                                    @Param("majorId") Long majorId,
                                    @Param("academicYear") String academicYear,
                                    @Param("isActive") Boolean isActive,
                                    @Param("hasAvailableSpace") Boolean hasAvailableSpace,
                                    Pageable pageable);
}
