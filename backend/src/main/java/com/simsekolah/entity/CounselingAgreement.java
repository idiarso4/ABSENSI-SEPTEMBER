package com.simsekolah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "counseling_agreements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_case_id", nullable = false)
    private CounselingCase counselingCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(name = "agreement_number", unique = true)
    private String agreementNumber;

    @Column(name = "agreement_title", nullable = false)
    private String agreementTitle;

    @Column(name = "agreement_content", columnDefinition = "TEXT", nullable = false)
    private String agreementContent;

    @Column(name = "agreement_type")
    @Enumerated(EnumType.STRING)
    private AgreementType agreementType;

    @Column(name = "student_commitments", columnDefinition = "TEXT")
    private String studentCommitments;

    @Column(name = "counselor_commitments", columnDefinition = "TEXT")
    private String counselorCommitments;

    @Column(name = "parent_commitments", columnDefinition = "TEXT")
    private String parentCommitments;

    @Column(name = "school_commitments", columnDefinition = "TEXT")
    private String schoolCommitments;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "student_signature")
    private Boolean studentSignature = false;

    @Column(name = "student_signature_date")
    private LocalDateTime studentSignatureDate;

    @Column(name = "parent_signature")
    private Boolean parentSignature = false;

    @Column(name = "parent_signature_date")
    private LocalDateTime parentSignatureDate;

    @Column(name = "counselor_signature")
    private Boolean counselorSignature = false;

    @Column(name = "counselor_signature_date")
    private LocalDateTime counselorSignatureDate;

    @Column(name = "witness_signature")
    private Boolean witnessSignature = false;

    @Column(name = "witness_signature_date")
    private LocalDateTime witnessSignatureDate;

    @Column(name = "witness_name")
    private String witnessName;

    @Column(name = "agreement_status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus = AgreementStatus.DRAFT;

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus = ComplianceStatus.PENDING;

    @Column(name = "termination_reason", columnDefinition = "TEXT")
    private String terminationReason;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "follow_up_notes", columnDefinition = "TEXT")
    private String followUpNotes;

    @Column(name = "attachments")
    private String attachments; // JSON array of file URLs

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (agreementNumber == null) {
            agreementNumber = "AGR-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AgreementType {
        BEHAVIORAL_CONTRACT, ACADEMIC_IMPROVEMENT, ATTENDANCE_COMMITMENT,
        DISCIPLINARY_AGREEMENT, COUNSELING_CONTRACT, OTHER
    }

    public enum AgreementStatus {
        DRAFT, PENDING_SIGNATURES, ACTIVE, TERMINATED, EXPIRED, BREACHED
    }

    public enum ComplianceStatus {
        PENDING, COMPLIANT, NON_COMPLIANT, UNDER_REVIEW
    }
}