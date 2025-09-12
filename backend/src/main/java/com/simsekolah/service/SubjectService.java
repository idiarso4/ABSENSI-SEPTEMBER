package com.simsekolah.service;

import com.simsekolah.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    Subject createSubject(Subject subject);

    Subject updateSubject(Long id, Subject subjectDetails);

    Optional<Subject> getSubjectById(Long id);

    Optional<Subject> getSubjectByCode(String code);

    Page<Subject> getAllSubjects(Pageable pageable);

    Page<Subject> searchSubjects(String query, Pageable pageable);

    void deleteSubject(Long id);

    List<Subject> getSubjectsByType(Subject.SubjectType type);

    long countActiveSubjects();
}