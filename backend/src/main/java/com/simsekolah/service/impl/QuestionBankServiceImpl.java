package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateQuestionBankRequest;
import com.simsekolah.dto.response.QuestionBankResponse;
import com.simsekolah.repository.QuestionBankRepository;
import com.simsekolah.repository.SubjectRepository;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.entity.QuestionBank;
import com.simsekolah.entity.Subject;
import com.simsekolah.entity.User;
import com.simsekolah.service.QuestionBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionBankServiceImpl implements QuestionBankService {

    private final QuestionBankRepository questionBankRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public QuestionBankResponse createQuestion(CreateQuestionBankRequest request) {
        log.info("Creating question bank entry");

        // Validate required fields (questionText, subjectId, teacherId)
        if (request.getQuestionText() == null || request.getQuestionText().isBlank()) {
            throw new IllegalArgumentException("Question text is required");
        }
        if (request.getSubjectId() == null) {
            throw new IllegalArgumentException("Subject ID is required");
        }
        if (request.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher ID is required");
        }

    // Fetch subject and teacher
    // Jika import dari Excel, bisa tambahkan mapping dari nama ke ID di sini
    // Contoh: jika request.getSubjectId() == null && request.getSubjectName() != null
    // Subject subject = subjectRepository.findByName(request.getSubjectName()) ...
    Subject subject = subjectRepository.findById(request.getSubjectId())
        .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + request.getSubjectId()));
    User teacher = userRepository.findById(request.getTeacherId())
        .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + request.getTeacherId()));

    // NOTE: Untuk import Excel, pastikan template menyertakan kolom Nama Guru/Nama Mapel yang sesuai dengan data master
    // dan lakukan mapping ke ID sebelum proses simpan. Jika ingin otomatis, tambahkan lookup di sini.

        // Map enums
        QuestionBank.QuestionType questionType = null;
        if (request.getQuestionType() != null) {
            try {
                questionType = QuestionBank.QuestionType.valueOf(request.getQuestionType().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid question type: " + request.getQuestionType());
            }
        }
        QuestionBank.DifficultyLevel difficultyLevel = QuestionBank.DifficultyLevel.MEDIUM;
        if (request.getDifficultyLevel() != null) {
            try {
                difficultyLevel = QuestionBank.DifficultyLevel.valueOf(request.getDifficultyLevel().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid difficulty level: " + request.getDifficultyLevel());
            }
        }

        // Build entity
        QuestionBank qb = new QuestionBank();
        qb.setQuestionText(request.getQuestionText());
        qb.setQuestionType(questionType);
        qb.setDifficultyLevel(difficultyLevel);
        qb.setSubject(subject);
        qb.setTeacher(teacher);
        qb.setChapterTopic(request.getChapterTopic());
        qb.setCorrectAnswer(request.getCorrectAnswer());
        qb.setExplanation(request.getExplanation());
        qb.setPoints(request.getPoints() != null ? request.getPoints() : 1);
        qb.setIsActive(true);
        qb.setUsageCount(0);
        qb.setCorrectAnswerRate(0.0);
        qb.setAverageTimeSeconds(0.0);

        // Save
        QuestionBank saved = questionBankRepository.save(qb);

        // Map to response
        return QuestionBankResponse.builder()
                .id(saved.getId())
                .questionText(saved.getQuestionText())
                .questionType(saved.getQuestionType() != null ? saved.getQuestionType().name() : null)
                .difficultyLevel(saved.getDifficultyLevel() != null ? saved.getDifficultyLevel().name() : null)
                .subjectId(saved.getSubject() != null ? saved.getSubject().getId() : null)
                .teacherId(saved.getTeacher() != null ? saved.getTeacher().getId() : null)
                .chapterTopic(saved.getChapterTopic())
                .correctAnswer(saved.getCorrectAnswer())
                .explanation(saved.getExplanation())
                .points(saved.getPoints())
                .isActive(saved.getIsActive())
                .usageCount(saved.getUsageCount())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public Page<QuestionBankResponse> getAllQuestions(Pageable pageable) {
        log.info("Getting all questions - stub implementation");
        // TODO: Implement actual query logic
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Optional<QuestionBankResponse> getQuestionById(Long questionId) {
        log.info("Getting question by ID: {} - stub implementation", questionId);
        // TODO: Implement actual query logic
        return Optional.empty();
    }

    @Override
    public Page<QuestionBankResponse> getQuestionsBySubject(Long subjectId, Pageable pageable) {
        log.info("Getting questions by subject ID: {} - stub implementation", subjectId);
        // TODO: Implement actual query logic
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Page<QuestionBankResponse> getQuestionsByTeacher(Long teacherId, Pageable pageable) {
        log.info("Getting questions by teacher ID: {} - stub implementation", teacherId);
        // TODO: Implement actual query logic
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Page<QuestionBankResponse> getActiveQuestions(Pageable pageable) {
        log.info("Getting active questions - stub implementation");
        // TODO: Implement actual query logic
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    @Transactional
    public QuestionBankResponse activateQuestion(Long questionId) {
        log.info("Activating question ID: {} - stub implementation", questionId);
        // TODO: Implement actual activation logic
        return new QuestionBankResponse();
    }

    @Override
    @Transactional
    public QuestionBankResponse deactivateQuestion(Long questionId) {
        log.info("Deactivating question ID: {} - stub implementation", questionId);
        // TODO: Implement actual deactivation logic
        return new QuestionBankResponse();
    }

    @Override
    public List<QuestionBankResponse> getQuestionsByDifficultyAndSubject(Long subjectId, String difficultyLevel) {
        log.info("Getting questions by subject ID: {} and difficulty: {} - stub implementation", subjectId, difficultyLevel);
        // TODO: Implement actual query logic
        return Collections.emptyList();
    }

    @Override
    public List<QuestionBankResponse> getQuestionsByTypeAndSubject(Long subjectId, String questionType) {
        log.info("Getting questions by subject ID: {} and type: {} - stub implementation", subjectId, questionType);
        // TODO: Implement actual query logic
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getQuestionStatistics() {
        log.info("Getting question statistics - stub implementation");
        // TODO: Implement actual statistics logic
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestions", 0);
        stats.put("activeQuestions", 0);
        stats.put("questionsByDifficulty", new HashMap<String, Integer>());
        stats.put("questionsByType", new HashMap<String, Integer>());
        return stats;
    }

    @Override
    public List<QuestionBankResponse> getMostUsedQuestions(int limit) {
        log.info("Getting most used questions with limit: {} - stub implementation", limit);
        // TODO: Implement actual query logic
        return Collections.emptyList();
    }

    @Override
    public Page<QuestionBankResponse> advancedSearch(Long subjectId, Long teacherId, String questionType, 
                                                     String difficultyLevel, Boolean isActive, String chapterTopic, 
                                                     Pageable pageable) {
        log.info("Advanced search with parameters - stub implementation");
        // TODO: Implement actual advanced search logic
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}