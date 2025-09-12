package com.simsekolah.repository;

import com.simsekolah.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findBySubjectId(Long subjectId);

    List<Test> findBySubjectIdOrderByTestDateDesc(Long subjectId);

    List<Test> findByTeacherId(Long teacherId);

    List<Test> findByTeacherIdOrderByTestDateDesc(Long teacherId);

    List<Test> findByClassRoomId(Long classRoomId);

    List<Test> findByTestType(Test.TestType testType);

    List<Test> findByTestStatus(Test.TestStatus testStatus);

    List<Test> findByTestDate(LocalDate testDate);

    List<Test> findByTestDateBetween(LocalDate startDate, LocalDate endDate);

    List<Test> findByIsPublished(Boolean isPublished);

    List<Test> findByIsPublishedOrderByPublishDateDesc(Boolean isPublished);

    List<Test> findBySubjectIdAndClassRoomId(Long subjectId, Long classRoomId);

    @Query("SELECT t FROM Test t WHERE t.subject.id = :subjectId AND t.isPublished = true ORDER BY t.testDate DESC")
    List<Test> findPublishedTestsBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT t FROM Test t WHERE t.teacher.id = :teacherId AND t.isPublished = true ORDER BY t.testDate DESC")
    List<Test> findPublishedTestsByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT t FROM Test t WHERE t.classRoom.id = :classRoomId AND t.isPublished = true ORDER BY t.testDate DESC")
    List<Test> findPublishedTestsByClassRoom(@Param("classRoomId") Long classRoomId);

    @Query("SELECT COUNT(t) FROM Test t WHERE t.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(t) FROM Test t WHERE t.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(t) FROM Test t WHERE t.classRoom.id = :classRoomId")
    long countByClassRoomId(@Param("classRoomId") Long classRoomId);

    @Query("SELECT COUNT(t) FROM Test t WHERE t.subject.id = :subjectId AND t.isPublished = true")
    long countPublishedBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT t FROM Test t WHERE t.testDate = :currentDate AND t.isPublished = true")
    List<Test> findTestsForToday(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT t FROM Test t WHERE t.testDate < :currentDate AND t.testStatus = 'PUBLISHED'")
    List<Test> findOverdueTests(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT t FROM Test t WHERE t.testDate > :currentDate AND t.isPublished = true ORDER BY t.testDate")
    List<Test> findUpcomingTests(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT AVG(t.averageScore) FROM Test t WHERE t.subject.id = :subjectId AND t.testStatus = 'COMPLETED'")
    Double getAverageScoreBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT AVG(t.completionRate) FROM Test t WHERE t.subject.id = :subjectId AND t.testStatus = 'COMPLETED'")
    Double getAverageCompletionRateBySubject(@Param("subjectId") Long subjectId);

    // Pagination support
    Page<Test> findBySubjectId(Long subjectId, Pageable pageable);

    Page<Test> findByTeacherId(Long teacherId, Pageable pageable);

    Page<Test> findByClassRoomId(Long classRoomId, Pageable pageable);

    Page<Test> findByTestType(Test.TestType testType, Pageable pageable);

    Page<Test> findByTestStatus(Test.TestStatus testStatus, Pageable pageable);

    Page<Test> findByTestDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Test> findByIsPublished(Boolean isPublished, Pageable pageable);

    @Query("SELECT t FROM Test t WHERE " +
           "(:subjectId IS NULL OR t.subject.id = :subjectId) AND " +
           "(:teacherId IS NULL OR t.teacher.id = :teacherId) AND " +
           "(:classRoomId IS NULL OR t.classRoom.id = :classRoomId) AND " +
           "(:testType IS NULL OR t.testType = :testType) AND " +
           "(:testStatus IS NULL OR t.testStatus = :testStatus) AND " +
           "(:isPublished IS NULL OR t.isPublished = :isPublished) AND " +
           "(:startDate IS NULL OR t.testDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.testDate <= :endDate)")
    Page<Test> advancedSearch(@Param("subjectId") Long subjectId,
                             @Param("teacherId") Long teacherId,
                             @Param("classRoomId") Long classRoomId,
                             @Param("testType") Test.TestType testType,
                             @Param("testStatus") Test.TestStatus testStatus,
                             @Param("isPublished") Boolean isPublished,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate,
                             Pageable pageable);

    @Query("SELECT t.testType as type, COUNT(t) as count FROM Test t GROUP BY t.testType")
    List<Object[]> getTestStatisticsByType();

    @Query("SELECT t.testStatus as status, COUNT(t) as count FROM Test t GROUP BY t.testStatus")
    List<Object[]> getTestStatisticsByStatus();

    @Query("SELECT t.isPublished as published, COUNT(t) as count FROM Test t GROUP BY t.isPublished")
    List<Object[]> getTestStatisticsByPublishStatus();

    @Query("SELECT DATE_FORMAT(t.testDate, '%Y-%m') as month, COUNT(t) as count FROM Test t GROUP BY DATE_FORMAT(t.testDate, '%Y-%m') ORDER BY month DESC")
    List<Object[]> getMonthlyTestStatistics();

    @Query("SELECT t FROM Test t WHERE t.isPublished = true ORDER BY t.attemptsCount DESC")
    List<Test> findMostAttemptedTests(Pageable pageable);

    @Query("SELECT t FROM Test t WHERE t.testStatus = 'COMPLETED' ORDER BY t.averageScore DESC")
    List<Test> findHighestScoringTests(Pageable pageable);

    @Query("SELECT t FROM Test t WHERE t.isPublished = true ORDER BY t.publishDate DESC")
    List<Test> findRecentlyPublishedTests(Pageable pageable);

    @Query("SELECT t FROM Test t WHERE t.testTitle LIKE %:keyword% OR t.testDescription LIKE %:keyword%")
    List<Test> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT t FROM Test t WHERE t.subject.id = :subjectId AND t.testType = :testType AND t.isPublished = true")
    List<Test> findTestsBySubjectAndType(@Param("subjectId") Long subjectId,
                                        @Param("testType") Test.TestType testType);

    @Query("SELECT t FROM Test t WHERE t.classRoom.id = :classRoomId AND t.testDate = :testDate AND t.isPublished = true")
    List<Test> findTestsByClassAndDate(@Param("classRoomId") Long classRoomId,
                                      @Param("testDate") LocalDate testDate);
}