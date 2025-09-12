package com.simsekolah.repository;

import com.simsekolah.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);

    Page<Subject> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code, Pageable pageable);

    @Query("SELECT s FROM Subject s WHERE s.subjectType = :type AND s.isActive = true")
    List<Subject> findBySubjectTypeAndIsActiveTrue(@Param("type") Subject.SubjectType type);

    long countByIsActiveTrue();

    @Query("SELECT s FROM Subject s WHERE s.isActive = true ORDER BY s.name")
    List<Subject> findAllActiveSubjectsOrdered();
}
