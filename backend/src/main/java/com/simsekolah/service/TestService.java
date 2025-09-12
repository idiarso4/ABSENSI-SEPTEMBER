package com.simsekolah.service;

import com.simsekolah.dto.request.CreateTestRequest;
import com.simsekolah.dto.response.TestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TestService {

    TestResponse createTest(CreateTestRequest request);

    Page<TestResponse> getAllTests(Pageable pageable);

    Optional<TestResponse> getTestById(Long testId);

    Page<TestResponse> getTestsBySubject(Long subjectId, Pageable pageable);

    Page<TestResponse> getTestsByTeacher(Long teacherId, Pageable pageable);

    Page<TestResponse> getTestsByClassRoom(Long classRoomId, Pageable pageable);

    TestResponse publishTest(Long testId);

    Page<TestResponse> getPublishedTests(Pageable pageable);

    List<TestResponse> getTodaysTests();

    List<TestResponse> getUpcomingTests();

    Map<String, Object> getTestStatistics();

    List<TestResponse> getTestsBySubjectAndType(Long subjectId, String testType);

    Page<TestResponse> advancedSearch(Long subjectId, Long teacherId, Long classRoomId, String testType,
                                      String testStatus, Boolean isPublished, LocalDate startDate, LocalDate endDate, Pageable pageable);
}