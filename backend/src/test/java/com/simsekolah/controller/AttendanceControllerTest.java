package com.simsekolah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simsekolah.dto.request.AttendanceReportRequest;
import com.simsekolah.dto.request.BulkAttendanceRequest;
import com.simsekolah.dto.request.CreateAttendanceRequest;
import com.simsekolah.dto.request.UpdateAttendanceRequest;
import com.simsekolah.dto.response.AttendanceReportResponse;
import com.simsekolah.dto.response.AttendanceResponse;
import com.simsekolah.enums.AttendanceStatus;
import com.simsekolah.service.AttendanceReportService;
import com.simsekolah.service.AttendanceService;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private AttendanceReportService attendanceReportService;

    @InjectMocks
    private AttendanceController attendanceController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
                .setValidator(mock(Validator.class))
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void recordAttendance_Success() throws Exception {
        CreateAttendanceRequest request = new CreateAttendanceRequest();
        request.setStudentId(1L);
        request.setTeachingActivityId(1L);
        AttendanceResponse response = new AttendanceResponse();
        response.setId(1L);

        when(attendanceService.recordAttendance(any(CreateAttendanceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(attendanceService).recordAttendance(any(CreateAttendanceRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAttendance_Success() throws Exception {
        Page<AttendanceResponse> page = new PageImpl<>(Collections.emptyList());
        when(attendanceService.getAllAttendance(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/attendance")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(attendanceService).getAllAttendance(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceById_Success() throws Exception {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(1L);
        when(attendanceService.getAttendanceById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/attendance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(attendanceService).getAttendanceById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAttendance_Success() throws Exception {
        UpdateAttendanceRequest request = new UpdateAttendanceRequest();
        AttendanceResponse response = new AttendanceResponse();
        response.setId(1L);

        when(attendanceService.updateAttendance(eq(1L), any(UpdateAttendanceRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/attendance/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(attendanceService).updateAttendance(eq(1L), any(UpdateAttendanceRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAttendance_Success() throws Exception {
        doNothing().when(attendanceService).deleteAttendance(1L);

        mockMvc.perform(delete("/api/v1/attendance/1"))
                .andExpect(status().isNoContent());

        verify(attendanceService).deleteAttendance(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceByStudent_Success() throws Exception {
        Page<AttendanceResponse> page = new PageImpl<>(Collections.emptyList());
        when(attendanceService.getAttendanceByStudent(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/attendance/student/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(attendanceService).getAttendanceByStudent(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceByDateRange_Success() throws Exception {
        Page<AttendanceResponse> page = new PageImpl<>(Collections.emptyList());
        when(attendanceService.getAttendanceByDateRange(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/attendance/date-range")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(attendanceService).getAttendanceByDateRange(any(LocalDate.class), any(LocalDate.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceByStatus_Success() throws Exception {
        Page<AttendanceResponse> page = new PageImpl<>(Collections.emptyList());
        when(attendanceService.getAttendanceByStatus(eq(AttendanceStatus.PRESENT), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/attendance/status/PRESENT")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(attendanceService).getAttendanceByStatus(eq(AttendanceStatus.PRESENT), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void recordBulkAttendance_Success() throws Exception {
        BulkAttendanceRequest request = new BulkAttendanceRequest();
        request.setStudentAttendances(Collections.emptyList());
        AttendanceService.BulkAttendanceResult result = mock(AttendanceService.BulkAttendanceResult.class);
        when(result.getTotalProcessed()).thenReturn(0);
        when(result.getSuccessCount()).thenReturn(0);
        when(result.getErrorCount()).thenReturn(0);
        when(result.getSuccessfulRecords()).thenReturn(Collections.emptyList());
        when(result.getErrors()).thenReturn(Collections.emptyList());
        when(attendanceService.bulkRecordAttendance(any(BulkAttendanceRequest.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/attendance/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bulk attendance recorded successfully"));

        verify(attendanceService).bulkRecordAttendance(any(BulkAttendanceRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAttendanceStatistics_Success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        when(attendanceService.getAttendanceStatistics(isNull(LocalDate.class), isNull(LocalDate.class))).thenReturn(stats);

        mockMvc.perform(get("/api/v1/attendance/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(attendanceService).getAttendanceStatistics(isNull(LocalDate.class), isNull(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateAttendanceReport_Success() throws Exception {
        AttendanceReportRequest request = new AttendanceReportRequest();
        AttendanceReportResponse response = new AttendanceReportResponse();

        when(attendanceReportService.generateAttendanceReport(any(AttendanceReportRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/attendance/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(attendanceReportService).generateAttendanceReport(any(AttendanceReportRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportAttendanceReport_Success() throws Exception {
        AttendanceReportRequest request = new AttendanceReportRequest();

        when(attendanceReportService.exportAttendanceReportToExcel(any(AttendanceReportRequest.class))).thenReturn(mock(java.io.ByteArrayOutputStream.class));

        mockMvc.perform(post("/api/v1/attendance/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(attendanceReportService).exportAttendanceReportToExcel(any(AttendanceReportRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDailyAttendanceSummary_Success() throws Exception {
        Map<String, Object> summary = new HashMap<>();
        when(attendanceService.getDailyAttendanceSummary(any(LocalDate.class))).thenReturn(summary);

        mockMvc.perform(get("/api/v1/attendance/daily-summary")
                        .param("date", "2023-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(attendanceService).getDailyAttendanceSummary(any(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentAttendanceRate_Success() throws Exception {
        when(attendanceService.calculateStudentAttendanceRate(eq(1L), isNull(LocalDate.class), isNull(LocalDate.class))).thenReturn(95.0);

        mockMvc.perform(get("/api/v1/attendance/student/1/rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceRate").value(95.0));

        verify(attendanceService).calculateStudentAttendanceRate(eq(1L), isNull(LocalDate.class), isNull(LocalDate.class));
    }
}