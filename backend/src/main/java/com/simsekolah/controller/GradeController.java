package com.simsekolah.controller;

import com.simsekolah.entity.Grade;
import com.simsekolah.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Optional<Grade> grade = gradeService.getGradeById(id);
        if (grade.isPresent()) {
            return ResponseEntity.ok(grade.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Grade> createGrade(@Valid @RequestBody Grade grade) {
        try {
            Grade createdGrade = gradeService.createGrade(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @Valid @RequestBody Grade gradeDetails) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, gradeDetails);
            return ResponseEntity.ok(updatedGrade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesBySubject(@PathVariable Long subjectId) {
        List<Grade> grades = gradeService.getGradesBySubject(subjectId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesByTeacher(@PathVariable Long teacherId) {
        List<Grade> grades = gradeService.getGradesByTeacher(teacherId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getGradesByStudentAndSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        List<Grade> grades = gradeService.getGradesByStudentAndSubject(studentId, subjectId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/semester/{semester}/year/{academicYear}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getGradesByStudentAndSemester(@PathVariable Long studentId,
                                                                     @PathVariable String semester,
                                                                     @PathVariable Integer academicYear) {
        List<Grade> grades = gradeService.getGradesByStudentAndSemester(studentId, semester, academicYear);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesByClass(@PathVariable Long classId) {
        List<Grade> grades = gradeService.getGradesByClass(classId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/class/{classId}/subject/{subjectId}/type/{gradeType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Grade>> getGradesByClassSubjectAndType(@PathVariable Long classId,
                                                                      @PathVariable Long subjectId,
                                                                      @PathVariable Grade.GradeType gradeType) {
        List<Grade> grades = gradeService.getGradesByClassSubjectAndType(classId, subjectId, gradeType);
        return ResponseEntity.ok(grades);
    }

    // Statistics and Reporting Endpoints
    @GetMapping("/student/{studentId}/subject/{subjectId}/average")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<Double> getAverageGradeByStudentAndSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        Double average = gradeService.getAverageGradeByStudentAndSubject(studentId, subjectId);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }

    @GetMapping("/student/{studentId}/semester/{semester}/year/{academicYear}/average")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<Double> getAverageGradeByStudentAndSemester(@PathVariable Long studentId,
                                                                     @PathVariable String semester,
                                                                     @PathVariable Integer academicYear) {
        Double average = gradeService.getAverageGradeByStudentAndSemester(studentId, semester, academicYear);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }

    @GetMapping("/subject/{subjectId}/type/{gradeType}/average")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Double> getAverageGradeBySubjectAndType(@PathVariable Long subjectId, @PathVariable Grade.GradeType gradeType) {
        Double average = gradeService.getAverageGradeBySubjectAndType(subjectId, gradeType);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }

    @GetMapping("/subject/{subjectId}/type/{gradeType}/max")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Double> getMaxGradeBySubjectAndType(@PathVariable Long subjectId, @PathVariable Grade.GradeType gradeType) {
        Double max = gradeService.getMaxGradeBySubjectAndType(subjectId, gradeType);
        return ResponseEntity.ok(max != null ? max : 0.0);
    }

    @GetMapping("/subject/{subjectId}/type/{gradeType}/min")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Double> getMinGradeBySubjectAndType(@PathVariable Long subjectId, @PathVariable Grade.GradeType gradeType) {
        Double min = gradeService.getMinGradeBySubjectAndType(subjectId, gradeType);
        return ResponseEntity.ok(min != null ? min : 0.0);
    }

    @GetMapping("/student/{studentId}/passing-rate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('PARENT')")
    public ResponseEntity<Double> getPassingRateByStudent(@PathVariable Long studentId) {
        Double passingRate = gradeService.getPassingRateByStudent(studentId);
        return ResponseEntity.ok(passingRate);
    }

    @GetMapping("/academic-years")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Integer>> getAllAcademicYears() {
        List<Integer> years = gradeService.getAllAcademicYears();
        return ResponseEntity.ok(years);
    }

    @GetMapping("/year/{academicYear}/semesters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<String>> getSemestersByAcademicYear(@PathVariable Integer academicYear) {
        List<String> semesters = gradeService.getSemestersByAcademicYear(academicYear);
        return ResponseEntity.ok(semesters);
    }
}
