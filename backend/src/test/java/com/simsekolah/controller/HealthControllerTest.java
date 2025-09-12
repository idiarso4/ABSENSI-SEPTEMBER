package com.simsekolah.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest {

    private HealthController healthController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
        // Set @Value properties using ReflectionTestUtils
        ReflectionTestUtils.setField(healthController, "appName", "SIM School");
        ReflectionTestUtils.setField(healthController, "appVersion", "1.0.0");
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void health_Success() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("SIM School"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("School Information Management System is running successfully"));
    }
}