package com.simsekolah.controller;

import com.simsekolah.entity.Payment;
import com.simsekolah.service.PaymentService;
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

import java.time.LocalDate;
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
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPayment_Success() throws Exception {
        Payment request = new Payment();
        request.setId(1L);
        Payment response = new Payment();
        response.setId(1L);
        when(paymentService.createPayment(any(Payment.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(paymentService).createPayment(any(Payment.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllPayments_Success() throws Exception {
        Page<Payment> page = new PageImpl<>(Collections.emptyList());
        when(paymentService.getAllPayments(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/payments")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(paymentService).getAllPayments(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPaymentById_Success() throws Exception {
        Payment payment = new Payment();
        payment.setId(1L);
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/api/v1/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(paymentService).getPaymentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePayment_Success() throws Exception {
        Payment request = new Payment();
        request.setId(1L);
        Payment response = new Payment();
        response.setId(1L);
        when(paymentService.updatePayment(eq(1L), any(Payment.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(paymentService).updatePayment(eq(1L), any(Payment.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePayment_Success() throws Exception {
        doNothing().when(paymentService).deletePayment(1L);

        mockMvc.perform(delete("/api/v1/payments/1"))
                .andExpect(status().isNoContent());

        verify(paymentService).deletePayment(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPaymentsByStudent_Success() throws Exception {
        Page<Payment> page = new PageImpl<>(Collections.emptyList());
        when(paymentService.getPaymentsByStudent(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/payments/student/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(paymentService).getPaymentsByStudent(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOverduePayments_Success() throws Exception {
        List<Payment> payments = Collections.emptyList();
        when(paymentService.getOverduePayments(isNull(LocalDate.class), isNull(LocalDate.class))).thenReturn(payments);

        mockMvc.perform(get("/api/v1/payments/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(paymentService).getOverduePayments(isNull(LocalDate.class), isNull(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPaymentStatistics_Success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        when(paymentService.getPaymentStatistics(isNull(LocalDate.class), isNull(LocalDate.class))).thenReturn(stats);

        mockMvc.perform(get("/api/v1/payments/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(paymentService).getPaymentStatistics(isNull(LocalDate.class), isNull(LocalDate.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentPaymentSummary_Success() throws Exception {
        Map<String, Object> summary = new HashMap<>();
        when(paymentService.getStudentPaymentSummary(eq(1L), isNull(String.class), isNull(String.class))).thenReturn(summary);

        mockMvc.perform(get("/api/v1/payments/student/1/summary"))
                .andExpect(status().isOk());

        verify(paymentService).getStudentPaymentSummary(eq(1L), isNull(String.class), isNull(String.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void sendPaymentReminders_Success() throws Exception {
        doNothing().when(paymentService).sendPaymentReminders();

        mockMvc.perform(post("/api/v1/payments/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment reminders sent successfully"));

        verify(paymentService).sendPaymentReminders();
    }
}