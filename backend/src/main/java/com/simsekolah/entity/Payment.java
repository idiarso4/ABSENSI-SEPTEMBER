package com.simsekolah.entity;

import com.simsekolah.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(name = "payment_type", nullable = false)
    private String paymentType; // SPP, ACTIVITY, UNIFORM, BOOKS
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "notes")
    private String notes;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "academic_year")
    private String academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "invoice_id")
    private String invoiceId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Legacy getter for backward compatibility
    public String getStatus() {
        return status != null ? status.name() : "PENDING";
    }

    // Legacy setter for backward compatibility
    public void setStatus(String status) {
        try {
            this.status = PaymentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            this.status = PaymentStatus.PENDING;
        }
    }

    // Helper method to get student ID
    public Long getStudentId() {
        return student != null ? student.getId() : null;
    }

    // Helper method to get semester ID
    public Long getSemesterId() {
        return semester != null ? semester.getId() : null;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}