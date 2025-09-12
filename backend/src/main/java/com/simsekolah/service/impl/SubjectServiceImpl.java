package com.simsekolah.service.impl;

import com.simsekolah.exception.ResourceNotFoundException;
import com.simsekolah.exception.ValidationException;
import com.simsekolah.entity.Subject;
import com.simsekolah.repository.SubjectRepository;
import com.simsekolah.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Subject createSubject(Subject subject) {
        // Validate subject code uniqueness
        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new ValidationException("Subject code already exists: " + subject.getCode());
        }

        subject.setIsActive(true);
        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));

        // Check subject code uniqueness if changed
        if (!subject.getCode().equals(subjectDetails.getCode()) &&
            subjectRepository.existsByCode(subjectDetails.getCode())) {
            throw new ValidationException("Subject code already exists: " + subjectDetails.getCode());
        }

        // Update fields
        subject.setCode(subjectDetails.getCode());
        subject.setName(subjectDetails.getName());
        subject.setDescription(subjectDetails.getDescription());
        subject.setCreditHours(subjectDetails.getCreditHours());

        return subjectRepository.save(subject);
    }

    @Override
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Optional<Subject> getSubjectByCode(String code) {
        return subjectRepository.findByCode(code);
    }

    @Override
    public Page<Subject> getAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Override
    public Page<Subject> searchSubjects(String query, Pageable pageable) {
        return subjectRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query, pageable);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));

        // Soft delete - set isActive to false
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjectsByType(Subject.SubjectType type) {
        return subjectRepository.findBySubjectTypeAndIsActiveTrue(type);
    }

    @Override
    public long countActiveSubjects() {
        return subjectRepository.countByIsActiveTrue();
    }
}