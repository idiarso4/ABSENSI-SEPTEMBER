package com.simsekolah.repository;

import com.simsekolah.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {

    List<QuestionBank> findBySubjectId(Long subjectId);

    List<QuestionBank> findBySubjectIdOrderByCreatedAtDesc(Long subjectId);

    List<QuestionBank> findByTeacherId(Long teacherId);

    List<QuestionBank> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);

    List<QuestionBank> findByQuestionType(QuestionBank.QuestionType questionType);

    List<QuestionBank> findByDifficultyLevel(QuestionBank.DifficultyLevel difficultyLevel);

    List<QuestionBank> findByIsActive(Boolean isActive);

    List<QuestionBank> findByChapterTopic(String chapterTopic);

    List<QuestionBank> findBySubjectIdAndChapterTopic(Long subjectId, String chapterTopic);

    @Query("SELECT q FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.isActive = true ORDER BY q.difficultyLevel, q.createdAt")
    List<QuestionBank> findActiveQuestionsBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT q FROM QuestionBank q WHERE q.teacher.id = :teacherId AND q.isActive = true ORDER BY q.createdAt DESC")
    List<QuestionBank> findActiveQuestionsByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(q) FROM QuestionBank q WHERE q.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(q) FROM QuestionBank q WHERE q.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(q) FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.isActive = true")
    long countActiveBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT SUM(q.usageCount) FROM QuestionBank q WHERE q.subject.id = :subjectId")
    Long getTotalUsageCountBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT AVG(q.correctAnswerRate) FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.usageCount > 0")
    Double getAverageCorrectRateBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT AVG(q.averageTimeSeconds) FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.usageCount > 0")
    Double getAverageTimeBySubject(@Param("subjectId") Long subjectId);

    // Pagination support
    Page<QuestionBank> findBySubjectId(Long subjectId, Pageable pageable);

    Page<QuestionBank> findByTeacherId(Long teacherId, Pageable pageable);

    Page<QuestionBank> findByQuestionType(QuestionBank.QuestionType questionType, Pageable pageable);

    Page<QuestionBank> findByDifficultyLevel(QuestionBank.DifficultyLevel difficultyLevel, Pageable pageable);

    Page<QuestionBank> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT q FROM QuestionBank q WHERE " +
           "(:subjectId IS NULL OR q.subject.id = :subjectId) AND " +
           "(:teacherId IS NULL OR q.teacher.id = :teacherId) AND " +
           "(:questionType IS NULL OR q.questionType = :questionType) AND " +
           "(:difficultyLevel IS NULL OR q.difficultyLevel = :difficultyLevel) AND " +
           "(:isActive IS NULL OR q.isActive = :isActive) AND " +
           "(:chapterTopic IS NULL OR q.chapterTopic LIKE %:chapterTopic%)")
    Page<QuestionBank> advancedSearch(@Param("subjectId") Long subjectId,
                                     @Param("teacherId") Long teacherId,
                                     @Param("questionType") QuestionBank.QuestionType questionType,
                                     @Param("difficultyLevel") QuestionBank.DifficultyLevel difficultyLevel,
                                     @Param("isActive") Boolean isActive,
                                     @Param("chapterTopic") String chapterTopic,
                                     Pageable pageable);

    @Query("SELECT q.questionType as type, COUNT(q) as count FROM QuestionBank q GROUP BY q.questionType")
    List<Object[]> getQuestionStatisticsByType();

    @Query("SELECT q.difficultyLevel as level, COUNT(q) as count FROM QuestionBank q GROUP BY q.difficultyLevel")
    List<Object[]> getQuestionStatisticsByDifficulty();

    @Query("SELECT q.isActive as active, COUNT(q) as count FROM QuestionBank q GROUP BY q.isActive")
    List<Object[]> getQuestionStatisticsByActiveStatus();

    @Query("SELECT q FROM QuestionBank q WHERE q.isActive = true ORDER BY q.usageCount DESC")
    List<QuestionBank> findMostUsedQuestions(Pageable pageable);

    @Query("SELECT q FROM QuestionBank q WHERE q.isActive = true ORDER BY q.correctAnswerRate DESC")
    List<QuestionBank> findHighestCorrectRateQuestions(Pageable pageable);

    @Query("SELECT q FROM QuestionBank q WHERE q.isActive = true ORDER BY q.createdAt DESC")
    List<QuestionBank> findRecentlyCreatedQuestions(Pageable pageable);

    @Query("SELECT q FROM QuestionBank q WHERE q.questionText LIKE %:keyword% OR q.explanation LIKE %:keyword%")
    List<QuestionBank> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT q FROM QuestionBank q WHERE q.tags LIKE %:tag%")
    List<QuestionBank> findByTag(@Param("tag") String tag);

    @Query("SELECT q FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.difficultyLevel = :difficultyLevel AND q.isActive = true")
    List<QuestionBank> findQuestionsBySubjectAndDifficulty(@Param("subjectId") Long subjectId,
                                                          @Param("difficultyLevel") QuestionBank.DifficultyLevel difficultyLevel);

    @Query("SELECT q FROM QuestionBank q WHERE q.subject.id = :subjectId AND q.questionType = :questionType AND q.isActive = true")
    List<QuestionBank> findQuestionsBySubjectAndType(@Param("subjectId") Long subjectId,
                                                    @Param("questionType") QuestionBank.QuestionType questionType);
}