package com.simsekolah.service;

import com.simsekolah.entity.Grade;
import com.simsekolah.exception.ResourceNotFoundException;
import com.simsekolah.exception.ValidationException;
import com.simsekolah.repository.GradeRepository;
import com.simsekolah.repository.StudentRepository;
import com.simsekolah.repository.SubjectRepository;
import com.simsekolah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesBySubject(Long subjectId) {
        return gradeRepository.findBySubjectId(subjectId);
    }

    public List<Grade> getGradesByTeacher(Long teacherId) {
        return gradeRepository.findByTeacherId(teacherId);
    }

    public List<Grade> getGradesByStudentAndSubject(Long studentId, Long subjectId) {
        return gradeRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    public Grade createGrade(Grade grade) {
        // Validate required entities exist
        validateGradeEntities(grade);

        // Validate score range
        validateScore(grade.getScore());

        // Check if grade already exists for this student, subject, and type
        Optional<Grade> existingGrade = gradeRepository.findByStudentSubjectAndType(
                grade.getStudent().getId(),
                grade.getSubject().getId(),
                grade.getGradeType()
        );

        if (existingGrade.isPresent()) {
            throw new ValidationException("Grade already exists for this student, subject, and grade type");
        }

        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        // Validate required entities exist
        validateGradeEntities(gradeDetails);

        // Validate score range
        validateScore(gradeDetails.getScore());

        // Check if updated grade conflicts with existing grade
        if (!grade.getStudent().getId().equals(gradeDetails.getStudent().getId()) ||
            !grade.getSubject().getId().equals(gradeDetails.getSubject().getId()) ||
            !grade.getGradeType().equals(gradeDetails.getGradeType())) {

            Optional<Grade> existingGrade = gradeRepository.findByStudentSubjectAndType(
                    gradeDetails.getStudent().getId(),
                    gradeDetails.getSubject().getId(),
                    gradeDetails.getGradeType()
            );

            if (existingGrade.isPresent() && !existingGrade.get().getId().equals(id)) {
                throw new ValidationException("Grade already exists for this student, subject, and grade type");
            }
        }

        // Update fields
        grade.setStudent(gradeDetails.getStudent());
        grade.setSubject(gradeDetails.getSubject());
        grade.setTeacher(gradeDetails.getTeacher());
        grade.setGradeType(gradeDetails.getGradeType());
        grade.setScore(gradeDetails.getScore());
        grade.setSemester(gradeDetails.getSemester());
        grade.setAcademicYear(gradeDetails.getAcademicYear());

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        gradeRepository.delete(grade);
    }

    private void validateGradeEntities(Grade grade) {
        // Validate student
        if (grade.getStudent() != null) {
            Optional<com.simsekolah.entity.Student> student = studentRepository.findById(grade.getStudent().getId());
            if (student.isEmpty()) {
                throw new ValidationException("Student not found with ID: " + grade.getStudent().getId());
            }
        }

        // Validate subject
        if (grade.getSubject() != null) {
            Optional<com.simsekolah.entity.Subject> subject = subjectRepository.findById(grade.getSubject().getId());
            if (subject.isEmpty()) {
                throw new ValidationException("Subject not found with ID: " + grade.getSubject().getId());
            }
        }

        // Validate teacher
        if (grade.getTeacher() != null) {
            Optional<com.simsekolah.entity.User> teacher = userRepository.findById(grade.getTeacher().getId());
            if (teacher.isEmpty()) {
                throw new ValidationException("Teacher not found with ID: " + grade.getTeacher().getId());
            }
        }
    }

    private void validateScore(BigDecimal score) {
        if (score == null) {
            throw new ValidationException("Score cannot be null");
        }
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ValidationException("Score must be between 0 and 100");
        }
    }

    // Statistics and Reporting Methods
    public Double getAverageGradeByStudentAndSubject(Long studentId, Long subjectId) {
        return gradeRepository.findAverageGradeByStudentAndSubject(studentId, subjectId);
    }

    public Double getAverageGradeByStudentAndSemester(Long studentId, String semester, Integer academicYear) {
        return gradeRepository.findAverageGradeByStudentAndSemester(studentId, semester, academicYear);
    }

    public Double getAverageGradeBySubjectAndType(Long subjectId, Grade.GradeType gradeType) {
        return gradeRepository.findAverageGradeBySubjectAndType(subjectId, gradeType);
    }

    public Double getMaxGradeBySubjectAndType(Long subjectId, Grade.GradeType gradeType) {
        return gradeRepository.findMaxGradeBySubjectAndType(subjectId, gradeType);
    }

    public Double getMinGradeBySubjectAndType(Long subjectId, Grade.GradeType gradeType) {
        return gradeRepository.findMinGradeBySubjectAndType(subjectId, gradeType);
    }

    public Double getPassingRateByStudent(Long studentId) {
        Long passingGrades = gradeRepository.countPassingGradesByStudent(studentId);
        Long totalGrades = gradeRepository.countTotalGradesByStudent(studentId);

        if (totalGrades == 0) {
            return 0.0;
        }

        return (passingGrades.doubleValue() / totalGrades.doubleValue()) * 100;
    }

    public List<Grade> getGradesByClass(Long classId) {
        return gradeRepository.findGradesByClass(classId);
    }

    public List<Grade> getGradesByStudentAndSemester(Long studentId, String semester, Integer academicYear) {
        return gradeRepository.findByStudentAndSemester(studentId, semester, academicYear);
    }

    public List<Grade> getGradesByClassSubjectAndType(Long classId, Long subjectId, Grade.GradeType gradeType) {
        return gradeRepository.findByClassSubjectAndType(classId, subjectId, gradeType);
    }

    public List<Integer> getAllAcademicYears() {
        return gradeRepository.findAllAcademicYears();
    }

    public List<String> getSemestersByAcademicYear(Integer academicYear) {
        return gradeRepository.findSemestersByAcademicYear(academicYear);
    }
}
