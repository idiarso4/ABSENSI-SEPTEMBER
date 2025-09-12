package com.simsekolah.repository;

import com.simsekolah.entity.Invoice;
import com.simsekolah.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStudent(Student student);
    
    List<Invoice> findByStudentId(Long studentId);
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByStatus(String status);
    
    List<Invoice> findByPaymentStatus(String paymentStatus);
    
    List<Invoice> findByDueDate(LocalDate dueDate);
    
    List<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Invoice> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT i FROM Invoice i WHERE i.student.id = :studentId AND i.status = :status")
    List<Invoice> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") String status);
    
    @Query("SELECT i FROM Invoice i WHERE i.student.id = :studentId AND i.paymentStatus = :paymentStatus")
    List<Invoice> findByStudentIdAndPaymentStatus(@Param("studentId") Long studentId, @Param("paymentStatus") String paymentStatus);
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.paymentStatus != 'PAID'")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :startDate AND :endDate AND i.paymentStatus != 'PAID'")
    List<Invoice> findUpcomingDueInvoices(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.amount >= :minAmount AND i.amount <= :maxAmount")
    List<Invoice> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    // Pagination support
    Page<Invoice> findByStudentId(Long studentId, Pageable pageable);
    
    Page<Invoice> findByStatus(String status, Pageable pageable);
    
    Page<Invoice> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    Page<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    @Query("SELECT i FROM Invoice i WHERE " +
           "(:studentId IS NULL OR i.student.id = :studentId) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:paymentStatus IS NULL OR i.paymentStatus = :paymentStatus) AND " +
           "(:startDate IS NULL OR i.dueDate >= :startDate) AND " +
           "(:endDate IS NULL OR i.dueDate <= :endDate) AND " +
           "(:minAmount IS NULL OR i.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR i.amount <= :maxAmount)")
    Page<Invoice> advancedSearch(@Param("studentId") Long studentId,
                                @Param("status") String status,
                                @Param("paymentStatus") String paymentStatus,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("minAmount") BigDecimal minAmount,
                                @Param("maxAmount") BigDecimal maxAmount,
                                Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") String paymentStatus);
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    long countByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.paymentStatus = 'PAID'")
    BigDecimal getTotalPaidAmount();
    
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.paymentStatus != 'PAID'")
    BigDecimal getTotalUnpaidAmount();
    
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.dueDate < :currentDate AND i.paymentStatus != 'PAID'")
    BigDecimal getTotalOverdueAmount(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT i.paymentStatus as status, COUNT(i) as count FROM Invoice i GROUP BY i.paymentStatus")
    List<Object[]> getPaymentStatusStatistics();
    
    @Query("SELECT i.status as status, COUNT(i) as count FROM Invoice i GROUP BY i.status")
    List<Object[]> getInvoiceStatusStatistics();
    
    @Query("SELECT DATE_FORMAT(i.createdAt, '%Y-%m') as month, COUNT(i) as count, SUM(i.amount) as totalAmount " +
           "FROM Invoice i GROUP BY DATE_FORMAT(i.createdAt, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyInvoiceStatistics();
    
    @Query("SELECT i.paymentMethod as method, COUNT(i) as count, SUM(i.amount) as totalAmount " +
           "FROM Invoice i WHERE i.paymentStatus = 'PAID' GROUP BY i.paymentMethod")
    List<Object[]> getPaymentMethodStatistics();
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.student.id = :studentId AND i.paymentStatus = 'PAID'")
    BigDecimal getTotalPaidAmountByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.student.id = :studentId AND i.paymentStatus != 'PAID'")
    BigDecimal getTotalUnpaidAmountByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceNumber LIKE %:searchTerm% OR " +
           "i.student.name LIKE %:searchTerm%")
    List<Invoice> searchInvoices(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate = :date")
    List<Invoice> findInvoicesDueOnDate(@Param("date") LocalDate date);
    
    @Query("SELECT DISTINCT i.paymentMethod FROM Invoice i WHERE i.paymentMethod IS NOT NULL")
    List<String> findDistinctPaymentMethods();
    
    @Query("SELECT DISTINCT i.status FROM Invoice i WHERE i.status IS NOT NULL")
    List<String> findDistinctStatuses();
    
    @Query("SELECT DISTINCT i.paymentStatus FROM Invoice i WHERE i.paymentStatus IS NOT NULL")
    List<String> findDistinctPaymentStatuses();
}