package com.simsekolah.repository;

import com.simsekolah.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<User, Long> {

    Optional<User> findByNip(String nip);

    Boolean existsByNip(String nip);

    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER' AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER' AND u.isActive = true")
    List<User> findAllActiveTeachers();

    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER' AND u.isActive = true ORDER BY u.firstName, u.lastName")
    List<User> findAllActiveTeachersOrdered();

    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'TEACHER' AND u.isActive = true")
    Long countActiveTeachers();

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.userType = 'TEACHER' AND u.isActive = true")
    Optional<User> findByEmail(@Param("email") String email);

    // Placeholder methods - need proper relationship setup
    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER' AND u.isActive = true")
    List<User> findTeachersBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER' AND u.isActive = true")
    List<User> findTeachersByClassId(@Param("classId") Long classId);
}
