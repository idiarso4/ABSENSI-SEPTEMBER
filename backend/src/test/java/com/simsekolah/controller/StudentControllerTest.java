package com.simsekolah.controller;

import com.simsekolah.dto.request.CreateStudentRequest;
import com.simsekolah.dto.request.StudentSearchRequest;
import com.simsekolah.dto.request.UpdateStudentRequest;
import com.simsekolah.dto.response.StudentResponse;
import com.simsekolah.enums.StudentStatus;
import com.simsekolah.service.ExcelService;
import com.simsekolah.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStudent_Success() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setNis("12345678");
        StudentResponse response = new StudentResponse();
        response.setId(1L);

        when(studentService.createStudent(any(CreateStudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(studentService).createStudent(any(CreateStudentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllStudents_Success() throws Exception {
        Page<StudentResponse> page = new PageImpl<>(Collections.emptyList());
        when(studentService.getAllStudents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/students")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(studentService).getAllStudents(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentById_Success() throws Exception {
        StudentResponse response = new StudentResponse();
        response.setId(1L);
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(studentService).getStudentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentByNis_Success() throws Exception {
        StudentResponse response = new StudentResponse();
        response.setId(1L);
        when(studentService.getStudentByNis("12345678")).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/students/nis/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(studentService).getStudentByNis("12345678");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStudent_Success() throws Exception {
        UpdateStudentRequest request = new UpdateStudentRequest();
        StudentResponse response = new StudentResponse();
        response.setId(1L);
        when(studentService.updateStudent(eq(1L), any(UpdateStudentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(studentService).updateStudent(eq(1L), any(UpdateStudentRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchStudents_Success() throws Exception {
        StudentSearchRequest request = new StudentSearchRequest();
        Page<StudentResponse> page = new PageImpl<>(Collections.emptyList());
        when(studentService.searchStudents(any(StudentSearchRequest.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(post("/api/v1/students/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(studentService).searchStudents(any(StudentSearchRequest.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentsByClassRoom_Success() throws Exception {
        Page<StudentResponse> page = new PageImpl<>(Collections.emptyList());
        when(studentService.getStudentsByClassRoom(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/students/class/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(studentService).getStudentsByClassRoom(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentsByStatus_Success() throws Exception {
        Page<StudentResponse> page = new PageImpl<>(Collections.emptyList());
        when(studentService.getStudentsByStatus(eq(StudentStatus.ACTIVE), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/students/status/ACTIVE")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(studentService).getStudentsByStatus(eq(StudentStatus.ACTIVE), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignToClassRoom_Success() throws Exception {
        doNothing().when(studentService).assignToClassRoom(1L, 1L);

        mockMvc.perform(post("/api/v1/students/1/assign-class/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student assigned to class room successfully"));

        verify(studentService).assignToClassRoom(1L, 1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeFromClassRoom_Success() throws Exception {
        doNothing().when(studentService).removeFromClassRoom(1L);

        mockMvc.perform(post("/api/v1/students/1/remove-class"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student removed from class room successfully"));

        verify(studentService).removeFromClassRoom(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentStatistics_Success() throws Exception {
        Map<String, Object> statistics = new HashMap<>();
        when(studentService.getStudentStatistics()).thenReturn(statistics);

        mockMvc.perform(get("/api/v1/students/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(studentService).getStudentStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void importStudentsFromExcel_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "students.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());

        ExcelService.ImportResult importResult = mock(ExcelService.ImportResult.class);
        when(importResult.getSuccessfulImports()).thenReturn(5);
        when(importResult.getFailedImports()).thenReturn(0);
        when(importResult.getErrors()).thenReturn(Collections.emptyList());
        when(importResult.getImportedStudents()).thenReturn(Collections.emptyList());
        when(excelService.importStudentsFromExcel(any())).thenReturn(importResult);

        mockMvc.perform(multipart("/api/v1/students/excel/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulImports").value(5))
                .andExpect(jsonPath("$.failedImports").value(0));

        verify(excelService).importStudentsFromExcel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportStudentsToExcel_Success() throws Exception {
        StudentSearchRequest searchRequest = new StudentSearchRequest();
        List<StudentResponse> students = Collections.emptyList();
        Page<StudentResponse> studentPage = new PageImpl<>(students);
        when(studentService.searchStudents(any(StudentSearchRequest.class), eq(PageRequest.of(0, Integer.MAX_VALUE)))).thenReturn(studentPage);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelService.exportStudentsToExcel(anyList())).thenReturn(outputStream);

        mockMvc.perform(post("/api/v1/students/excel/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=")));

        verify(studentService).searchStudents(any(StudentSearchRequest.class), any(Pageable.class));
        verify(excelService).exportStudentsToExcel(anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void downloadExcelTemplate_Success() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(excelService.generateStudentImportTemplate()).thenReturn(outputStream);

        mockMvc.perform(get("/api/v1/students/excel/template"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=student_import_template.xlsx")));

        verify(excelService).generateStudentImportTemplate();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void bulkAssignToClassRoom_Success() throws Exception {
        doNothing().when(studentService).bulkAssignToClassRoom(anyList(), eq(1L));

        mockMvc.perform(post("/api/v1/students/bulk/assign-class/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Students assigned to class room successfully"))
                .andExpect(jsonPath("$.count").value(2));

        verify(studentService).bulkAssignToClassRoom(anyList(), eq(1L));
    }
}