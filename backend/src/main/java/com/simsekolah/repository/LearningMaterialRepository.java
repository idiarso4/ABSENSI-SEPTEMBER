package com.simsekolah.repository;

import com.simsekolah.entity.LearningMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {

    List<LearningMaterial> findBySubjectId(Long subjectId);

    List<LearningMaterial> findBySubjectIdOrderByCreatedAtDesc(Long subjectId);

    List<LearningMaterial> findByTeacherId(Long teacherId);

    List<LearningMaterial> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);

    List<LearningMaterial> findByMaterialType(LearningMaterial.MaterialType materialType);

    List<LearningMaterial> findByDifficultyLevel(LearningMaterial.DifficultyLevel difficultyLevel);

    List<LearningMaterial> findByIsPublished(Boolean isPublished);

    List<LearningMaterial> findByIsPublishedOrderByPublishDateDesc(Boolean isPublished);

    List<LearningMaterial> findByChapterNumber(Integer chapterNumber);

    List<LearningMaterial> findBySubjectIdAndChapterNumber(Long subjectId, Integer chapterNumber);

    @Query("SELECT l FROM LearningMaterial l WHERE l.subject.id = :subjectId AND l.isPublished = true ORDER BY l.chapterNumber, l.createdAt")
    List<LearningMaterial> findPublishedMaterialsBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT l FROM LearningMaterial l WHERE l.teacher.id = :teacherId AND l.isPublished = true ORDER BY l.publishDate DESC")
    List<LearningMaterial> findPublishedMaterialsByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(l) FROM LearningMaterial l WHERE l.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(l) FROM LearningMaterial l WHERE l.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(l) FROM LearningMaterial l WHERE l.subject.id = :subjectId AND l.isPublished = true")
    long countPublishedBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT SUM(l.viewCount) FROM LearningMaterial l WHERE l.subject.id = :subjectId")
    Long getTotalViewCountBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT SUM(l.downloadCount) FROM LearningMaterial l WHERE l.subject.id = :subjectId")
    Long getTotalDownloadCountBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT AVG(l.ratingAverage) FROM LearningMaterial l WHERE l.subject.id = :subjectId AND l.ratingCount > 0")
    Double getAverageRatingBySubject(@Param("subjectId") Long subjectId);

    // Pagination support
    Page<LearningMaterial> findBySubjectId(Long subjectId, Pageable pageable);

    Page<LearningMaterial> findByTeacherId(Long teacherId, Pageable pageable);

    Page<LearningMaterial> findByMaterialType(LearningMaterial.MaterialType materialType, Pageable pageable);

    Page<LearningMaterial> findByDifficultyLevel(LearningMaterial.DifficultyLevel difficultyLevel, Pageable pageable);

    Page<LearningMaterial> findByIsPublished(Boolean isPublished, Pageable pageable);

    @Query("SELECT l FROM LearningMaterial l WHERE " +
           "(:subjectId IS NULL OR l.subject.id = :subjectId) AND " +
           "(:teacherId IS NULL OR l.teacher.id = :teacherId) AND " +
           "(:materialType IS NULL OR l.materialType = :materialType) AND " +
           "(:difficultyLevel IS NULL OR l.difficultyLevel = :difficultyLevel) AND " +
           "(:isPublished IS NULL OR l.isPublished = :isPublished) AND " +
           "(:chapterNumber IS NULL OR l.chapterNumber = :chapterNumber)")
    Page<LearningMaterial> advancedSearch(@Param("subjectId") Long subjectId,
                                         @Param("teacherId") Long teacherId,
                                         @Param("materialType") LearningMaterial.MaterialType materialType,
                                         @Param("difficultyLevel") LearningMaterial.DifficultyLevel difficultyLevel,
                                         @Param("isPublished") Boolean isPublished,
                                         @Param("chapterNumber") Integer chapterNumber,
                                         Pageable pageable);

    @Query("SELECT l.materialType as type, COUNT(l) as count FROM LearningMaterial l GROUP BY l.materialType")
    List<Object[]> getMaterialStatisticsByType();

    @Query("SELECT l.difficultyLevel as level, COUNT(l) as count FROM LearningMaterial l GROUP BY l.difficultyLevel")
    List<Object[]> getMaterialStatisticsByDifficulty();

    @Query("SELECT l.isPublished as published, COUNT(l) as count FROM LearningMaterial l GROUP BY l.isPublished")
    List<Object[]> getMaterialStatisticsByPublishStatus();

    @Query("SELECT l FROM LearningMaterial l WHERE l.isPublished = true ORDER BY l.viewCount DESC")
    List<LearningMaterial> findMostViewedMaterials(Pageable pageable);

    @Query("SELECT l FROM LearningMaterial l WHERE l.isPublished = true ORDER BY l.ratingAverage DESC, l.ratingCount DESC")
    List<LearningMaterial> findTopRatedMaterials(Pageable pageable);

    @Query("SELECT l FROM LearningMaterial l WHERE l.isPublished = true ORDER BY l.publishDate DESC")
    List<LearningMaterial> findRecentlyPublishedMaterials(Pageable pageable);

    @Query("SELECT l FROM LearningMaterial l WHERE l.title LIKE %:keyword% OR l.description LIKE %:keyword% OR l.topic LIKE %:keyword%")
    List<LearningMaterial> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT l FROM LearningMaterial l WHERE l.tags LIKE %:tag%")
    List<LearningMaterial> findByTag(@Param("tag") String tag);
}