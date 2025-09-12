package com.simsekolah.controller;

import com.simsekolah.entity.Subject;
import com.simsekolah.service.SubjectService;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSubject_Success() throws Exception {
        Subject request = new Subject();
        request.setCode("MATH101");
        Subject response = new Subject();
        response.setId(1L);

        when(subjectService.createSubject(any(Subject.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"MATH101\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(subjectService).createSubject(any(Subject.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllSubjects_Success() throws Exception {
        Page<Subject> page = new PageImpl<>(Collections.emptyList());
        when(subjectService.getAllSubjects(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/subjects")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(subjectService).getAllSubjects(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSubjectById_Success() throws Exception {
        Subject response = new Subject();
        response.setId(1L);
        when(subjectService.getSubjectById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(subjectService).getSubjectById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSubjectByCode_Success() throws Exception {
        Subject response = new Subject();
        response.setId(1L);
        when(subjectService.getSubjectByCode("MATH101")).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/subjects/code/MATH101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(subjectService).getSubjectByCode("MATH101");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSubject_Success() throws Exception {
        Subject request = new Subject();
        request.setCode("MATH102");
        Subject response = new Subject();
        response.setId(1L);
        when(subjectService.updateSubject(eq(1L), any(Subject.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"MATH102\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(subjectService).updateSubject(eq(1L), any(Subject.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSubject_Success() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/api/v1/subjects/1"))
                .andExpect(status().isNoContent());

        verify(subjectService).deleteSubject(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchSubjects_Success() throws Exception {
        Page<Subject> page = new PageImpl<>(Collections.emptyList());
        when(subjectService.searchSubjects(eq("Math"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/subjects/search")
                        .param("query", "Math")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(subjectService).searchSubjects(eq("Math"), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSubjectsByType_Success() throws Exception {
        List<Subject> subjects = Collections.emptyList();
        when(subjectService.getSubjectsByType(Subject.SubjectType.THEORY)).thenReturn(subjects);

        mockMvc.perform(get("/api/v1/subjects/type/THEORY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(subjectService).getSubjectsByType(Subject.SubjectType.THEORY);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSubjectStatistics_Success() throws Exception {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("activeCount", 10L);
        when(subjectService.countActiveSubjects()).thenReturn(10L);
        when(subjectService.getAllSubjects(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/v1/subjects/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeCount").value(10))
                .andExpect(jsonPath("$.totalCount").value(1));

        verify(subjectService).countActiveSubjects();
        verify(subjectService).getAllSubjects(any(Pageable.class));
    }
}