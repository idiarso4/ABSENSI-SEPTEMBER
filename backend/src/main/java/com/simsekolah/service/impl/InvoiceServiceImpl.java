package com.simsekolah.service.impl;

import com.simsekolah.entity.Invoice;
import com.simsekolah.entity.Student;
import com.simsekolah.repository.InvoiceRepository;
import com.simsekolah.repository.StudentRepository;
import com.simsekolah.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;

    @Override
    public Invoice createInvoice(Long studentId, BigDecimal amount, LocalDate dueDate, String description) {
        log.info("Creating invoice for student ID: {} with amount: {}", studentId, amount);
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        String invoiceNumber = generateInvoiceNumber();
        
        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .student(student)
                .amount(amount)
                .description(description)
                .dueDate(dueDate.atStartOfDay())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully created invoice with ID: {} and number: {}", savedInvoice.getId(), invoiceNumber);
        
        return savedInvoice;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> getAllInvoices(Pageable pageable) {
        log.debug("Fetching all invoices with pagination");
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceById(Long invoiceId) {
        log.debug("Fetching invoice by ID: {}", invoiceId);
        return invoiceRepository.findById(invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceByNumber(String invoiceNumber) {
        log.debug("Fetching invoice by number: {}", invoiceNumber);
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStudent(Long studentId) {
        log.debug("Fetching invoices for student ID: {}", studentId);
        return invoiceRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> getInvoicesByStudent(Long studentId, Pageable pageable) {
        log.debug("Fetching invoices for student ID: {} with pagination", studentId);
        return invoiceRepository.findByStudentId(studentId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStatus(String status) {
        log.debug("Fetching invoices by status: {}", status);
        return invoiceRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByPaymentStatus(String paymentStatus) {
        log.debug("Fetching invoices by payment status: {}", paymentStatus);
        return invoiceRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getOverdueInvoices() {
        log.debug("Fetching overdue invoices");
        LocalDate currentDate = LocalDate.now();
        return invoiceRepository.findOverdueInvoices(currentDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getUpcomingDueInvoices(int daysAhead) {
        log.debug("Fetching upcoming due invoices for {} days ahead", daysAhead);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);
        return invoiceRepository.findUpcomingDueInvoices(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching invoices by date range: {} to {}", startDate, endDate);
        return invoiceRepository.findByDueDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.debug("Fetching invoices by amount range: {} to {}", minAmount, maxAmount);
        return invoiceRepository.findByAmountBetween(minAmount, maxAmount);
    }

    @Override
    public Invoice updateInvoiceStatus(Long invoiceId, String status) {
        log.info("Updating invoice status for ID: {} to {}", invoiceId, status);
        
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));
        
        invoice.setStatus(status);
        invoice.setUpdatedAt(LocalDateTime.now());
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully updated invoice status for ID: {}", invoiceId);
        
        return updatedInvoice;
    }

    @Override
    public Invoice updatePaymentStatus(Long invoiceId, String paymentStatus, String paymentMethod) {
        log.info("Updating payment status for invoice ID: {} to {} with method: {}", invoiceId, paymentStatus, paymentMethod);
        
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));
        
        invoice.setStatus(paymentStatus);
        if (paymentMethod != null) {
            invoice.setPaymentMethod(paymentMethod);
        }
        invoice.setUpdatedAt(LocalDateTime.now());
        
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully updated payment status for invoice ID: {}", invoiceId);
        
        return updatedInvoice;
    }

    @Override
    public Invoice markAsPaid(Long invoiceId, String paymentMethod) {
        log.info("Marking invoice ID: {} as paid with method: {}", invoiceId, paymentMethod);
        return updatePaymentStatus(invoiceId, "PAID", paymentMethod);
    }

    @Override
    public Invoice markAsUnpaid(Long invoiceId) {
        log.info("Marking invoice ID: {} as unpaid", invoiceId);
        return updatePaymentStatus(invoiceId, "UNPAID", null);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        log.info("Deleting invoice with ID: {}", invoiceId);
        
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new IllegalArgumentException("Invoice not found with ID: " + invoiceId);
        }
        
        invoiceRepository.deleteById(invoiceId);
        log.info("Successfully deleted invoice with ID: {}", invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> advancedSearch(Long studentId, String status, String paymentStatus,
                                       LocalDate startDate, LocalDate endDate, BigDecimal minAmount,
                                       BigDecimal maxAmount, Pageable pageable) {
        log.debug("Advanced search for invoices with filters");
        
        return invoiceRepository.advancedSearch(studentId, status, paymentStatus, 
                startDate, endDate, minAmount, maxAmount, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInvoiceStatistics() {
        log.debug("Fetching invoice statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        // Total invoices
        long totalInvoices = invoiceRepository.count();
        statistics.put("totalInvoices", totalInvoices);
        
        // Payment status statistics
        List<Object[]> paymentStatusStats = invoiceRepository.getPaymentStatusStatistics();
        Map<String, Long> paymentStatusStatistics = new HashMap<>();
        for (Object[] row : paymentStatusStats) {
            paymentStatusStatistics.put((String) row[0], (Long) row[1]);
        }
        statistics.put("paymentStatusStatistics", paymentStatusStatistics);
        
        // Invoice status statistics
        List<Object[]> invoiceStatusStats = invoiceRepository.getInvoiceStatusStatistics();
        Map<String, Long> invoiceStatusStatistics = new HashMap<>();
        for (Object[] row : invoiceStatusStats) {
            invoiceStatusStatistics.put((String) row[0], (Long) row[1]);
        }
        statistics.put("invoiceStatusStatistics", invoiceStatusStatistics);
        
        // Financial statistics
        BigDecimal totalPaidAmount = invoiceRepository.getTotalPaidAmount();
        BigDecimal totalUnpaidAmount = invoiceRepository.getTotalUnpaidAmount();
        BigDecimal totalOverdueAmount = invoiceRepository.getTotalOverdueAmount(LocalDate.now());
        
        statistics.put("totalPaidAmount", totalPaidAmount != null ? totalPaidAmount : BigDecimal.ZERO);
        statistics.put("totalUnpaidAmount", totalUnpaidAmount != null ? totalUnpaidAmount : BigDecimal.ZERO);
        statistics.put("totalOverdueAmount", totalOverdueAmount != null ? totalOverdueAmount : BigDecimal.ZERO);
        
        // Monthly statistics
        List<Object[]> monthlyStats = invoiceRepository.getMonthlyInvoiceStatistics();
        List<Map<String, Object>> monthlyStatistics = new ArrayList<>();
        for (Object[] row : monthlyStats) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", row[0]);
            monthData.put("count", row[1]);
            monthData.put("totalAmount", row[2]);
            monthlyStatistics.add(monthData);
        }
        statistics.put("monthlyStatistics", monthlyStatistics);
        
        // Payment method statistics
        List<Object[]> paymentMethodStats = invoiceRepository.getPaymentMethodStatistics();
        List<Map<String, Object>> paymentMethodStatistics = new ArrayList<>();
        for (Object[] row : paymentMethodStats) {
            Map<String, Object> methodData = new HashMap<>();
            methodData.put("method", row[0]);
            methodData.put("count", row[1]);
            methodData.put("totalAmount", row[2]);
            paymentMethodStatistics.add(methodData);
        }
        statistics.put("paymentMethodStatistics", paymentMethodStatistics);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentInvoiceStatistics(Long studentId) {
        log.debug("Fetching invoice statistics for student ID: {}", studentId);
        Map<String, Object> statistics = new HashMap<>();
        
        // Total invoices for student
        long totalInvoices = invoiceRepository.countByStudentId(studentId);
        statistics.put("totalInvoices", totalInvoices);
        
        // Financial statistics for student
        BigDecimal totalPaidAmount = invoiceRepository.getTotalPaidAmountByStudent(studentId);
        BigDecimal totalUnpaidAmount = invoiceRepository.getTotalUnpaidAmountByStudent(studentId);
        
        statistics.put("totalPaidAmount", totalPaidAmount != null ? totalPaidAmount : BigDecimal.ZERO);
        statistics.put("totalUnpaidAmount", totalUnpaidAmount != null ? totalUnpaidAmount : BigDecimal.ZERO);
        
        // Get invoices by status for this student
        List<Invoice> studentInvoices = invoiceRepository.findByStudentId(studentId);
        Map<String, Long> statusCount = studentInvoices.stream()
                .collect(Collectors.groupingBy(invoice -> invoice.getPaymentStatus() != null ? invoice.getPaymentStatus().name() : "UNKNOWN", Collectors.counting()));
        statistics.put("paymentStatusCount", statusCount);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> searchInvoices(String searchTerm) {
        log.debug("Searching invoices with term: {}", searchTerm);
        return invoiceRepository.searchInvoices(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctPaymentMethods() {
        log.debug("Fetching distinct payment methods");
        return invoiceRepository.findDistinctPaymentMethods();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctStatuses() {
        log.debug("Fetching distinct invoice statuses");
        return invoiceRepository.findDistinctStatuses();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctPaymentStatuses() {
        log.debug("Fetching distinct payment statuses");
        return invoiceRepository.findDistinctPaymentStatuses();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getPendingInvoices() {
        log.debug("Fetching pending invoices");
        return invoiceRepository.findByPaymentStatus("UNPAID");
    }

    @Override
    public Invoice updateInvoice(Long invoiceId, Invoice invoiceDetails) {
        log.info("Updating invoice with ID: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        if (invoiceDetails.getAmount() != null) {
            invoice.setAmount(invoiceDetails.getAmount());
        }
        if (invoiceDetails.getDueDate() != null) {
            invoice.setDueDate(invoiceDetails.getDueDate());
        }
        if (invoiceDetails.getDescription() != null) {
            invoice.setDescription(invoiceDetails.getDescription());
        }
        if (invoiceDetails.getStatus() != null) {
            invoice.setStatus(invoiceDetails.getStatus());
        }
        if (invoiceDetails.getPaymentStatus() != null) {
            invoice.setPaymentStatus(invoiceDetails.getPaymentStatus());
        }
        if (invoiceDetails.getPaymentMethod() != null) {
            invoice.setPaymentMethod(invoiceDetails.getPaymentMethod());
        }

        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully updated invoice with ID: {}", invoiceId);

        return updatedInvoice;
    }

    @Override
    public Invoice sendInvoice(Long invoiceId) {
        log.info("Marking invoice as sent: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        invoice.setStatus("SENT");
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully marked invoice as sent: {}", invoiceId);

        return updatedInvoice;
    }

    @Override
    public Invoice recordPayment(Long invoiceId, BigDecimal amount, String paymentMethod, String transactionId) {
        log.info("Recording payment for invoice: {} - Amount: {}", invoiceId, amount);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        invoice.setStatus("PAID");
        if (paymentMethod != null) {
            invoice.setPaymentMethod(paymentMethod);
        }
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully recorded payment for invoice: {}", invoiceId);

        return updatedInvoice;
    }

    @Override
    public Invoice cancelInvoice(Long invoiceId) {
        log.info("Cancelling invoice: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        invoice.setStatus("CANCELLED");
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Successfully cancelled invoice: {}", invoiceId);

        return updatedInvoice;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInvoiceStatistics(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching invoice statistics for date range: {} to {}", startDate, endDate);

        Map<String, Object> statistics = getInvoiceStatistics(); // Get base statistics

        // Add date range specific statistics
        if (startDate != null && endDate != null) {
            List<Invoice> dateRangeInvoices = invoiceRepository.findByDueDateBetween(startDate, endDate);
            statistics.put("dateRangeInvoiceCount", dateRangeInvoices.size());

            BigDecimal dateRangeTotal = dateRangeInvoices.stream()
                    .map(Invoice::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            statistics.put("dateRangeTotalAmount", dateRangeTotal);
        }

        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(Long invoiceId) {
        log.info("Generating PDF for invoice: {}", invoiceId);

        invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        // Placeholder implementation - return empty byte array
        // In a real implementation, you would use a PDF library like iText or Apache PDFBox
        log.warn("PDF generation not implemented yet for invoice: {}", invoiceId);
        return new byte[0];
    }

    @Override
    public void sendInvoiceEmail(Long invoiceId, String email) {
        log.info("Sending invoice {} via email to: {}", invoiceId, email);

        invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));

        // Placeholder implementation
        // In a real implementation, you would use an email service
        log.warn("Email sending not implemented yet for invoice: {} to email: {}", invoiceId, email);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentInvoiceSummary(Long studentId, String academicYear, Integer semester) {
        log.debug("Fetching invoice summary for student: {} - Year: {} - Semester: {}", studentId, academicYear, semester);

        Map<String, Object> summary = new HashMap<>();

        // Get student's invoices
        List<Invoice> studentInvoices = invoiceRepository.findByStudentId(studentId);

        // Filter by academic year and semester if provided
        if (academicYear != null && semester != null) {
            // Placeholder filtering logic - would need actual academic year/semester fields
            log.debug("Filtering by academic year and semester not implemented yet");
        }

        summary.put("studentId", studentId);
        summary.put("totalInvoices", studentInvoices.size());

        BigDecimal totalAmount = studentInvoices.stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.put("totalAmount", totalAmount);

        long paidCount = studentInvoices.stream()
                .filter(invoice -> "PAID".equals(invoice.getStatus()))
                .count();
        summary.put("paidInvoices", paidCount);

        long unpaidCount = studentInvoices.stream()
                .filter(invoice -> "UNPAID".equals(invoice.getStatus()))
                .count();
        summary.put("unpaidInvoices", unpaidCount);

        return summary;
    }

    @Override
    public Map<String, Object> bulkSendInvoices(List<Long> invoiceIds) {
        log.info("Bulk sending {} invoices", invoiceIds.size());

        Map<String, Object> result = new HashMap<>();
        List<Long> successful = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        for (Long invoiceId : invoiceIds) {
            try {
                sendInvoice(invoiceId);
                successful.add(invoiceId);
            } catch (Exception e) {
                log.error("Failed to send invoice: {}", invoiceId, e);
                failed.add(invoiceId);
            }
        }

        result.put("totalRequested", invoiceIds.size());
        result.put("successful", successful);
        result.put("failed", failed);
        result.put("successCount", successful.size());
        result.put("failureCount", failed.size());

        log.info("Bulk send completed: {} successful, {} failed", successful.size(), failed.size());

        return result;
    }

    @Override
    public Map<String, Object> bulkGenerateInvoices(List<Long> studentIds, String academicYear, Integer semester, LocalDate dueDate) {
        log.info("Bulk generating invoices for {} students", studentIds.size());

        Map<String, Object> result = new HashMap<>();
        List<Long> successful = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        BigDecimal defaultAmount = new BigDecimal("1000000"); // Default amount - should be configurable

        for (Long studentId : studentIds) {
            try {
                createInvoice(studentId, defaultAmount, dueDate, "Bulk generated invoice for " + academicYear + " semester " + semester);
                successful.add(studentId);
            } catch (Exception e) {
                log.error("Failed to generate invoice for student: {}", studentId, e);
                failed.add(studentId);
            }
        }

        result.put("totalRequested", studentIds.size());
        result.put("successful", successful);
        result.put("failed", failed);
        result.put("successCount", successful.size());
        result.put("failureCount", failed.size());

        log.info("Bulk generation completed: {} successful, {} failed", successful.size(), failed.size());

        return result;
    }

    private String generateInvoiceNumber() {
        // Generate invoice number with format: INV-YYYYMMDD-XXXX
        String datePrefix = LocalDate.now().toString().replace("-", "");
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%s-%04d", datePrefix, count);
    }
}