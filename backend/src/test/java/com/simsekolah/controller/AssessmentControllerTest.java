package com.simsekolah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simsekolah.dto.request.AssessmentSearchRequest;
import com.simsekolah.dto.request.CreateAssessmentRequest;
import com.simsekolah.dto.request.GradeAssessmentRequest;
import com.simsekolah.dto.request.UpdateAssessmentRequest;
import com.simsekolah.dto.response.AssessmentResponse;
import com.simsekolah.dto.response.StudentAssessmentResponse;
import com.simsekolah.enums.AssessmentType;
import com.simsekolah.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssessmentControllerTest {

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController)
                .setValidator(mock(Validator.class))
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAssessment_Success() throws Exception {
        CreateAssessmentRequest request = new CreateAssessmentRequest();
        request.setTitle("Test Assessment");
        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);

        when(assessmentService.createAssessment(any(CreateAssessmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(assessmentService).createAssessment(any(CreateAssessmentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAssessments_Success() throws Exception {
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getAllAssessments(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getAllAssessments(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentById_Success() throws Exception {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);
        when(assessmentService.getAssessmentResponseById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/assessments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(assessmentService).getAssessmentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAssessment_Success() throws Exception {
        UpdateAssessmentRequest request = new UpdateAssessmentRequest();
        request.setTitle("Updated Title");
        AssessmentResponse response = new AssessmentResponse();
        response.setId(1L);

        when(assessmentService.updateAssessment(eq(1L), any(UpdateAssessmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/assessments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(assessmentService).updateAssessment(eq(1L), any(UpdateAssessmentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAssessment_Success() throws Exception {
        doNothing().when(assessmentService).deleteAssessment(1L);

        mockMvc.perform(delete("/api/v1/assessments/1"))
                .andExpect(status().isNoContent());

        verify(assessmentService).deleteAssessment(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchAssessments_Success() throws Exception {
        AssessmentSearchRequest request = new AssessmentSearchRequest();
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.searchAssessments(any(AssessmentSearchRequest.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(post("/api/v1/assessments/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).searchAssessments(any(AssessmentSearchRequest.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentsByType_Success() throws Exception {
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getAssessmentsByType(eq(AssessmentType.KUIS), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/type/KUIS")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getAssessmentsByType(eq(AssessmentType.KUIS), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentsBySubject_Success() throws Exception {
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getAssessmentsBySubject(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/subject/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getAssessmentsBySubject(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentsByDateRange_Success() throws Exception {
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getAssessmentsByDateRange(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/date-range")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getAssessmentsByDateRange(any(LocalDate.class), any(LocalDate.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void gradeAssessment_Success() throws Exception {
        GradeAssessmentRequest request = new GradeAssessmentRequest();
        request.setStudentGrades(Collections.emptyList());
        List<StudentAssessmentResponse> responses = Collections.emptyList();
        when(assessmentService.gradeAssessment(eq(1L), any(GradeAssessmentRequest.class))).thenReturn(responses);

        mockMvc.perform(post("/api/v1/assessments/1/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(assessmentService).gradeAssessment(eq(1L), any(GradeAssessmentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentGrades_Success() throws Exception {
        Page<StudentAssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getAssessmentGrades(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/1/grades")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getAssessmentGrades(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentAssessments_Success() throws Exception {
        Page<StudentAssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getStudentAssessments(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/student/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(assessmentService).getStudentAssessments(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAssessmentStatistics_Success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        when(assessmentService.getAssessmentStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/assessments/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(assessmentService).getAssessmentStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void calculateStudentGPA_Success() throws Exception {
        when(assessmentService.calculateStudentGPA(eq(1L), isNull(String.class), isNull(Integer.class))).thenReturn(BigDecimal.valueOf(3.5));

        mockMvc.perform(get("/api/v1/assessments/student/1/gpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpa").value(3.5));

        verify(assessmentService).calculateStudentGPA(eq(1L), isNull(String.class), isNull(Integer.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUpcomingAssessments_Success() throws Exception {
        Page<AssessmentResponse> page = new PageImpl<>(Collections.emptyList());
        when(assessmentService.getUpcomingAssessments(eq(7), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assessments/upcoming")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(assessmentService).getUpcomingAssessments(eq(7), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void bulkGradeAssessment_Success() throws Exception {
        List<GradeAssessmentRequest> requests = Collections.emptyList();
        List<StudentAssessmentResponse> responses = Collections.emptyList();
        when(assessmentService.bulkGradeAssessment(eq(1L), anyList())).thenReturn(responses);

        mockMvc.perform(post("/api/v1/assessments/1/bulk-grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bulk grading completed successfully"));

        verify(assessmentService).bulkGradeAssessment(eq(1L), anyList());
    }
}