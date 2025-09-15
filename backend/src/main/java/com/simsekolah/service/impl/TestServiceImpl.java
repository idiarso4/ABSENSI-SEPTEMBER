package com.simsekolah.service.impl;

import com.simsekolah.dto.request.CreateTestRequest;
import com.simsekolah.dto.response.TestResponse;
import com.simsekolah.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    @Override
    public TestResponse createTest(CreateTestRequest request) {
        log.info("Creating test for request: {}", request);
        // TODO: Implement actual test creation logic
        return TestResponse.builder()
                .id(1L)
                .testTitle(request.getTestTitle())
                .testDescription(request.getTestDescription())
                .testType(request.getTestType())
                .subjectId(request.getSubjectId())
                .classRoomId(request.getClassRoomId())
                .teacherId(request.getTeacherId())
                .testDate(request.getTestDate())
                .testTime(request.getTestTime())
                .durationMinutes(request.getDurationMinutes())
                .totalQuestions(request.getTotalQuestions())
                .passingScore(request.getPassingScore())
                .isPublished(false)
                .status("DRAFT")
                .build();
    }

    @Override
    public Page<TestResponse> getAllTests(Pageable pageable) {
        log.info("Getting all tests with pagination: {}", pageable);
            // Simple in-memory mock data for demonstration
            List<TestResponse> tests = Arrays.asList(
                TestResponse.builder()
                    .id(1L)
                    .testTitle("Math Midterm")
                    .testDescription("Midterm exam for mathematics")
                    .testType("MIDTERM")
                    .subjectId(101L)
                    .classRoomId(201L)
                    .teacherId(301L)
                    .testDate(LocalDate.of(2025, 9, 20))
                    .testTime(LocalTime.parse("09:00"))
                    .durationMinutes(90)
                    .totalQuestions(40)
                    .passingScore(70.0)
                    .isPublished(true)
                    .status("PUBLISHED")
                    .build(),
                TestResponse.builder()
                    .id(2L)
                    .testTitle("Science Quiz")
                    .testDescription("Quiz for science chapter 3")
                    .testType("QUIZ")
                    .subjectId(102L)
                    .classRoomId(202L)
                    .teacherId(302L)
                    .testDate(LocalDate.of(2025, 9, 22))
                    .testTime(LocalTime.parse("10:00"))
                    .durationMinutes(30)
                    .totalQuestions(20)
                    .passingScore(60.0)
                    .isPublished(false)
                    .status("DRAFT")
                    .build()
            );
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), tests.size());
            List<TestResponse> pageContent = start < end ? tests.subList(start, end) : new ArrayList<>();
            return new PageImpl<>(pageContent, pageable, tests.size());
    }

    @Override
    public Optional<TestResponse> getTestById(Long testId) {
        log.info("Getting test by ID: {}", testId);
        // TODO: Implement actual data retrieval
        return Optional.empty();
    }

    @Override
    public Page<TestResponse> getTestsBySubject(Long subjectId, Pageable pageable) {
        log.info("Getting tests for subject ID: {} with pagination: {}", subjectId, pageable);
            // Mock data, filter by subjectId
            List<TestResponse> allTests = Arrays.asList(
                TestResponse.builder()
                    .id(1L)
                    .testTitle("Math Midterm")
                    .testDescription("Midterm exam for mathematics")
                    .testType("MIDTERM")
                    .subjectId(101L)
                    .classRoomId(201L)
                    .teacherId(301L)
                    .testDate(LocalDate.of(2025, 9, 20))
                    .testTime(LocalTime.parse("09:00"))
                    .durationMinutes(90)
                    .totalQuestions(40)
                    .passingScore(70.0)
                    .isPublished(true)
                    .status("PUBLISHED")
                    .build(),
                TestResponse.builder()
                    .id(2L)
                    .testTitle("Science Quiz")
                    .testDescription("Quiz for science chapter 3")
                    .testType("QUIZ")
                    .subjectId(102L)
                    .classRoomId(202L)
                    .teacherId(302L)
                    .testDate(LocalDate.of(2025, 9, 22))
                    .testTime(LocalTime.parse("10:00"))
                    .durationMinutes(30)
                    .totalQuestions(20)
                    .passingScore(60.0)
                    .isPublished(false)
                    .status("DRAFT")
                    .build()
            );
            List<TestResponse> filtered = new ArrayList<>();
            for (TestResponse test : allTests) {
                if (Objects.equals(test.getSubjectId(), subjectId)) {
                    filtered.add(test);
                }
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filtered.size());
            List<TestResponse> pageContent = start < end ? filtered.subList(start, end) : new ArrayList<>();
            return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public Page<TestResponse> getTestsByTeacher(Long teacherId, Pageable pageable) {
        log.info("Getting tests for teacher ID: {} with pagination: {}", teacherId, pageable);
            // Mock data, filter by teacherId
            List<TestResponse> allTests = Arrays.asList(
                TestResponse.builder()
                    .id(1L)
                    .testTitle("Math Midterm")
                    .testDescription("Midterm exam for mathematics")
                    .testType("MIDTERM")
                    .subjectId(101L)
                    .classRoomId(201L)
                    .teacherId(301L)
                    .testDate(LocalDate.of(2025, 9, 20))
                    .testTime(LocalTime.parse("09:00"))
                    .durationMinutes(90)
                    .totalQuestions(40)
                    .passingScore(70.0)
                    .isPublished(true)
                    .status("PUBLISHED")
                    .build(),
                TestResponse.builder()
                    .id(2L)
                    .testTitle("Science Quiz")
                    .testDescription("Quiz for science chapter 3")
                    .testType("QUIZ")
                    .subjectId(102L)
                    .classRoomId(202L)
                    .teacherId(302L)
                    .testDate(LocalDate.of(2025, 9, 22))
                    .testTime(LocalTime.parse("10:00"))
                    .durationMinutes(30)
                    .totalQuestions(20)
                    .passingScore(60.0)
                    .isPublished(false)
                    .status("DRAFT")
                    .build()
            );
            List<TestResponse> filtered = new ArrayList<>();
            for (TestResponse test : allTests) {
                if (Objects.equals(test.getTeacherId(), teacherId)) {
                    filtered.add(test);
                }
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filtered.size());
            List<TestResponse> pageContent = start < end ? filtered.subList(start, end) : new ArrayList<>();
            return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public Page<TestResponse> getTestsByClassRoom(Long classRoomId, Pageable pageable) {
        log.info("Getting tests for classroom ID: {} with pagination: {}", classRoomId, pageable);
            // Mock data, filter by classRoomId
            List<TestResponse> allTests = Arrays.asList(
                TestResponse.builder()
                    .id(1L)
                    .testTitle("Math Midterm")
                    .testDescription("Midterm exam for mathematics")
                    .testType("MIDTERM")
                    .subjectId(101L)
                    .classRoomId(201L)
                    .teacherId(301L)
                    .testDate(LocalDate.of(2025, 9, 20))
                    .testTime(LocalTime.parse("09:00"))
                    .durationMinutes(90)
                    .totalQuestions(40)
                    .passingScore(70.0)
                    .isPublished(true)
                    .status("PUBLISHED")
                    .build(),
                TestResponse.builder()
                    .id(2L)
                    .testTitle("Science Quiz")
                    .testDescription("Quiz for science chapter 3")
                    .testType("QUIZ")
                    .subjectId(102L)
                    .classRoomId(202L)
                    .teacherId(302L)
                    .testDate(LocalDate.of(2025, 9, 22))
                    .testTime(LocalTime.parse("10:00"))
                    .durationMinutes(30)
                    .totalQuestions(20)
                    .passingScore(60.0)
                    .isPublished(false)
                    .status("DRAFT")
                    .build()
            );
            List<TestResponse> filtered = new ArrayList<>();
            for (TestResponse test : allTests) {
                if (Objects.equals(test.getClassRoomId(), classRoomId)) {
                    filtered.add(test);
                }
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filtered.size());
            List<TestResponse> pageContent = start < end ? filtered.subList(start, end) : new ArrayList<>();
            return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public TestResponse publishTest(Long testId) {
        log.info("Publishing test with ID: {}", testId);
        // TODO: Implement actual publish logic
        return TestResponse.builder()
                .id(testId)
                .isPublished(true)
                .build();
    }

    @Override
    public Page<TestResponse> getPublishedTests(Pageable pageable) {
        log.info("Getting published tests with pagination: {}", pageable);
        // Mock data, filter by isPublished
        List<TestResponse> allTests = Arrays.asList(
            TestResponse.builder()
                .id(1L)
                .testTitle("Math Midterm")
                .testDescription("Midterm exam for mathematics")
                .testType("MIDTERM")
                .subjectId(101L)
                .classRoomId(201L)
                .teacherId(301L)
                .testDate(LocalDate.of(2025, 9, 20))
                .testTime(LocalTime.parse("09:00"))
                .durationMinutes(90)
                .totalQuestions(40)
                .passingScore(70.0)
                .isPublished(true)
                .status("PUBLISHED")
                .build(),
            TestResponse.builder()
                .id(2L)
                .testTitle("Science Quiz")
                .testDescription("Quiz for science chapter 3")
                .testType("QUIZ")
                .subjectId(102L)
                .classRoomId(202L)
                .teacherId(302L)
                .testDate(LocalDate.of(2025, 9, 22))
                .testTime(LocalTime.parse("10:00"))
                .durationMinutes(30)
                .totalQuestions(20)
                .passingScore(60.0)
                .isPublished(false)
                .status("DRAFT")
                .build()
        );
        List<TestResponse> filtered = new ArrayList<>();
        for (TestResponse test : allTests) {
            if (Boolean.TRUE.equals(test.getIsPublished())) {
                filtered.add(test);
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<TestResponse> pageContent = start < end ? filtered.subList(start, end) : new ArrayList<>();
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public List<TestResponse> getTodaysTests() {
        log.info("Getting today's tests");
        // Mock data, filter by today's date
        List<TestResponse> allTests = Arrays.asList(
            TestResponse.builder()
                .id(1L)
                .testTitle("Math Midterm")
                .testDescription("Midterm exam for mathematics")
                .testType("MIDTERM")
                .subjectId(101L)
                .classRoomId(201L)
                .teacherId(301L)
                .testDate(LocalDate.of(2025, 9, 20))
                .testTime(LocalTime.parse("09:00"))
                .durationMinutes(90)
                .totalQuestions(40)
                .passingScore(70.0)
                .isPublished(true)
                .status("PUBLISHED")
                .build(),
            TestResponse.builder()
                .id(2L)
                .testTitle("Science Quiz")
                .testDescription("Quiz for science chapter 3")
                .testType("QUIZ")
                .subjectId(102L)
                .classRoomId(202L)
                .teacherId(302L)
                .testDate(LocalDate.of(2025, 9, 22))
                .testTime(LocalTime.parse("10:00"))
                .durationMinutes(30)
                .totalQuestions(20)
                .passingScore(60.0)
                .isPublished(false)
                .status("DRAFT")
                .build(),
            TestResponse.builder()
                .id(3L)
                .testTitle("Today's Test")
                .testDescription("Test scheduled for today")
                .testType("QUIZ")
                .subjectId(103L)
                .classRoomId(203L)
                .teacherId(303L)
                .testDate(LocalDate.now())
                .testTime(LocalTime.parse("11:00"))
                .durationMinutes(45)
                .totalQuestions(25)
                .passingScore(65.0)
                .isPublished(true)
                .status("PUBLISHED")
                .build()
        );
        List<TestResponse> filtered = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (TestResponse test : allTests) {
            if (today.equals(test.getTestDate())) {
                filtered.add(test);
            }
        }
        return filtered;
    }

    @Override
    public List<TestResponse> getUpcomingTests() {
        log.info("Getting upcoming tests");
        // TODO: Implement actual data retrieval
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getTestStatistics() {
        log.info("Getting test statistics");
        // TODO: Implement actual statistics calculation
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTests", 0);
        stats.put("publishedTests", 0);
        stats.put("upcomingTests", 0);
        stats.put("completedTests", 0);
        return stats;
    }

    @Override
    public List<TestResponse> getTestsBySubjectAndType(Long subjectId, String testType) {
        log.info("Getting tests by subject ID: {} and type: {}", subjectId, testType);
        // TODO: Implement actual data retrieval
        return new ArrayList<>();
    }

    @Override
    public Page<TestResponse> advancedSearch(Long subjectId, Long teacherId, Long classRoomId, String testType,
                                           String testStatus, Boolean isPublished, LocalDate startDate, 
                                           LocalDate endDate, Pageable pageable) {
        log.info("Advanced search for tests - subjectId: {}, teacherId: {}, classRoomId: {}, testType: {}, " +
                "testStatus: {}, isPublished: {}, startDate: {}, endDate: {}", 
                subjectId, teacherId, classRoomId, testType, testStatus, isPublished, startDate, endDate);
        // TODO: Implement actual search logic
        List<TestResponse> tests = new ArrayList<>();
        return new PageImpl<>(tests, pageable, 0);
    }
}