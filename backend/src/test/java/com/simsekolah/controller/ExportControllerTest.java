package com.simsekolah.controller;

import com.simsekolah.service.ExportService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExportControllerTest {

    @Mock
    private ExportService exportService;

    @InjectMocks
    private ExportController exportController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exportController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportToExcel_Success() throws Exception {
        byte[] excelData = "mock excel data".getBytes();
        when(exportService.exportToExcel(anyString(), anyMap(), anyMap())).thenReturn(excelData);
        when(exportService.generateExportFilename(anyString(), eq("excel"), anyMap())).thenReturn("test.xlsx");
        when(exportService.getMimeType("excel")).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        mockMvc.perform(post("/api/v1/export/excel")
                        .param("reportType", "students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")))
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=")));

        verify(exportService).exportToExcel("students", anyMap(), anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportListToExcel_Success() throws Exception {
        byte[] excelData = "mock list excel data".getBytes();
        when(exportService.exportListToExcel(anyList(), anyList(), anyString())).thenReturn(excelData);
        when(exportService.generateExportFilename(anyString(), eq("excel"), anyMap())).thenReturn("list.xlsx");
        when(exportService.getMimeType("excel")).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        mockMvc.perform(post("/api/v1/export/excel/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"key\":\"value\"}]")
                        .param("sheetName", "Data"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")));

        verify(exportService).exportListToExcel(anyList(), isNull(), eq("Data"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportMultipleSheetsToExcel_Success() throws Exception {
        byte[] excelData = "mock multi sheet data".getBytes();
        when(exportService.exportMultipleSheetsToExcel(anyMap(), anyMap())).thenReturn(excelData);
        when(exportService.generateExportFilename(anyString(), eq("excel"), anyMap())).thenReturn("multi.xlsx");
        when(exportService.getMimeType("excel")).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        mockMvc.perform(post("/api/v1/export/excel/multiple-sheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sheet1\":[{\"key\":\"value\"}]}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")));

        verify(exportService).exportMultipleSheetsToExcel(anyMap(), anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportToCSV_Success() throws Exception {
        byte[] csvData = "mock csv data".getBytes();
        when(exportService.exportToCSV(anyList(), anyList(), eq(","))).thenReturn(csvData);
        when(exportService.generateExportFilename(anyString(), eq("csv"), anyMap())).thenReturn("data.csv");
        when(exportService.getMimeType("csv")).thenReturn("text/csv");

        mockMvc.perform(post("/api/v1/export/csv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"key\":\"value\"}]"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"));

        verify(exportService).exportToCSV(anyList(), isNull(), eq(","));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportMultipleCSV_Success() throws Exception {
        byte[] zipData = "mock zip data".getBytes();
        when(exportService.exportMultipleCSV(anyMap(), anyMap())).thenReturn(zipData);
        when(exportService.generateExportFilename(anyString(), eq("zip"), anyMap())).thenReturn("multi.csv.zip");

        mockMvc.perform(post("/api/v1/export/csv/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"table1\":[{\"key\":\"value\"}]}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE));

        verify(exportService).exportMultipleCSV(anyMap(), anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportToJSON_Success() throws Exception {
        byte[] jsonData = "{\"key\":\"value\"}".getBytes();
        when(exportService.exportToFormattedJSON(anyMap(), eq(false))).thenReturn(jsonData);
        when(exportService.generateExportFilename(anyString(), eq("json"), anyMap())).thenReturn("data.json");
        when(exportService.getMimeType("json")).thenReturn("application/json");

        mockMvc.perform(post("/api/v1/export/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));

        verify(exportService).exportToFormattedJSON(anyMap(), eq(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void startAsyncExport_Success() throws Exception {
        when(exportService.startAsyncExport(anyString(), anyString(), anyMap(), anyMap())).thenReturn("job-123");

        mockMvc.perform(post("/api/v1/export/async")
                        .param("exportType", "students")
                        .param("format", "excel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.jobId").value("job-123"))
                .andExpect(jsonPath("$.message").value("Export job started successfully"));

        verify(exportService).startAsyncExport("students", "excel", anyMap(), anyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExportJobStatus_Success() throws Exception {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "COMPLETED");
        when(exportService.getExportJobStatus("job-123")).thenReturn(status);

        mockMvc.perform(get("/api/v1/export/async/job-123/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(exportService).getExportJobStatus("job-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExportJobResult_Success() throws Exception {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "COMPLETED");
        status.put("exportType", "students");
        status.put("format", "excel");
        when(exportService.getExportJobStatus("job-123")).thenReturn(status);
        when(exportService.getExportJobResult("job-123")).thenReturn("result data".getBytes());
        when(exportService.generateExportFilename(anyString(), anyString(), anyMap())).thenReturn("job-result.xlsx");
        when(exportService.getMimeType("excel")).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        mockMvc.perform(get("/api/v1/export/async/job-123/result"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")));

        verify(exportService).getExportJobStatus("job-123");
        verify(exportService).getExportJobResult("job-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelExportJob_Success() throws Exception {
        doNothing().when(exportService).cancelExportJob("job-123");

        mockMvc.perform(delete("/api/v1/export/async/job-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Export job cancelled successfully"));

        verify(exportService).cancelExportJob("job-123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSupportedFormats_Success() throws Exception {
        List<String> formats = List.of("excel", "csv", "json");
        when(exportService.getSupportedFormats()).thenReturn(formats);

        mockMvc.perform(get("/api/v1/export/formats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formats").isArray())
                .andExpect(jsonPath("$.formats[0]").value("excel"))
                .andExpect(jsonPath("$.count").value(3));

        verify(exportService).getSupportedFormats();
    }
}