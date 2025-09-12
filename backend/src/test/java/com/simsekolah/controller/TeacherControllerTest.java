package com.simsekolah.controller;

import com.simsekolah.dto.response.UserResponse;
import com.simsekolah.enums.UserType;
import com.simsekolah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private com.simsekolah.repository.UserRepository userRepository;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTeachers_Success() throws Exception {
        Page<UserResponse> page = new PageImpl<>(Collections.emptyList());
        when(userService.getUsersByType(eq(UserType.TEACHER), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/teachers")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(userService).getUsersByType(eq(UserType.TEACHER), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTeacherById_Success() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userService).getUserById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchTeachers_Success() throws Exception {
        Page<UserResponse> page = new PageImpl<>(Collections.emptyList());
        when(userService.searchUsersByTypeAndQuery(eq(UserType.TEACHER), eq("math"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/teachers/search")
                        .param("query", "math")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(userService).searchUsersByTypeAndQuery(eq(UserType.TEACHER), eq("math"), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getActiveTeachers_Success() throws Exception {
        Page<UserResponse> page = new PageImpl<>(Collections.emptyList());
        when(userService.getActiveUsersByType(eq(UserType.TEACHER), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/teachers/active")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(userService).getActiveUsersByType(eq(UserType.TEACHER), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTeacherStats_Success() throws Exception {
        when(userRepository.countByUserType(eq(UserType.TEACHER))).thenReturn(10L);
        when(userRepository.countByUserTypeAndIsActive(eq(UserType.TEACHER), eq(true))).thenReturn(8L);

        mockMvc.perform(get("/api/v1/teachers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTeachers").value(10))
                .andExpect(jsonPath("$.activeTeachers").value(8))
                .andExpect(jsonPath("$.inactiveTeachers").value(2));

        verify(userRepository).countByUserType(eq(UserType.TEACHER));
        verify(userRepository).countByUserTypeAndIsActive(eq(UserType.TEACHER), eq(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTeachersByDepartment_Success() throws Exception {
        List<UserResponse> teachers = Collections.emptyList();
        when(userRepository.findByUserType(eq(UserType.TEACHER))).thenReturn(Collections.emptyList());
        when(teachers.stream().map(user -> new UserResponse()).collect(Collectors.toList())).thenReturn(teachers);

        mockMvc.perform(get("/api/v1/teachers/department/general"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userRepository).findByUserType(eq(UserType.TEACHER));
    }
}