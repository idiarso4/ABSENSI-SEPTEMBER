package com.simsekolah.repository;

import com.simsekolah.entity.CounselingAgreement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CounselingAgreementRepository extends JpaRepository<CounselingAgreement, Long> {

    List<CounselingAgreement> findByCounselingCaseId(Long counselingCaseId);

    List<CounselingAgreement> findByCounselingCaseIdOrderByEffectiveDateDesc(Long counselingCaseId);

    List<CounselingAgreement> findByCounselorId(Long counselorId);

    List<CounselingAgreement> findByEffectiveDate(LocalDate effectiveDate);

    List<CounselingAgreement> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);

    List<CounselingAgreement> findByAgreementStatus(CounselingAgreement.AgreementStatus agreementStatus);

    List<CounselingAgreement> findByAgreementType(CounselingAgreement.AgreementType agreementType);

    List<CounselingAgreement> findByComplianceStatus(CounselingAgreement.ComplianceStatus complianceStatus);

    Optional<CounselingAgreement> findByAgreementNumber(String agreementNumber);

    @Query("SELECT a FROM CounselingAgreement a WHERE a.counselingCase.id = :caseId AND a.effectiveDate BETWEEN :startDate AND :endDate ORDER BY a.effectiveDate DESC")
    List<CounselingAgreement> findByCaseIdAndDateRange(@Param("caseId") Long caseId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM CounselingAgreement a WHERE a.counselor.id = :counselorId AND a.effectiveDate BETWEEN :startDate AND :endDate")
    List<CounselingAgreement> findByCounselorIdAndDateRange(@Param("counselorId") Long counselorId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM CounselingAgreement a WHERE a.counselingCase.id = :caseId")
    long countByCaseId(@Param("caseId") Long caseId);

    @Query("SELECT COUNT(a) FROM CounselingAgreement a WHERE a.counselor.id = :counselorId")
    long countByCounselorId(@Param("counselorId") Long counselorId);

    @Query("SELECT a FROM CounselingAgreement a WHERE a.agreementStatus = 'PENDING_SIGNATURES'")
    List<CounselingAgreement> findPendingSignatures();

    @Query("SELECT a FROM CounselingAgreement a WHERE a.agreementStatus = 'ACTIVE'")
    List<CounselingAgreement> findActiveAgreements();

    @Query("SELECT a FROM CounselingAgreement a WHERE a.agreementStatus = 'TERMINATED' OR a.agreementStatus = 'BREACHED'")
    List<CounselingAgreement> findTerminatedAgreements();

    @Query("SELECT a FROM CounselingAgreement a WHERE a.expirationDate <= :currentDate AND a.agreementStatus = 'ACTIVE'")
    List<CounselingAgreement> findExpiringAgreements(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT a FROM CounselingAgreement a WHERE a.complianceStatus = 'NON_COMPLIANT'")
    List<CounselingAgreement> findNonCompliantAgreements();

    @Query("SELECT a FROM CounselingAgreement a WHERE a.reviewDate <= :currentDate AND a.agreementStatus = 'ACTIVE'")
    List<CounselingAgreement> findAgreementsDueForReview(@Param("currentDate") LocalDate currentDate);

    // Pagination support
    Page<CounselingAgreement> findByCounselingCaseId(Long counselingCaseId, Pageable pageable);

    Page<CounselingAgreement> findByCounselorId(Long counselorId, Pageable pageable);

    Page<CounselingAgreement> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<CounselingAgreement> findByAgreementStatus(CounselingAgreement.AgreementStatus agreementStatus, Pageable pageable);

    Page<CounselingAgreement> findByAgreementType(CounselingAgreement.AgreementType agreementType, Pageable pageable);

    @Query("SELECT a FROM CounselingAgreement a WHERE " +
           "(:caseId IS NULL OR a.counselingCase.id = :caseId) AND " +
           "(:counselorId IS NULL OR a.counselor.id = :counselorId) AND " +
           "(:startDate IS NULL OR a.effectiveDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.effectiveDate <= :endDate) AND " +
           "(:agreementType IS NULL OR a.agreementType = :agreementType) AND " +
           "(:agreementStatus IS NULL OR a.agreementStatus = :agreementStatus) AND " +
           "(:complianceStatus IS NULL OR a.complianceStatus = :complianceStatus)")
    Page<CounselingAgreement> advancedSearch(@Param("caseId") Long caseId,
                                            @Param("counselorId") Long counselorId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("agreementType") CounselingAgreement.AgreementType agreementType,
                                            @Param("agreementStatus") CounselingAgreement.AgreementStatus agreementStatus,
                                            @Param("complianceStatus") CounselingAgreement.ComplianceStatus complianceStatus,
                                            Pageable pageable);

    @Query("SELECT a.agreementStatus as status, COUNT(a) as count FROM CounselingAgreement a GROUP BY a.agreementStatus")
    List<Object[]> getAgreementStatisticsByStatus();

    @Query("SELECT a.agreementType as type, COUNT(a) as count FROM CounselingAgreement a GROUP BY a.agreementType")
    List<Object[]> getAgreementStatisticsByType();

    @Query("SELECT a.complianceStatus as status, COUNT(a) as count FROM CounselingAgreement a GROUP BY a.complianceStatus")
    List<Object[]> getComplianceStatistics();

    @Query("SELECT DATE_FORMAT(a.effectiveDate, '%Y-%m') as month, COUNT(a) as count FROM CounselingAgreement a GROUP BY DATE_FORMAT(a.effectiveDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyAgreementStatistics();

    @Query("SELECT a FROM CounselingAgreement a WHERE a.agreementStatus = 'ACTIVE' ORDER BY a.effectiveDate DESC")
    List<CounselingAgreement> findActiveAgreementsOrderByDateDesc(Pageable pageable);

    @Query("SELECT a FROM CounselingAgreement a WHERE a.counselingCase.student.id = :studentId ORDER BY a.effectiveDate DESC")
    List<CounselingAgreement> findByStudentId(@Param("studentId") Long studentId);
}