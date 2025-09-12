package com.simsekolah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simsekolah.entity.ClassRoom;
import com.simsekolah.service.ClassRoomService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

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
class ClassRoomControllerTest {

    @Mock
    private ClassRoomService classRoomService;

    @InjectMocks
    private ClassRoomController classRoomController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(classRoomController)
                .setValidator(mock(Validator.class))
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createClassRoom_Success() throws Exception {
        ClassRoom request = new ClassRoom();
        request.setName("Class 10A");
        ClassRoom response = new ClassRoom();
        response.setId(1L);
        response.setName("Class 10A");

        when(classRoomService.createClassRoom(any(ClassRoom.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Class 10A"));

        verify(classRoomService).createClassRoom(any(ClassRoom.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllClassRooms_Success() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Collections.emptyList());
        when(classRoomService.getAllClassRooms(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/classrooms")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(classRoomService).getAllClassRooms(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getClassRoomById_Success() throws Exception {
        ClassRoom response = new ClassRoom();
        response.setId(1L);
        response.setName("Class 10A");
        when(classRoomService.getClassRoomById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/classrooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Class 10A"));

        verify(classRoomService).getClassRoomById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateClassRoom_Success() throws Exception {
        ClassRoom request = new ClassRoom();
        request.setName("Updated Class 10A");
        ClassRoom response = new ClassRoom();
        response.setId(1L);
        response.setName("Updated Class 10A");

        when(classRoomService.updateClassRoom(eq(1L), any(ClassRoom.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Class 10A"));

        verify(classRoomService).updateClassRoom(eq(1L), any(ClassRoom.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteClassRoom_Success() throws Exception {
        doNothing().when(classRoomService).deleteClassRoom(1L);

        mockMvc.perform(delete("/api/v1/classrooms/1"))
                .andExpect(status().isNoContent());

        verify(classRoomService).deleteClassRoom(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getClassRoomsByGrade_Success() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Collections.emptyList());
        when(classRoomService.getClassRoomsByGrade(eq(10), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/classrooms/grade/10")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(classRoomService).getClassRoomsByGrade(eq(10), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getActiveClassRooms_Success() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Collections.emptyList());
        when(classRoomService.getActiveClassRooms(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/classrooms/active")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(classRoomService).getActiveClassRooms(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getClassRoomStats_Success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        when(classRoomService.getClassRoomStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/classrooms/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(classRoomService).getClassRoomStatistics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchClassRooms_Success() throws Exception {
        Page<ClassRoom> page = new PageImpl<>(Collections.emptyList());
        when(classRoomService.searchClassRooms(eq("10A"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/classrooms/search")
                        .param("query", "10A")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(classRoomService).searchClassRooms(eq("10A"), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAvailableClassRooms_Success() throws Exception {
        List<ClassRoom> classRooms = Collections.emptyList();
        when(classRoomService.getClassRoomsWithAvailableSpace()).thenReturn(classRooms);

        mockMvc.perform(get("/api/v1/classrooms/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(classRoomService).getClassRoomsWithAvailableSpace();
    }
}