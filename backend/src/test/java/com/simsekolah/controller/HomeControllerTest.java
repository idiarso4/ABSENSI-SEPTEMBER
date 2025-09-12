package com.simsekolah.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {

    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void home_Success() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("School Information Management System"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.status").value("Running"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Welcome to SIM Backend API"))
                .andExpect(jsonPath("$.endpoints.API Documentation").value("/swagger-ui.html"))
                .andExpect(jsonPath("$.endpoints.Health Check").value("/actuator/health"))
                .andExpect(jsonPath("$.endpoints.API Docs JSON").value("/api-docs"));
    }

    @Test
    void health_Success() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.application").value("SIM Backend"));
    }

    @Test
    void info_Success() throws Exception {
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("School Information Management System"))
                .andExpect(jsonPath("$.description").value("Spring Boot backend for School Information Management"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.java.version").exists())
                .andExpect(jsonPath("$.spring.version").exists())
                .andExpect(jsonPath("$.features.Authentication").value("JWT-based authentication and authorization"))
                .andExpect(jsonPath("$.features.User Management").value("Complete user and role management"))
                .andExpect(jsonPath("$.features.Student Management").value("Student lifecycle and academic tracking"))
                .andExpect(jsonPath("$.features.Attendance").value("Attendance recording and reporting"))
                .andExpect(jsonPath("$.features.Assessment").value("Assessment and grading system"))
                .andExpect(jsonPath("$.features.Academic Reports").value("Comprehensive academic reporting"))
                .andExpect(jsonPath("$.features.Schedule Management").value("Timetable and schedule management"))
                .andExpect(jsonPath("$.features.Excel Integration").value("Import/export functionality"));
    }
}