package com.simsekolah.service;

import com.simsekolah.dto.request.CreateLearningMaterialRequest;
import com.simsekolah.dto.response.LearningMaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LearningMaterialService {

    LearningMaterialResponse createMaterial(CreateLearningMaterialRequest request);

    Page<LearningMaterialResponse> getAllMaterials(Pageable pageable);

    Optional<LearningMaterialResponse> getMaterialById(Long materialId);

    Page<LearningMaterialResponse> getMaterialsBySubject(Long subjectId, Pageable pageable);

    Page<LearningMaterialResponse> getMaterialsByTeacher(Long teacherId, Pageable pageable);

    LearningMaterialResponse publishMaterial(Long materialId);

    LearningMaterialResponse unpublishMaterial(Long materialId);

    Page<LearningMaterialResponse> getPublishedMaterials(Pageable pageable);

    void incrementViewCount(Long materialId);

    void incrementDownloadCount(Long materialId);

    Map<String, Object> getMaterialStatistics();

    List<LearningMaterialResponse> getMostViewedMaterials(int limit);

    Page<LearningMaterialResponse> advancedSearch(Long subjectId, Long teacherId, String materialType, String difficultyLevel,
                                                   Boolean isPublished, Integer chapterNumber, Pageable pageable);
}