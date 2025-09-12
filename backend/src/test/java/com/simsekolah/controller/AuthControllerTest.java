package com.simsekolah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simsekolah.dto.request.LoginRequest;
import com.simsekolah.dto.request.PasswordResetConfirmRequest;
import com.simsekolah.dto.request.PasswordResetRequest;
import com.simsekolah.dto.request.RefreshTokenRequest;
import com.simsekolah.dto.response.AuthenticationResponse;
import com.simsekolah.service.AuthenticationService;
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
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setValidator(mock(Validator.class))
                .build();
    }

    @Test
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setIdentifier("test@example.com");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("mock-jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600)
                .build();

        when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mock-jwt-token"));

        verify(authenticationService).authenticate(any(LoginRequest.class));
    }

    @Test
    @WithMockUser
    void logout_Success() throws Exception {
        doNothing().when(authenticationService).logout(anyString());

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        verify(authenticationService).logout("mock-token");
    }

    @Test
    void refreshToken_Success() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("mock-refresh-token");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("new-mock-jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600)
                .build();

        when(authenticationService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-mock-jwt-token"));

        verify(authenticationService).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    void requestPasswordReset_Success() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");

        doNothing().when(authenticationService).initiatePasswordReset(any(PasswordResetRequest.class));

        mockMvc.perform(post("/api/v1/auth/password-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset email sent successfully"));

        verify(authenticationService).initiatePasswordReset(any(PasswordResetRequest.class));
    }

    @Test
    void confirmPasswordReset_Success() throws Exception {
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
        request.setToken("mock-reset-token");
        request.setNewPassword("newPassword123");

        doNothing().when(authenticationService).confirmPasswordReset(any(PasswordResetConfirmRequest.class));

        mockMvc.perform(post("/api/v1/auth/password-reset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset successful"));

        verify(authenticationService).confirmPasswordReset(any(PasswordResetConfirmRequest.class));
    }

    @Test
    @WithMockUser
    void validateToken_Success() throws Exception {
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("userId", 1L);
        when(authenticationService.validateToken("mock-token")).thenReturn(tokenInfo);

        mockMvc.perform(get("/api/v1/auth/validate")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));

        verify(authenticationService).validateToken("mock-token");
    }

    @Test
    @WithMockUser
    void getCurrentUser_Success() throws Exception {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", 1L);
        when(authenticationService.getCurrentUserInfo("mock-token")).thenReturn(userInfo);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userId").value(1L));

        verify(authenticationService).getCurrentUserInfo("mock-token");
    }

    @Test
    @WithMockUser
    void changePassword_Success() throws Exception {
        doNothing().when(authenticationService).changePassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .param("currentPassword", "oldPassword")
                        .param("newPassword", "newPassword123")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        verify(authenticationService).changePassword("mock-token", "oldPassword", "newPassword123");
    }

    @Test
    @WithMockUser
    void getAuthStatus_Success() throws Exception {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", 1L);
        when(authenticationService.isTokenValid("mock-token")).thenReturn(true);
        when(authenticationService.getCurrentUserInfo("mock-token")).thenReturn(userInfo);

        mockMvc.perform(get("/api/v1/auth/status")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true));

        verify(authenticationService).isTokenValid("mock-token");
        verify(authenticationService).getCurrentUserInfo("mock-token");
    }
}
