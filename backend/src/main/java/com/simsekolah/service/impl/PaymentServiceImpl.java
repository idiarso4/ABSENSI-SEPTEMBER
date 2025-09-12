package com.simsekolah.service.impl;

import com.simsekolah.entity.Payment;
import com.simsekolah.entity.Student;
import com.simsekolah.enums.PaymentStatus;
import com.simsekolah.exception.ResourceNotFoundException;
import com.simsekolah.repository.PaymentRepository;
import com.simsekolah.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    // Using model.Student directly; repository validation is omitted for simplicity

    @Override
    public Payment createPayment(Payment payment) {
        // Basic validation can be extended to check student existence if needed

        payment.setStatus(PaymentStatus.PENDING.name());
        return paymentRepository.save(payment);
        }

        @Override
        public Payment updatePayment(Long id, Payment paymentDetails) {
            Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        // Basic validation can be extended to check student existence if needed

        // Update fields
        payment.setPaymentType(paymentDetails.getPaymentType());
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setStatus(paymentDetails.getStatus());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setTransactionId(paymentDetails.getTransactionId());
        payment.setNotes(paymentDetails.getNotes());
        payment.setDueDate(paymentDetails.getDueDate());
        payment.setAcademicYear(paymentDetails.getAcademicYear());
        payment.setSemester(paymentDetails.getSemester());

        return paymentRepository.save(payment);
        }

        @Override
        public Optional<Payment> getPaymentById(Long id) {
            return paymentRepository.findById(id);
        }

        @Override
        public Page<Payment> getAllPayments(Pageable pageable) {
            return paymentRepository.findAll(pageable);
        }

        @Override
        public Page<Payment> getPaymentsByStudent(Long studentId, Pageable pageable) {
            return paymentRepository.findByStudentId(studentId, pageable);
        }

        @Override
        public List<Payment> getOverduePayments(LocalDate startDate, LocalDate endDate) {
            return paymentRepository.findOverduePayments(PaymentStatus.PENDING, startDate, endDate);
        }

        @Override
        public Map<String, Object> getPaymentStatistics(LocalDate startDate, LocalDate endDate) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalPayments", paymentRepository.count());
            stats.put("pendingPayments", paymentRepository.findByStatus(PaymentStatus.PENDING).size());
            stats.put("completedPayments", paymentRepository.findByStatus(PaymentStatus.PAID).size());
            if (startDate != null && endDate != null) {
                stats.put("overduePayments", getOverduePayments(startDate, endDate).size());
            }
            stats.put("timestamp", System.currentTimeMillis());
            return stats;
        }

        @Override
        public Payment recordPayment(Long studentId, String paymentType, BigDecimal amount, LocalDate paymentDate, PaymentStatus status) {
            Payment payment = new Payment();
            Student student = new Student();
            student.setId(studentId);
            payment.setStudent(student);
            payment.setPaymentType(paymentType);
            payment.setAmount(amount);
            payment.setPaymentDate(paymentDate);
            payment.setStatus(status.name());

            return paymentRepository.save(payment);
        }

        @Override
        public void deletePayment(Long id) {
            Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

            paymentRepository.delete(payment);
        }

        @Override
        public boolean existsByTransactionId(String transactionId) {
            return paymentRepository.findByTransactionId(transactionId).isPresent();
        }

        @Override
        public BigDecimal getTotalAmountByStudentAndStatus(Long studentId, PaymentStatus status) {
            return paymentRepository.getTotalAmountByStudentAndStatus(studentId, status);
        }

        @Override
        public List<Payment> getPaymentsBySemesterAndAcademicYear(String semester, String academicYear) {
            return paymentRepository.findBySemesterAndAcademicYear(semester, academicYear);
        }

        @Override
        public List<Payment> getOverduePaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
            return paymentRepository.findOverduePaymentsByDateRange(PaymentStatus.PENDING, startDate, endDate);
        }

        @Override
        public void sendPaymentReminders() {
            // Implementation for sending payment reminders
            // This would typically involve email service or notification system
            LocalDate dueDate = LocalDate.now().plusDays(7);
            List<Payment> overduePayments = paymentRepository.findOverduePayments(PaymentStatus.PENDING, LocalDate.now(), dueDate);
            // Send notifications for overdue payments
            // For now, just log
            logger.info("Sending payment reminders for {} overdue payments", overduePayments.size());
        }

        @Override
        public Map<String, Object> getStudentPaymentSummary(Long studentId, String academicYear, String semester) {
            Map<String, Object> summary = new HashMap<>();
            BigDecimal totalPaid = paymentRepository.getTotalAmountByStudentAndStatus(studentId, PaymentStatus.PAID);
            BigDecimal totalDue = paymentRepository.getTotalAmountByStudentAndStatus(studentId, PaymentStatus.PENDING);
            summary.put("studentId", studentId);
            summary.put("academicYear", academicYear);
            summary.put("semester", semester);
            summary.put("totalPaid", totalPaid);
            summary.put("totalDue", totalDue);
            summary.put("balance", totalDue.subtract(totalPaid));
            return summary;
        }
}
