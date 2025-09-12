package com.simsekolah.controller;

import com.simsekolah.entity.Payment;
import com.simsekolah.service.PaymentService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Payment Management", description = "Payment management endpoints")
@RestController
@RequestMapping("/api/v1/payments")
@Validated
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create payment", description = "Create a new payment record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Transaction ID already exists")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        logger.info("Creating new payment for student: {}", payment.getStudent().getId());
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            logger.info("Successfully created payment with ID: {}", createdPayment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
        } catch (Exception e) {
            logger.error("Failed to create payment for student: {}", payment.getStudent().getId(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieve all payment records with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Page<Payment>> getAllPayments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.debug("Fetching all payments - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Payment> payments = paymentService.getAllPayments(pageable);
        logger.debug("Retrieved {} payments", payments.getTotalElements());
        
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve payment information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") @NotNull Long paymentId) {
        logger.debug("Fetching payment by ID: {}", paymentId);
        
        Optional<Payment> payment = paymentService.getPaymentById(paymentId);
        if (payment.isPresent()) {
            logger.debug("Payment found with ID: {}", paymentId);
            return ResponseEntity.ok(payment.get());
        } else {
            logger.debug("Payment not found with ID: {}", paymentId);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment", description = "Update payment information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable("id") @NotNull Long paymentId,
            @Valid @RequestBody Payment paymentDetails) {
        
        logger.info("Updating payment with ID: {}", paymentId);
        
        try {
            Payment updatedPayment = paymentService.updatePayment(paymentId, paymentDetails);
            logger.info("Successfully updated payment with ID: {}", paymentId);
            return ResponseEntity.ok(updatedPayment);
        } catch (Exception e) {
            logger.error("Failed to update payment with ID: {}", paymentId, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment", description = "Delete payment record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Payment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") @NotNull Long paymentId) {
        logger.info("Deleting payment with ID: {}", paymentId);
        
        try {
            paymentService.deletePayment(paymentId);
            logger.info("Successfully deleted payment with ID: {}", paymentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete payment with ID: {}", paymentId, e);
            throw e;
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get payments by student", description = "Retrieve all payments for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Page<Payment>> getPaymentsByStudent(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        logger.debug("Fetching payments for student: {}", studentId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentDate"));
        Page<Payment> payments = paymentService.getPaymentsByStudent(studentId, pageable);
        
        logger.debug("Retrieved {} payments for student: {}", payments.getTotalElements(), studentId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue payments", description = "Retrieve all overdue payments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Overdue payments retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<List<Payment>> getOverduePayments(
            @Parameter(description = "Start date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        logger.debug("Fetching overdue payments");
        
        List<Payment> overduePayments = paymentService.getOverduePayments(startDate, endDate);
        
        logger.debug("Retrieved {} overdue payments", overduePayments.size());
        return ResponseEntity.ok(overduePayments);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get payment statistics", description = "Retrieve payment statistics and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> getPaymentStatistics(
            @Parameter(description = "Start date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        logger.debug("Fetching payment statistics");
        
        try {
            Map<String, Object> statistics = paymentService.getPaymentStatistics(startDate, endDate);
            statistics.put("timestamp", System.currentTimeMillis());
            
            logger.debug("Retrieved payment statistics");
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Failed to get payment statistics", e);
            throw e;
        }
    }

    @GetMapping("/student/{studentId}/summary")
    @Operation(summary = "Get student payment summary", description = "Get payment summary for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE') or hasRole('PARENT')")
    public ResponseEntity<Map<String, Object>> getStudentPaymentSummary(
            @PathVariable("studentId") @NotNull Long studentId,
            @Parameter(description = "Academic year") @RequestParam(required = false) String academicYear,
            @Parameter(description = "Semester") @RequestParam(required = false) String semester) {
        
        logger.debug("Fetching payment summary for student: {}", studentId);
        
        try {
            Map<String, Object> summary = paymentService.getStudentPaymentSummary(studentId, academicYear, semester);
            
            logger.debug("Retrieved payment summary for student: {}", studentId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Failed to get student payment summary for student: {}", studentId, e);
            throw e;
        }
    }

    @PostMapping("/reminders")
    @Operation(summary = "Send payment reminders", description = "Send payment reminder notifications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reminders sent successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Map<String, Object>> sendPaymentReminders() {
        logger.info("Sending payment reminders");
        
        try {
            paymentService.sendPaymentReminders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payment reminders sent successfully");
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("Successfully sent payment reminders");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to send payment reminders", e);
            throw e;
        }
    }
}