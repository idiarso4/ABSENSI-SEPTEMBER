package com.simsekolah.controller;

import com.simsekolah.service.ExcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExcelControllerTest {

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private ExcelController excelController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(excelController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void importStudents_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());

        ExcelService.ValidationResult validation = new ExcelService.ValidationResult(true, List.of());
        when(excelService.validateExcelFile(any())).thenReturn(validation);

        ExcelService.ImportResult importResult = new ExcelService.ImportResult(5, 5, 0, List.of(), List.of());
        when(excelService.importStudentsFromExcel(any())).thenReturn(importResult);

        mockMvc.perform(multipart("/api/excel/import/students")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulImports").value(5))
                .andExpect(jsonPath("$.failedImports").value(0));

        verify(excelService).validateExcelFile(any());
        verify(excelService).importStudentsFromExcel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportAllStudents_Success() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelService.exportAllStudentsToExcel()).thenReturn(outputStream);

        mockMvc.perform(get("/api/excel/export/students"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")))
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=")));

        verify(excelService).exportAllStudentsToExcel();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportStudentsByClassRoom_Success() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelService.exportStudentsByClassRoomToExcel(1L)).thenReturn(outputStream);

        mockMvc.perform(get("/api/excel/export/students/classroom/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")))
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=")));

        verify(excelService).exportStudentsByClassRoomToExcel(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void downloadStudentTemplate_Success() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelService.generateStudentImportTemplate()).thenReturn(outputStream);

        mockMvc.perform(get("/api/excel/template/students"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("spreadsheetml.sheet")))
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=student_import_template.xlsx")));

        verify(excelService).generateStudentImportTemplate();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void validateExcelFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());

        ExcelService.ValidationResult result = new ExcelService.ValidationResult(true, List.of());
        when(excelService.validateExcelFile(any())).thenReturn(result);

        mockMvc.perform(multipart("/api/excel/validate")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));

        verify(excelService).validateExcelFile(any());
    }
}