package com.simsekolah.controller;

import com.simsekolah.repository.StudentRepository;
import com.simsekolah.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardController dashboardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDashboardStats_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);
        when(userRepository.count()).thenReturn(20L);

        mockMvc.perform(get("/api/v1/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalStudents").value(100))
                .andExpect(jsonPath("$.totalUsers").value(20))
                .andExpect(jsonPath("$.activeClasses").exists())
                .andExpect(jsonPath("$.pendingTasks").value(0))
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(studentRepository).count();
        verify(userRepository).count();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentStats_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);

        mockMvc.perform(get("/api/v1/dashboard/students/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(100))
                .andExpect(jsonPath("$.activeCount").value(100))
                .andExpect(jsonPath("$.graduatedCount").value(0))
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(studentRepository).count();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserStats_Success() throws Exception {
        when(userRepository.count()).thenReturn(20L);

        mockMvc.perform(get("/api/v1/dashboard/users/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(20))
                .andExpect(jsonPath("$.activeCount").value(20))
                .andExpect(jsonPath("$.adminCount").value(1))
                .andExpect(jsonPath("$.teacherCount").exists())
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(userRepository).count();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getClassStats_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);

        mockMvc.perform(get("/api/v1/dashboard/classes/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeCount").exists())
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(studentRepository).count();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRecentActivities_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/activities/recent")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activities").isArray())
                .andExpect(jsonPath("$.totalCount").value(0))
                .andExpect(jsonPath("$.limit").value(10))
                .andExpect(jsonPath("$.lastUpdated").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getKPIs_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);
        when(userRepository.count()).thenReturn(20L);

        mockMvc.perform(get("/api/v1/dashboard/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentGrowth").value(12.5))
                .andExpect(jsonPath("$.totalStudents").value(100))
                .andExpect(jsonPath("$.teacherPerformance").value(87.3))
                .andExpect(jsonPath("$.attendanceRate").value(94.2))
                .andExpect(jsonPath("$.revenueGrowth").value(8.7))
                .andExpect(jsonPath("$.totalRevenue").value(150000000))
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(studentRepository).count();
        verify(userRepository).count();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRecentActivitiesAlias_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/recent-activities")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activities").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceTrend_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/attendance-trend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.period").value("7_days"))
                .andExpect(jsonPath("$.lastUpdated").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUpcomingEvents_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/upcoming-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.lastUpdated").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMonthlyPayroll_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/payroll-monthly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.period").value("6_months"))
                .andExpect(jsonPath("$.currency").value("IDR"))
                .andExpect(jsonPath("$.lastUpdated").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSystemHealth_Success() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.database").value("UP"))
                .andExpect(jsonPath("$.memory").value("OK"))
                .andExpect(jsonPath("$.diskSpace").value("OK"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}