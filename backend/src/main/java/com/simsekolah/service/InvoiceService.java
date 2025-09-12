package com.simsekolah.service;

import com.simsekolah.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InvoiceService {

    Invoice createInvoice(Long studentId, BigDecimal amount, LocalDate dueDate, String description);

    Page<Invoice> getAllInvoices(Pageable pageable);

    Optional<Invoice> getInvoiceById(Long invoiceId);

    Optional<Invoice> getInvoiceByNumber(String invoiceNumber);

    List<Invoice> getInvoicesByStudent(Long studentId);

    Page<Invoice> getInvoicesByStudent(Long studentId, Pageable pageable);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByPaymentStatus(String paymentStatus);

    List<Invoice> getOverdueInvoices();

    List<Invoice> getUpcomingDueInvoices(int daysAhead);

    List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate);

    List<Invoice> getInvoicesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    Invoice updateInvoiceStatus(Long invoiceId, String status);

    Invoice updatePaymentStatus(Long invoiceId, String paymentStatus, String paymentMethod);

    Invoice markAsPaid(Long invoiceId, String paymentMethod);

    Invoice markAsUnpaid(Long invoiceId);

    void deleteInvoice(Long invoiceId);

    Page<Invoice> advancedSearch(Long studentId, String status, String paymentStatus,
                                LocalDate startDate, LocalDate endDate, BigDecimal minAmount,
                                BigDecimal maxAmount, Pageable pageable);

    Map<String, Object> getInvoiceStatistics();

    Map<String, Object> getStudentInvoiceStatistics(Long studentId);

    List<Invoice> searchInvoices(String searchTerm);

    List<String> getDistinctPaymentMethods();

    List<String> getDistinctStatuses();

    List<String> getDistinctPaymentStatuses();

    // Additional methods used by controller
    List<Invoice> getPendingInvoices();

    Invoice updateInvoice(Long invoiceId, Invoice invoiceDetails);

    Invoice sendInvoice(Long invoiceId);

    Invoice recordPayment(Long invoiceId, BigDecimal amount, String paymentMethod, String transactionId);

    Invoice cancelInvoice(Long invoiceId);

    Map<String, Object> getInvoiceStatistics(LocalDate startDate, LocalDate endDate);

    byte[] generateInvoicePdf(Long invoiceId);

    void sendInvoiceEmail(Long invoiceId, String email);

    Map<String, Object> getStudentInvoiceSummary(Long studentId, String academicYear, Integer semester);

    Map<String, Object> bulkSendInvoices(List<Long> invoiceIds);

    Map<String, Object> bulkGenerateInvoices(List<Long> studentIds, String academicYear, Integer semester, LocalDate dueDate);
}