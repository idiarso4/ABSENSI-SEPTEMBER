package com.simsekolah.service;

import com.simsekolah.entity.Payment;
import com.simsekolah.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment updatePayment(Long id, Payment paymentDetails);
    Optional<Payment> getPaymentById(Long id);
    Page<Payment> getAllPayments(Pageable pageable);
    Page<Payment> getPaymentsByStudent(Long studentId, Pageable pageable);
    List<Payment> getOverduePayments(LocalDate startDate, LocalDate endDate);
    Map<String, Object> getPaymentStatistics(LocalDate startDate, LocalDate endDate);
    Payment recordPayment(Long studentId, String paymentType, BigDecimal amount, LocalDate paymentDate, PaymentStatus status);
    void deletePayment(Long id);
    boolean existsByTransactionId(String transactionId);
    BigDecimal getTotalAmountByStudentAndStatus(Long studentId, PaymentStatus status);
    List<Payment> getPaymentsBySemesterAndAcademicYear(String semester, String academicYear);
    List<Payment> getOverduePaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    void sendPaymentReminders();
    Map<String, Object> getStudentPaymentSummary(Long studentId, String academicYear, String semester);
}
