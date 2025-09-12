package com.simsekolah.controller;

import com.simsekolah.dto.request.AcademicReportRequest;
import com.simsekolah.dto.request.AttendanceReportRequest;
import com.simsekolah.dto.response.AcademicReportResponse;
import com.simsekolah.dto.response.AttendanceReportResponse;
import com.simsekolah.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateAcademicReport_Success() throws Exception {
        AcademicReportRequest request = new AcademicReportRequest();
        AcademicReportResponse response = new AcademicReportResponse();
        when(reportService.generateAcademicReport(any(AcademicReportRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/reports/academic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"academicYear\":\"2023\",\"semester\":1}"))
                .andExpect(status().isOk());

        verify(reportService).generateAcademicReport(any(AcademicReportRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateStudentTranscript_Success() throws Exception {
        Map<String, Object> transcript = new HashMap<>();
        when(reportService.generateStudentTranscript(eq(1L), eq("2023"), eq(1))).thenReturn(transcript);

        mockMvc.perform(get("/api/v1/reports/academic/transcript/student/1")
                        .param("academicYear", "2023")
                        .param("semester", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateStudentTranscript(eq(1L), eq("2023"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateClassPerformanceReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateClassPerformanceReport(eq(1L), eq("2023"), eq(1))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/academic/performance/class/1")
                        .param("academicYear", "2023")
                        .param("semester", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateClassPerformanceReport(eq(1L), eq("2023"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateSubjectPerformanceReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateSubjectPerformanceReport(eq(1L), eq("2023"), eq(1))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/academic/performance/subject/1")
                        .param("academicYear", "2023")
                        .param("semester", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateSubjectPerformanceReport(eq(1L), eq("2023"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateGradeDistributionReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateGradeDistributionReport(eq("2023"), eq(1))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/academic/grade-distribution")
                        .param("academicYear", "2023")
                        .param("semester", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateGradeDistributionReport(eq("2023"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateTopPerformersReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateTopPerformersReport(eq("2023"), eq(1), eq(10))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/academic/top-performers")
                        .param("academicYear", "2023")
                        .param("semester", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk());

        verify(reportService).generateTopPerformersReport(eq("2023"), eq(1), eq(10));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateStudentsAtRiskReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateStudentsAtRiskReport(eq("2023"), eq(1), eq(2.0))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/academic/students-at-risk")
                        .param("academicYear", "2023")
                        .param("semester", "1")
                        .param("threshold", "2.0"))
                .andExpect(status().isOk());

        verify(reportService).generateStudentsAtRiskReport(eq("2023"), eq(1), eq(2.0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateAttendanceReport_Success() throws Exception {
        AttendanceReportRequest request = new AttendanceReportRequest();
        AttendanceReportResponse response = new AttendanceReportResponse();
        when(reportService.generateAttendanceReport(any(AttendanceReportRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/reports/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\":\"2023-01-01\",\"endDate\":\"2023-01-31\"}"))
                .andExpect(status().isOk());

        verify(reportService).generateAttendanceReport(any(AttendanceReportRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateDailyAttendanceSummary_Success() throws Exception {
        Map<String, Object> summary = new HashMap<>();
        when(reportService.generateDailyAttendanceSummary(any(LocalDate.class))).thenReturn(summary);

        mockMvc.perform(get("/api/v1/reports/attendance/daily-summary")
                        .param("date", "2023-01-01"))
                .andExpect(status().isOk());

        verify(reportService).generateDailyAttendanceSummary(any(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateMonthlyAttendanceReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateMonthlyAttendanceReport(eq(2023), eq(1))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/attendance/monthly")
                        .param("year", "2023")
                        .param("month", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateMonthlyAttendanceReport(eq(2023), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateClassAttendanceReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateClassAttendanceReport(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/attendance/class/1")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31"))
                .andExpect(status().isOk());

        verify(reportService).generateClassAttendanceReport(eq(1L), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateStudentAttendanceReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateStudentAttendanceReport(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/attendance/student/1")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31"))
                .andExpect(status().isOk());

        verify(reportService).generateStudentAttendanceReport(eq(1L), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateEnrollmentReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateEnrollmentReport(eq("2023"))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/administrative/enrollment")
                        .param("academicYear", "2023"))
                .andExpect(status().isOk());

        verify(reportService).generateEnrollmentReport(eq("2023"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateDemographicReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateDemographicReport()).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/administrative/demographics"))
                .andExpect(status().isOk());

        verify(reportService).generateDemographicReport();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateDashboardSummary_Success() throws Exception {
        Map<String, Object> summary = new HashMap<>();
        when(reportService.generateDashboardSummary()).thenReturn(summary);

        mockMvc.perform(get("/api/v1/reports/dashboard/summary"))
                .andExpect(status().isOk());

        verify(reportService).generateDashboardSummary();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateKPIReport_Success() throws Exception {
        Map<String, Object> report = new HashMap<>();
        when(reportService.generateKPIReport(eq("2023"), eq(1))).thenReturn(report);

        mockMvc.perform(get("/api/v1/reports/analytics/kpi")
                        .param("academicYear", "2023")
                        .param("semester", "1"))
                .andExpect(status().isOk());

        verify(reportService).generateKPIReport(eq("2023"), eq(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAvailableReportTypes_Success() throws Exception {
        List<Map<String, Object>> reportTypes = List.of(new HashMap<>());
        when(reportService.getAvailableReportTypes()).thenReturn(reportTypes);

        mockMvc.perform(get("/api/v1/reports/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(reportService).getAvailableReportTypes();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void clearReportCache_Success() throws Exception {
        doNothing().when(reportService).clearAllReportCache();

        mockMvc.perform(delete("/api/v1/reports/cache"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Report cache cleared successfully"));

        verify(reportService).clearAllReportCache();
    }
}