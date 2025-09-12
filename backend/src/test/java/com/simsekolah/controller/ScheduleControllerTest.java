package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateScheduleRequest;
import com.simsekolah.dto.request.ScheduleSearchRequest;
import com.simsekolah.dto.request.UpdateScheduleRequest;
import com.simsekolah.dto.response.ScheduleResponse;
import com.simsekolah.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSchedule_Success() throws Exception {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(1L);
        when(scheduleService.createSchedule(any(CreateScheduleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"classRoomId\":1,\"subjectId\":1,\"teacherId\":1,\"startTime\":\"2023-01-01T09:00:00\",\"endTime\":\"2023-01-01T10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(scheduleService).createSchedule(any(CreateScheduleRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllSchedules_Success() throws Exception {
        List<ScheduleResponse> schedules = Collections.emptyList();
        when(scheduleService.searchSchedules(any(ScheduleSearchRequest.class), any(Pageable.class))).thenReturn(new PageImpl<>(schedules));

        mockMvc.perform(get("/api/v1/schedules")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(scheduleService).searchSchedules(any(ScheduleSearchRequest.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScheduleById_Success() throws Exception {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(1L);
        when(scheduleService.getScheduleById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/schedules/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(scheduleService).getScheduleById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSchedule_Success() throws Exception {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(1L);
        when(scheduleService.updateSchedule(eq(1L), any(UpdateScheduleRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/schedules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"classRoomId\":1,\"subjectId\":1,\"teacherId\":1,\"startTime\":\"2023-01-01T09:00:00\",\"endTime\":\"2023-01-01T10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(scheduleService).updateSchedule(eq(1L), any(UpdateScheduleRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSchedule_Success() throws Exception {
        doNothing().when(scheduleService).deleteSchedule(1L);

        mockMvc.perform(delete("/api/v1/schedules/1"))
                .andExpect(status().isNoContent());

        verify(scheduleService).deleteSchedule(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSchedulesByClassRoom_Success() throws Exception {
        List<ScheduleResponse> schedules = Collections.emptyList();
        when(scheduleService.getSchedulesByClassRoom(eq(1L), eq("2024/2025"), eq(1))).thenReturn(schedules);

        mockMvc.perform(get("/api/v1/schedules/classroom/1")
                        .param("academicYear", "2024/2025")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(scheduleService).getSchedulesByClassRoom(eq(1L), eq("2024/2025"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSchedulesByTeacher_Success() throws Exception {
        List<ScheduleResponse> schedules = Collections.emptyList();
        when(scheduleService.getSchedulesByTeacher(eq(1L), eq("2024/2025"), eq(1))).thenReturn(schedules);

        mockMvc.perform(get("/api/v1/schedules/teacher/1")
                        .param("academicYear", "2024/2025")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(scheduleService).getSchedulesByTeacher(eq(1L), eq("2024/2025"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSchedulesBySubject_Success() throws Exception {
        List<ScheduleResponse> schedules = Collections.emptyList();
        when(scheduleService.getSchedulesBySubject(eq(1L), eq("2024/2025"), eq(1))).thenReturn(schedules);

        mockMvc.perform(get("/api/v1/schedules/subject/1")
                        .param("academicYear", "2024/2025")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(scheduleService).getSchedulesBySubject(eq(1L), eq("2024/2025"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScheduleConflicts_Success() throws Exception {
        List<Map<String, Object>> conflicts = Collections.emptyList();
        when(scheduleService.detectExistingConflicts(eq("2024/2025"), eq(1))).thenReturn(conflicts);

        mockMvc.perform(get("/api/v1/schedules/conflicts")
                        .param("academicYear", "2024/2025")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(scheduleService).detectExistingConflicts(eq("2024/2025"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getScheduleStatistics_Success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        when(scheduleService.getScheduleStatistics(eq("2024/2025"), eq(1))).thenReturn(stats);

        mockMvc.perform(get("/api/v1/schedules/statistics")
                        .param("academicYear", "2024/2025")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(scheduleService).getScheduleStatistics(eq("2024/2025"), eq(1));
    }
}