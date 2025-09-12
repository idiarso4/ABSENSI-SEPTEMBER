package com.simsekolah.controller;

import com.simsekolah.service.DataInitializerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DataInitControllerTest {

    @Mock
    private DataInitializerService dataInitializerService;

    @InjectMocks
    private DataInitController dataInitController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataInitController).build();
    }

    @Test
    void initializeData_Success() throws Exception {
        doNothing().when(dataInitializerService).initializeTestData();

        mockMvc.perform(post("/api/data-init/initialize"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Test data initialized successfully!"))
                .andExpect(jsonPath("$.data.status").value(1));

        verify(dataInitializerService).initializeTestData();
    }

    @Test
    void clearData_Success() throws Exception {
        doNothing().when(dataInitializerService).clearAllData();

        mockMvc.perform(post("/api/data-init/clear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("All test data cleared successfully!"))
                .andExpect(jsonPath("$.data.status").value(1));

        verify(dataInitializerService).clearAllData();
    }

    @Test
    void getDataStatus_Success() throws Exception {
        mockMvc.perform(get("/api/data-init/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.hasData").exists())
                .andExpect(jsonPath("$.data.status").value(1));

        // getDataCounts is private, no direct mock needed
    }

    @Test
    void resetData_Success() throws Exception {
        doNothing().when(dataInitializerService).clearAllData();
        doNothing().when(dataInitializerService).initializeTestData();

        mockMvc.perform(post("/api/data-init/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Database reset and reinitialized successfully!"))
                .andExpect(jsonPath("$.data.status").value(1));

        verify(dataInitializerService).clearAllData();
        verify(dataInitializerService).initializeTestData();
    }
}