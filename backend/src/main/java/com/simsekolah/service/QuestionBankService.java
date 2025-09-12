package com.simsekolah.service;

import com.simsekolah.dto.request.CreateQuestionBankRequest;
import com.simsekolah.dto.response.QuestionBankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QuestionBankService {

    QuestionBankResponse createQuestion(CreateQuestionBankRequest request);

    Page<QuestionBankResponse> getAllQuestions(Pageable pageable);

    Optional<QuestionBankResponse> getQuestionById(Long questionId);

    Page<QuestionBankResponse> getQuestionsBySubject(Long subjectId, Pageable pageable);

    Page<QuestionBankResponse> getQuestionsByTeacher(Long teacherId, Pageable pageable);

    Page<QuestionBankResponse> getActiveQuestions(Pageable pageable);

    QuestionBankResponse activateQuestion(Long questionId);

    QuestionBankResponse deactivateQuestion(Long questionId);

    List<QuestionBankResponse> getQuestionsByDifficultyAndSubject(Long subjectId, String difficultyLevel);

    List<QuestionBankResponse> getQuestionsByTypeAndSubject(Long subjectId, String questionType);

    Map<String, Object> getQuestionStatistics();

    List<QuestionBankResponse> getMostUsedQuestions(int limit);

    Page<QuestionBankResponse> advancedSearch(Long subjectId, Long teacherId, String questionType, String difficultyLevel,
                                               Boolean isActive, String chapterTopic, Pageable pageable);
}