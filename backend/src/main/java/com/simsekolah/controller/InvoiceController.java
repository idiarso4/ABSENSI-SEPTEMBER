package com.simsekolah.controller;

import com.simsekolah.entity.Invoice;
import com.simsekolah.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for invoice management operations
 * Provides endpoints for invoice management (invoices)
 */
@RestController
@RequestMapping({"/api/v1/invoices", "/api/invoices"})
@Tag(name = "Invoice Management", description = "Invoice management endpoints")
@Validated
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Create a new invoice
     */
    @PostMapping
    @Operation(summary = "Create invoice", description = "Create a new invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        logger.info("Creating new invoice for student: {}", invoice.getStudent().getId());

        try {
            Invoice createdInvoice = invoiceService.createInvoice(
                invoice.getStudent().getId(),
                invoice.getAmount(),
                invoice.getDueDate().toLocalDate(),
                invoice.getDescription()
            );
            logger.info("Successfully created invoice with ID: {}", createdInvoice.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
        } catch (Exception e) {
            logger.error("Failed to create invoice for student: {}", invoice.getStudent().getId(), e);
            throw e;
        }
    }

    /**
     * Get all invoices with pagination
     */
    @GetMapping
    @Operation(summary = "Get all invoices", description = "Retrieve all invoices with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Page<Invoice>> getAllInvoices(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "issueDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.debug("Fetching all invoices - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Invoice> invoices = invoiceService.getAllInvoices(pageable);
        logger.debug("Retrieved {} invoices", invoices.getTotalElements());

        return ResponseEntity.ok(invoices);
    }

    /**
     * Get invoice by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID", description = "Retrieve invoice information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable("id") @NotNull Long invoiceId) {
        logger.debug("Fetching invoice by ID: {}", invoiceId);

        Optional<Invoice> invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice.isPresent()) {
            logger.debug("Invoice found with ID: {}", invoiceId);
            return ResponseEntity.ok(invoice.get());
        } else {
            logger.debug("Invoice not found with ID: {}", invoiceId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get invoice by invoice number
     */
    @GetMapping("/number/{invoiceNumber}")
    @Operation(summary = "Get invoice by number", description = "Retrieve invoice by invoice number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Invoice> getInvoiceByNumber(@PathVariable("invoiceNumber") String invoiceNumber) {
        logger.debug("Fetching invoice by number: {}", invoiceNumber);

        Optional<Invoice> invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
        if (invoice.isPresent()) {
            logger.debug("Invoice found with number: {}", invoiceNumber);
            return ResponseEntity.ok(invoice.get());
        } else {
            logger.debug("Invoice not found with number: {}", invoiceNumber);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get invoices by student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get invoices by student", description = "Retrieve all invoices for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Page<Invoice>> getInvoicesByStudent(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {

        logger.debug("Fetching invoices for student: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "issueDate"));
        Page<Invoice> invoices = invoiceService.getInvoicesByStudent(studentId, pageable);

        logger.debug("Retrieved {} invoices for student: {}", invoices.getTotalElements(), studentId);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Get overdue invoices
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue invoices", description = "Retrieve all overdue invoices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Overdue invoices retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<List<Invoice>> getOverdueInvoices() {
        logger.debug("Fetching overdue invoices");

        List<Invoice> overdueInvoices = invoiceService.getOverdueInvoices();
        logger.debug("Retrieved {} overdue invoices", overdueInvoices.size());

        return ResponseEntity.ok(overdueInvoices);
    }

    /**
     * Get pending invoices
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending invoices", description = "Retrieve all pending invoices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending invoices retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<List<Invoice>> getPendingInvoices() {
        logger.debug("Fetching pending invoices");

        List<Invoice> pendingInvoices = invoiceService.getPendingInvoices();
        logger.debug("Retrieved {} pending invoices", pendingInvoices.size());

        return ResponseEntity.ok(pendingInvoices);
    }

    /**
     * Update invoice
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update invoice", description = "Update invoice information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable("id") @NotNull Long invoiceId,
            @Valid @RequestBody Invoice invoiceDetails) {

        logger.info("Updating invoice with ID: {}", invoiceId);

        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, invoiceDetails);
            logger.info("Successfully updated invoice with ID: {}", invoiceId);
            return ResponseEntity.ok(updatedInvoice);
        } catch (Exception e) {
            logger.error("Failed to update invoice with ID: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Delete invoice
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice", description = "Delete invoice record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Invoice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Void> deleteInvoice(@PathVariable("id") @NotNull Long invoiceId) {
        logger.info("Deleting invoice with ID: {}", invoiceId);

        try {
            invoiceService.deleteInvoice(invoiceId);
            logger.info("Successfully deleted invoice with ID: {}", invoiceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete invoice with ID: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Mark invoice as sent
     */
    @PostMapping("/{id}/send")
    @Operation(summary = "Send invoice", description = "Mark invoice as sent")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice sent successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Invoice> sendInvoice(@PathVariable("id") @NotNull Long invoiceId) {
        logger.info("Sending invoice: {}", invoiceId);

        try {
            Invoice sentInvoice = invoiceService.sendInvoice(invoiceId);
            logger.info("Successfully sent invoice: {}", invoiceId);
            return ResponseEntity.ok(sentInvoice);
        } catch (Exception e) {
            logger.error("Failed to send invoice: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Record payment for invoice
     */
    @PostMapping("/{id}/payment")
    @Operation(summary = "Record payment", description = "Record payment for invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment recorded successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid payment data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Invoice> recordPayment(
            @PathVariable("id") @NotNull Long invoiceId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String transactionId) {

        logger.info("Recording payment for invoice: {} - Amount: {}", invoiceId, amount);

        try {
            Invoice updatedInvoice = invoiceService.recordPayment(invoiceId, amount, paymentMethod, transactionId);
            logger.info("Successfully recorded payment for invoice: {}", invoiceId);
            return ResponseEntity.ok(updatedInvoice);
        } catch (Exception e) {
            logger.error("Failed to record payment for invoice: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Cancel invoice
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel invoice", description = "Cancel invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Invoice> cancelInvoice(@PathVariable("id") @NotNull Long invoiceId) {
        logger.info("Cancelling invoice: {}", invoiceId);

        try {
            Invoice cancelledInvoice = invoiceService.cancelInvoice(invoiceId);
            logger.info("Successfully cancelled invoice: {}", invoiceId);
            return ResponseEntity.ok(cancelledInvoice);
        } catch (Exception e) {
            logger.error("Failed to cancel invoice: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Get invoice statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get invoice statistics", description = "Get invoice statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> getInvoiceStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        logger.debug("Fetching invoice statistics");

        try {
            Map<String, Object> statistics = invoiceService.getInvoiceStatistics(startDate, endDate);
            statistics.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved invoice statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get invoice statistics", e);
            throw e;
        }
    }

    /**
     * Generate invoice PDF
     */
    @GetMapping("/{id}/pdf")
    @Operation(summary = "Generate PDF", description = "Generate PDF for invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<byte[]> generateInvoicePdf(@PathVariable("id") @NotNull Long invoiceId) {
        logger.info("Generating PDF for invoice: {}", invoiceId);

        try {
            byte[] pdfData = invoiceService.generateInvoicePdf(invoiceId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + invoiceId + ".pdf");
            headers.setContentLength(pdfData.length);

            logger.info("Successfully generated PDF for invoice: {}", invoiceId);
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
        } catch (Exception e) {
            logger.error("Failed to generate PDF for invoice: {}", invoiceId, e);
            throw e;
        }
    }

    /**
     * Send invoice via email
     */
    @PostMapping("/{id}/email")
    @Operation(summary = "Send via email", description = "Send invoice via email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email sent successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> sendInvoiceEmail(
            @PathVariable("id") @NotNull Long invoiceId,
            @RequestParam(required = false) String email) {

        logger.info("Sending invoice {} via email", invoiceId);

        try {
            invoiceService.sendInvoiceEmail(invoiceId, email);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invoice sent via email successfully");
            response.put("invoiceId", invoiceId);
            response.put("timestamp", System.currentTimeMillis());

            logger.info("Successfully sent invoice {} via email", invoiceId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send invoice {} via email", invoiceId, e);
            throw e;
        }
    }

    /**
     * Get student invoice summary
     */
    @GetMapping("/student/{studentId}/summary")
    @Operation(summary = "Get student invoice summary", description = "Get invoice summary for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Map<String, Object>> getStudentInvoiceSummary(
            @PathVariable("studentId") @NotNull Long studentId,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Integer semester) {

        logger.debug("Fetching invoice summary for student: {}", studentId);

        try {
            Map<String, Object> summary = invoiceService.getStudentInvoiceSummary(studentId, academicYear, semester);
            summary.put("timestamp", System.currentTimeMillis());

            logger.debug("Retrieved invoice summary for student: {}", studentId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Failed to get student invoice summary for student: {}", studentId, e);
            throw e;
        }
    }

    /**
     * Bulk send invoices
     */
    @PostMapping("/bulk-send")
    @Operation(summary = "Bulk send invoices", description = "Send multiple invoices via email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> bulkSendInvoices(@RequestBody List<Long> invoiceIds) {
        logger.info("Bulk sending {} invoices", invoiceIds.size());

        try {
            Map<String, Object> result = invoiceService.bulkSendInvoices(invoiceIds);

            logger.info("Successfully bulk sent invoices");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to bulk send invoices", e);
            throw e;
        }
    }

    /**
     * Generate bulk invoices
     */
    @PostMapping("/bulk-generate")
    @Operation(summary = "Bulk generate invoices", description = "Generate multiple invoices for students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices generated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> bulkGenerateInvoices(
            @RequestBody List<Long> studentIds,
            @RequestParam String academicYear,
            @RequestParam Integer semester,
            @RequestParam LocalDate dueDate) {

        logger.info("Bulk generating invoices for {} students", studentIds.size());

        try {
            Map<String, Object> result = invoiceService.bulkGenerateInvoices(studentIds, academicYear, semester, dueDate);

            logger.info("Successfully bulk generated invoices");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to bulk generate invoices", e);
            throw e;
        }
    }
}