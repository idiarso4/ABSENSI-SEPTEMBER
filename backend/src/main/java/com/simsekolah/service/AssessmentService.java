package com.simsekolah.service;

import com.simsekolah.dto.request.AssessmentSearchRequest;
import com.simsekolah.dto.request.CreateAssessmentRequest;
import com.simsekolah.dto.request.GradeAssessmentRequest;
import com.simsekolah.dto.request.UpdateAssessmentRequest;
import com.simsekolah.dto.response.AssessmentResponse;
import com.simsekolah.dto.response.StudentAssessmentResponse;
import com.simsekolah.entity.Assessment;
import com.simsekolah.enums.AssessmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssessmentService {

    // Legacy methods for backward compatibility
    List<Assessment> getAllAssessments();
    Optional<Assessment> getAssessmentById(Long id);
    Assessment createAssessment(Assessment assessment);
    Assessment updateAssessment(Long id, Assessment assessmentDetails);
    void deleteAssessment(Long id);
    List<Assessment> getAssessmentsByStudentId(Long studentId);
    List<Assessment> getAssessmentsBySubjectId(Long subjectId);
    List<Assessment> getAssessmentsByStudentAndSubject(Long studentId, Long subjectId);

    // New methods
    Page<AssessmentResponse> getAllAssessmentsPageable(Pageable pageable);
    AssessmentResponse getAssessmentResponseById(Long assessmentId);
    AssessmentResponse createAssessment(CreateAssessmentRequest request);
    AssessmentResponse updateAssessment(Long assessmentId, UpdateAssessmentRequest request);
    List<StudentAssessmentResponse> gradeAssessment(Long assessmentId, GradeAssessmentRequest request);
    Page<StudentAssessmentResponse> getAssessmentGrades(Long assessmentId, Pageable pageable);
    Page<StudentAssessmentResponse> getStudentAssessments(Long studentId, Pageable pageable);
    Map<String, Object> getAssessmentStatistics();
    BigDecimal calculateStudentGPA(Long studentId, String academicYear, Integer semester);
    List<StudentAssessmentResponse> bulkGradeAssessment(Long assessmentId, List<GradeAssessmentRequest> requests);

    // Additional methods for test compatibility
    Page<AssessmentResponse> getAllAssessments(Pageable pageable);
    Page<AssessmentResponse> searchAssessments(AssessmentSearchRequest request, Pageable pageable);
    Page<AssessmentResponse> getAssessmentsByType(AssessmentType type, Pageable pageable);
    Page<AssessmentResponse> getAssessmentsBySubject(long subjectId, Pageable pageable);
    Page<AssessmentResponse> getAssessmentsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<AssessmentResponse> getUpcomingAssessments(int days, Pageable pageable);
}