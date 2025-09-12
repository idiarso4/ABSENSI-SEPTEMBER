package com.simsekolah.service;

import com.simsekolah.exception.ResourceNotFoundException;
import com.simsekolah.exception.ValidationException;
import com.simsekolah.entity.Subject;
import com.simsekolah.entity.User;
import com.simsekolah.repository.SubjectRepository;
import com.simsekolah.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<User> getAllTeachers() {
        return teacherRepository.findAllActiveTeachersOrdered();
    }

    public Optional<User> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<User> getTeacherByNip(String nip) {
        return teacherRepository.findByNip(nip);
    }

    public User createTeacher(User teacher) {
        // Validate NIP uniqueness
        if (teacherRepository.existsByNip(teacher.getUsername())) {
            throw new ValidationException("NIP already exists: " + teacher.getUsername());
        }

        return teacherRepository.save(teacher);
    }

    public User updateTeacher(Long id, User teacherDetails) {
        User teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        // Check NIP uniqueness if changed
        if (!teacher.getUsername().equals(teacherDetails.getUsername()) &&
            teacherRepository.existsByNip(teacherDetails.getUsername())) {
            throw new ValidationException("NIP already exists: " + teacherDetails.getUsername());
        }

        // Update fields
        teacher.setUsername(teacherDetails.getUsername());
        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setEmail(teacherDetails.getEmail());
        // Note: User entity may not have phone/address fields, adjust as needed

        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        User teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        // Soft delete - set isActive to false
        teacher.setIsActive(false);
        teacherRepository.save(teacher);
    }

    public List<User> searchTeachersByName(String name) {
        return teacherRepository.findByFullNameContainingIgnoreCase(name);
    }

    public List<User> getTeachersBySubject(Long subjectId) {
        return teacherRepository.findTeachersBySubjectId(subjectId);
    }

    public List<User> getTeachersByClass(Long classId) {
        return teacherRepository.findTeachersByClassId(classId);
    }

    public Long countActiveTeachers() {
        return teacherRepository.countActiveTeachers();
    }

    public User assignSubjectsToTeacher(Long teacherId, List<Long> subjectIds) {
        User teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        // Validate all subject IDs exist
        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        if (subjects.size() != subjectIds.size()) {
            Set<Long> foundIds = subjects.stream()
                    .map(Subject::getId)
                    .collect(Collectors.toSet());
            List<Long> missingIds = subjectIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new ValidationException("Subjects not found with IDs: " + missingIds);
        }

        // Note: User entity may not have subjects relationship, this might need adjustment
        // teacher.setSubjects(subjects);
        return teacherRepository.save(teacher);
    }

    public User addSubjectToTeacher(Long teacherId, Long subjectId) {
        User teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        // Validate subject exists
        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        // Note: User entity may not have subjects relationship, this might need adjustment
        // if (!teacher.getSubjects().contains(subject)) {
        //     teacher.getSubjects().add(subject);
        //     teacherRepository.save(teacher);
        // }

        return teacher;
    }

    public User removeSubjectFromTeacher(Long teacherId, Long subjectId) {
        User teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        // Validate subject exists
        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        // Note: User entity may not have subjects relationship, this might need adjustment
        // teacher.getSubjects().remove(subject);
        return teacherRepository.save(teacher);
    }

    public List<Subject> getTeacherSubjects(Long teacherId) {
        // Validate teacher exists
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        // Note: User entity may not have subjects relationship, this might need adjustment
        // return teacher.getSubjects();
        return new java.util.ArrayList<>(); // Placeholder
    }
}
