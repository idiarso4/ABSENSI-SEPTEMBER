package com.simsekolah.controller;

import com.simsekolah.entity.Grade;
import com.simsekolah.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GradeControllerTest {

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllGrades_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getAllGrades()).thenReturn(grades);

        mockMvc.perform(get("/api/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getAllGrades();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradeById_Success() throws Exception {
        Grade grade = new Grade();
        grade.setId(1L);
        when(gradeService.getGradeById(1L)).thenReturn(Optional.of(grade));

        mockMvc.perform(get("/api/grades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(gradeService).getGradeById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGrade_Success() throws Exception {
        Grade request = new Grade();
        request.setId(1L);
        when(gradeService.createGrade(any(Grade.class))).thenReturn(request);

        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(gradeService).createGrade(any(Grade.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGrade_Success() throws Exception {
        Grade request = new Grade();
        request.setId(1L);
        Grade response = new Grade();
        response.setId(1L);
        when(gradeService.updateGrade(eq(1L), any(Grade.class))).thenReturn(response);

        mockMvc.perform(put("/api/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(gradeService).updateGrade(eq(1L), any(Grade.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGrade_Success() throws Exception {
        doNothing().when(gradeService).deleteGrade(1L);

        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isNoContent());

        verify(gradeService).deleteGrade(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByStudent_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByStudent(1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByStudent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesBySubject_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesBySubject(1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesBySubject(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByTeacher_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByTeacher(1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByTeacher(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByStudentAndSubject_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByStudentAndSubject(1L, 1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/student/1/subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByStudentAndSubject(1L, 1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByStudentAndSemester_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByStudentAndSemester(1L, "1", 2023)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/student/1/semester/1/year/2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByStudentAndSemester(1L, "1", 2023);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByClass_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByClass(1L)).thenReturn(grades);

        mockMvc.perform(get("/api/grades/class/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByClass(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGradesByClassSubjectAndType_Success() throws Exception {
        List<Grade> grades = Collections.emptyList();
        when(gradeService.getGradesByClassSubjectAndType(eq(1L), eq(1L), any(Grade.GradeType.class))).thenReturn(grades);

        mockMvc.perform(get("/api/grades/class/1/subject/1/type/QUIZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(gradeService).getGradesByClassSubjectAndType(eq(1L), eq(1L), any(Grade.GradeType.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAverageGradeByStudentAndSubject_Success() throws Exception {
        when(gradeService.getAverageGradeByStudentAndSubject(1L, 1L)).thenReturn(85.5);

        mockMvc.perform(get("/api/grades/student/1/subject/1/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(85.5));

        verify(gradeService).getAverageGradeByStudentAndSubject(1L, 1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAverageGradeByStudentAndSemester_Success() throws Exception {
        when(gradeService.getAverageGradeByStudentAndSemester(1L, "1", 2023)).thenReturn(82.0);

        mockMvc.perform(get("/api/grades/student/1/semester/1/year/2023/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(82.0));

        verify(gradeService).getAverageGradeByStudentAndSemester(1L, "1", 2023);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAverageGradeBySubjectAndType_Success() throws Exception {
        when(gradeService.getAverageGradeBySubjectAndType(1L, Grade.GradeType.QUIZ)).thenReturn(90.0);

        mockMvc.perform(get("/api/grades/subject/1/type/QUIZ/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(90.0));

        verify(gradeService).getAverageGradeBySubjectAndType(1L, Grade.GradeType.QUIZ);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMaxGradeBySubjectAndType_Success() throws Exception {
        when(gradeService.getMaxGradeBySubjectAndType(1L, Grade.GradeType.QUIZ)).thenReturn(100.0);

        mockMvc.perform(get("/api/grades/subject/1/type/QUIZ/max"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100.0));

        verify(gradeService).getMaxGradeBySubjectAndType(1L, Grade.GradeType.QUIZ);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMinGradeBySubjectAndType_Success() throws Exception {
        when(gradeService.getMinGradeBySubjectAndType(1L, Grade.GradeType.QUIZ)).thenReturn(70.0);

        mockMvc.perform(get("/api/grades/subject/1/type/QUIZ/min"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(70.0));

        verify(gradeService).getMinGradeBySubjectAndType(1L, Grade.GradeType.QUIZ);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPassingRateByStudent_Success() throws Exception {
        when(gradeService.getPassingRateByStudent(1L)).thenReturn(75.0);

        mockMvc.perform(get("/api/grades/student/1/passing-rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(75.0));

        verify(gradeService).getPassingRateByStudent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAcademicYears_Success() throws Exception {
        List<Integer> years = List.of(2023, 2024);
        when(gradeService.getAllAcademicYears()).thenReturn(years);

        mockMvc.perform(get("/api/grades/academic-years"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(2023));

        verify(gradeService).getAllAcademicYears();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSemestersByAcademicYear_Success() throws Exception {
        List<String> semesters = List.of("1", "2");
        when(gradeService.getSemestersByAcademicYear(2023)).thenReturn(semesters);

        mockMvc.perform(get("/api/grades/year/2023/semesters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("1"));

        verify(gradeService).getSemestersByAcademicYear(2023);
    }
}