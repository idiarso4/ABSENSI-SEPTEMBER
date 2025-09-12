package com.simsekolah.repository;

import com.simsekolah.entity.Payment;
import com.simsekolah.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId")
    List<Payment> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.paymentType = :paymentType")
    Optional<Payment> findByStudentIdAndPaymentType(@Param("studentId") Long studentId, @Param("paymentType") String paymentType);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId")
    Page<Payment> findByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.dueDate BETWEEN :startDate AND :endDate")
    List<Payment> findOverduePayments(@Param("status") PaymentStatus status,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.paymentType = :paymentType")
    List<Payment> findByStudentAndPaymentType(@Param("studentId") Long studentId, @Param("paymentType") String paymentType);

    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    List<Payment> findByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.semester = :semester AND p.academicYear = :academicYear")
    List<Payment> findBySemesterAndAcademicYear(@Param("semester") String semester, @Param("academicYear") String academicYear);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.student.id = :studentId AND p.status = :status")
    BigDecimal getTotalAmountByStudentAndStatus(@Param("studentId") Long studentId, @Param("status") PaymentStatus status);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status AND p.dueDate <= :dueDate")
    Long countOverduePayments(@Param("status") PaymentStatus status, @Param("dueDate") LocalDate dueDate);

    @Query("SELECT p FROM Payment p WHERE p.transactionId = :transactionId")
    Optional<Payment> findByTransactionId(@Param("transactionId") String transactionId);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.dueDate BETWEEN :startDate AND :endDate")
    List<Payment> findByStudentAndDueDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.dueDate BETWEEN :startDate AND :endDate")
    List<Payment> findOverduePaymentsByDateRange(@Param("status") PaymentStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
