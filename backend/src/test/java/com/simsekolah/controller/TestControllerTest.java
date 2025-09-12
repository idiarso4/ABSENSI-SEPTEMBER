package com.simsekolah.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simsekolah.entity.User;
import com.simsekolah.enums.UserType;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private com.simsekolah.repository.UserRepository userRepository;

    @Mock
    private com.simsekolah.repository.StudentRepository studentRepository;

    @Mock
    private com.simsekolah.repository.ClassRoomRepository classRoomRepository;

    @InjectMocks
    private TestController testController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void generateHash_Success() throws Exception {
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        mockMvc.perform(get("/api/test/hash/password"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Password: password")))
                .andExpect(content().string(containsString("Hash: hashedPassword")))
                .andExpect(content().string(containsString("Matches: true")));

        verify(passwordEncoder).encode("password");
        verify(passwordEncoder).matches("password", "hashedPassword");
    }

    @Test
    void verifyPassword_Success() throws Exception {
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        mockMvc.perform(post("/api/test/verify")
                        .param("password", "password")
                        .param("hash", "hashedPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Password: password")))
                .andExpect(content().string(containsString("Hash: hashedPassword")))
                .andExpect(content().string(containsString("Matches: true")));

        verify(passwordEncoder).matches("password", "hashedPassword");
    }

    @Test
    void listUsers_Success() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/test/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total users: 0")));

        verify(userRepository).findAll();
    }

    @Test
    void listUsersSimple_Success() throws Exception {
        mockMvc.perform(get("/api/test/users/simple"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Database connection test - checking if users table exists")));

        // No mocks to verify for this endpoint
    }

    @Test
    void testDatabase_Success() throws Exception {
        mockMvc.perform(get("/api/test/db/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Database test endpoint - backend is running")));

        // No mocks to verify for this endpoint
    }

    @Test
    void createAdmin_Success() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/test/create-admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin user created with ID: ")));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void forceLogin_Success() throws Exception {
        mockMvc.perform(post("/api/test/force-login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.name").value("System Administrator"))
                .andExpect(jsonPath("$.user.email").value("admin@sim.edu"))
                .andExpect(jsonPath("$.user.userType").value("ADMIN"))
                .andExpect(jsonPath("$.user.roles[0]").value("ADMIN"))
                .andExpect(jsonPath("$.message").value("DEVELOPMENT LOGIN - BYPASSED AUTHENTICATION"));
    }

    @Test
    void getDashboardData_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);
        when(userRepository.count()).thenReturn(20L);
        when(classRoomRepository.count()).thenReturn(10L);
        when(studentRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/test/dashboard-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statistics.totalStudents").value(100))
                .andExpect(jsonPath("$.statistics.totalTeachers").value(20))
                .andExpect(jsonPath("$.statistics.totalClasses").value(10))
                .andExpect(jsonPath("$.statistics.attendanceRate").value(98.5))
                .andExpect(jsonPath("$.recentActivities[0].icon").value("user-plus"))
                .andExpect(jsonPath("$.recentActivities[0].type").value("success"))
                .andExpect(jsonPath("$.recentActivities[0].message").exists())
                .andExpect(jsonPath("$.recentActivities[0].time").exists())
                .andExpect(jsonPath("$.recentActivities[1].icon").value("user-tie"))
                .andExpect(jsonPath("$.recentActivities[1].type").value("info"))
                .andExpect(jsonPath("$.recentActivities[1].message").exists())
                .andExpect(jsonPath("$.recentActivities[1].time").exists())
                .andExpect(jsonPath("$.recentActivities[2].icon").value("school"))
                .andExpect(jsonPath("$.recentActivities[2].type").value("primary"))
                .andExpect(jsonPath("$.recentActivities[2].message").exists())
                .andExpect(jsonPath("$.recentActivities[2].time").exists())
                .andExpect(jsonPath("$.recentActivities[3].icon").value("database"))
                .andExpect(jsonPath("$.recentActivities[3].type").value("info"))
                .andExpect(jsonPath("$.recentActivities[3].message").exists())
                .andExpect(jsonPath("$.recentActivities[3].time").value("now"));

        verify(studentRepository).count();
        verify(userRepository).count();
        verify(classRoomRepository).count();
        verify(studentRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getRealStudents_Success() throws Exception {
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/test/students/sample")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.empty").value(true));

        verify(studentRepository).findAll(any(Pageable.class));
    }

    @Test
    void getDetailedStatistics_Success() throws Exception {
        when(studentRepository.count()).thenReturn(100L);
        when(userRepository.countByUserType(eq(UserType.TEACHER))).thenReturn(15L);
        when(classRoomRepository.count()).thenReturn(10L);

        mockMvc.perform(get("/api/test/statistics/detailed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students.total").value(100))
                .andExpect(jsonPath("$.students.active").value(100))
                .andExpect(jsonPath("$.students.inactive").value(0))
                .andExpect(jsonPath("$.students.graduated").value(0))
                .andExpect(jsonPath("$.teachers.total").value(15))
                .andExpect(jsonPath("$.teachers.active").value(15))
                .andExpect(jsonPath("$.teachers.inactive").value(0))
                .andExpect(jsonPath("$.classes.total").value(10))
                .andExpect(jsonPath("$.classes.active").value(10))
                .andExpect(jsonPath("$.classes.inactive").value(0))
                .andExpect(jsonPath("$.classes.averageCapacity").value(30))
                .andExpect(jsonPath("$.classes.averageEnrollment").value(25))
                .andExpect(jsonPath("$.attendance.todayRate").value(0.0))
                .andExpect(jsonPath("$.attendance.weeklyRate").value(0.0))
                .andExpect(jsonPath("$.attendance.monthlyRate").value(0.0))
                .andExpect(jsonPath("$.attendance.present").value(0))
                .andExpect(jsonPath("$.attendance.absent").value(0))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.lastUpdated").exists());

        verify(studentRepository).count();
        verify(userRepository).countByUserType(eq(UserType.TEACHER));
        verify(classRoomRepository).count();
    }

    @Test
    void getRealRecentActivities_Success() throws Exception {
        when(studentRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(classRoomRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/test/activities/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activities").isArray())
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(studentRepository).findAll(any(PageRequest.class));
        verify(userRepository).findAll(any(PageRequest.class));
        verify(classRoomRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getSystemStatus_Success() throws Exception {
        mockMvc.perform(get("/api/test/system/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.database").value("UP"))
                .andExpect(jsonPath("$.backend").value("UP"))
                .andExpect(jsonPath("$.authentication").value("UP"))
                .andExpect(jsonPath("$.fileSystem").value("UP"))
                .andExpect(jsonPath("$.performance.cpuUsage").value("N/A"))
                .andExpect(jsonPath("$.performance.memoryUsage").value("N/A"))
                .andExpect(jsonPath("$.performance.diskUsage").value("N/A"))
                .andExpect(jsonPath("$.performance.responseTime").value("N/A"))
                .andExpect(jsonPath("$.uptime").value("5 days, 12 hours"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.environment").value("development"))
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getRealTeachers_Success() throws Exception {
        when(userRepository.findByUserType(eq(UserType.TEACHER))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/test/teachers/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.empty").value(true));

        verify(userRepository).findByUserType(eq(UserType.TEACHER));
    }

    @Test
    void getRealTodayAttendance_Success() throws Exception {
        when(classRoomRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/test/attendance/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classes").isArray())
                .andExpect(jsonPath("$.classes.length()").value(0))
                .andExpect(jsonPath("$.summary.totalStudents").value(0))
                .andExpect(jsonPath("$.summary.totalPresent").value(0))
                .andExpect(jsonPath("$.summary.totalAbsent").value(0))
                .andExpect(jsonPath("$.summary.totalCapacity").value(0))
                .andExpect(jsonPath("$.summary.overallRate").value(0.0))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        verify(classRoomRepository).findAll();
    }

    @Test
    void searchStudents_Success() throws Exception {
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        Map<String, Object> searchRequest = new HashMap<>();
        searchRequest.put("query", "John");
        searchRequest.put("class", "10A");
        searchRequest.put("status", "ACTIVE");

        mockMvc.perform(post("/api/test/students/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"John\",\"class\":\"10A\",\"status\":\"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.searchQuery").value("John"))
                .andExpect(jsonPath("$.appliedFilters.class").value("10A"))
                .andExpect(jsonPath("$.appliedFilters.status").value("ACTIVE"));

        verify(studentRepository).findAll(any(Pageable.class));
    }

    @Test
    void getRealRecentGrades_Success() throws Exception {
        mockMvc.perform(get("/api/test/grades/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grades").isArray())
                .andExpect(jsonPath("$.grades.length()").value(1))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getDashboardAnalytics_Success() throws Exception {
        mockMvc.perform(get("/api/test/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentTrend").isArray())
                .andExpect(jsonPath("$.enrollmentTrend.length()").value(6))
                .andExpect(jsonPath("$.enrollmentTrend[0].month").value("Aug"))
                .andExpect(jsonPath("$.enrollmentTrend[0].count").value(1150))
                .andExpect(jsonPath("$.attendanceTrend").isArray())
                .andExpect(jsonPath("$.attendanceTrend.length()").value(7))
                .andExpect(jsonPath("$.attendanceTrend[0].day").value("Mon"))
                .andExpect(jsonPath("$.attendanceTrend[0].rate").value(98.2))
                .andExpect(jsonPath("$.gradeDistribution").isMap())
                .andExpect(jsonPath("$.gradeDistribution.A").value(245))
                .andExpect(jsonPath("$.topPerformingClasses").isArray())
                .andExpect(jsonPath("$.topPerformingClasses.length()").value(5))
                .andExpect(jsonPath("$.topPerformingClasses[0].name").value("12A"))
                .andExpect(jsonPath("$.topPerformingClasses[0].score").value(87.5))
                .andExpect(jsonPath("$.overallAverage").value(82.5))
                .andExpect(jsonPath("$.overallPassingRate").value(92.3))
                .andExpect(jsonPath("$.totalAssessments").value(1456))
                .andExpect(jsonPath("$.generatedAt").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void bulkStudentAction_Success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("action", "export");
        request.put("studentIds", List.of("1", "2"));

        mockMvc.perform(post("/api/test/students/bulk-action")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"action\":\"export\",\"studentIds\":[\"1\",\"2\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Exported 2 students successfully"))
                .andExpect(jsonPath("$.downloadUrl").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getRecentNotifications_Success() throws Exception {
        mockMvc.perform(get("/api/test/notifications/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifications").isArray())
                .andExpect(jsonPath("$.notifications.length()").value(5))
                .andExpect(jsonPath("$.unreadCount").exists())
                .andExpect(jsonPath("$.totalCount").value(5))
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getAttendanceSummary_Success() throws Exception {
        when(classRoomRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/test/attendance/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyData").isArray())
                .andExpect(jsonPath("$.weeklyData.length()").value(5))
                .andExpect(jsonPath("$.weeklyData[0].day").value("Monday"))
                .andExpect(jsonPath("$.weeklyData[0].totalStudents").value(1234))
                .andExpect(jsonPath("$.weeklyData[0].present").exists())
                .andExpect(jsonPath("$.weeklyData[0].absent").exists())
                .andExpect(jsonPath("$.weeklyData[0].rate").exists())
                .andExpect(jsonPath("$.monthlyTrends").isArray())
                .andExpect(jsonPath("$.monthlyTrends.length()").value(5))
                .andExpect(jsonPath("$.monthlyTrends[0].month").value("Sep"))
                .andExpect(jsonPath("$.monthlyTrends[0].rate").value(97.2))
                .andExpect(jsonPath("$.classAttendance").isArray())
                .andExpect(jsonPath("$.classAttendance.length()").value(6))
                .andExpect(jsonPath("$.classAttendance[0].className").value("10A"))
                .andExpect(jsonPath("$.classAttendance[0].capacity").value(30))
                .andExpect(jsonPath("$.classAttendance[0].present").exists())
                .andExpect(jsonPath("$.classAttendance[0].absent").exists())
                .andExpect(jsonPath("$.classAttendance[0].rate").exists())
                .andExpect(jsonPath("$.classAttendance[0].status").exists())
                .andExpect(jsonPath("$.overallRate").value(98.2))
                .andExpect(jsonPath("$.totalStudents").value(1234))
                .andExpect(jsonPath("$.presentToday").value(1212))
                .andExpect(jsonPath("$.absentToday").value(22))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(classRoomRepository).findAll();
    }

    @Test
    void markAttendance_Success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("className", "10A");
        request.put("date", "2023-01-01");
        request.put("attendance", List.of(Map.of("status", "present"), Map.of("status", "absent")));

        mockMvc.perform(post("/api/test/attendance/mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"className\":\"10A\",\"date\":\"2023-01-01\",\"attendance\":[{\"status\":\"present\"},{\"status\":\"absent\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Attendance marked for class 10A on 2023-01-01"))
                .andExpect(jsonPath("$.totalRecords").value(2))
                .andExpect(jsonPath("$.presentCount").value(1))
                .andExpect(jsonPath("$.absentCount").value(1))
                .andExpect(jsonPath("$.attendanceRate").value(50.0));

        // No mocks to verify for this endpoint
    }

    @Test
    void getGradesAnalytics_Success() throws Exception {
        mockMvc.perform(get("/api/test/grades/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectGrades").isMap())
                .andExpect(jsonPath("$.subjectGrades.Matematika").isMap())
                .andExpect(jsonPath("$.subjectGrades.Matematika.A").exists())
                .andExpect(jsonPath("$.classAverages").isArray())
                .andExpect(jsonPath("$.classAverages.length()").value(6))
                .andExpect(jsonPath("$.classAverages[0].className").value("10A"))
                .andExpect(jsonPath("$.classAverages[0].average").exists())
                .andExpect(jsonPath("$.classAverages[0].studentCount").exists())
                .andExpect(jsonPath("$.classAverages[0].highestScore").exists())
                .andExpect(jsonPath("$.classAverages[0].lowestScore").exists())
                .andExpect(jsonPath("$.classAverages[0].passingRate").exists())
                .andExpect(jsonPath("$.performanceTrends").isArray())
                .andExpect(jsonPath("$.performanceTrends.length()").value(5))
                .andExpect(jsonPath("$.performanceTrends[0].month").value("Sep"))
                .andExpect(jsonPath("$.performanceTrends[0].averageScore").exists())
                .andExpect(jsonPath("$.performanceTrends[0].passingRate").exists())
                .andExpect(jsonPath("$.overallAverage").value(82.5))
                .andExpect(jsonPath("$.overallPassingRate").value(92.3))
                .andExpect(jsonPath("$.totalAssessments").value(1456))
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void generateReports_Success() throws Exception {
        mockMvc.perform(get("/api/test/reports/generate")
                        .param("type", "attendance")
                        .param("period", "2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.reportId").exists())
                .andExpect(jsonPath("$.fileName").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.type").value("attendance"))
                .andExpect(jsonPath("$.period").value("2023"))
                .andExpect(jsonPath("$.status").value("generating"))
                .andExpect(jsonPath("$.estimatedTime").value("2-3 minutes"))
                .andExpect(jsonPath("$.downloadUrl").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getReportStatus_Success() throws Exception {
        mockMvc.perform(get("/api/test/reports/status/RPT-1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value("RPT-1234567890"))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.progress").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }

    @Test
    void getDashboardWidgets_Success() throws Exception {
        mockMvc.perform(get("/api/test/dashboard/widgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.widgets").isArray())
                .andExpect(jsonPath("$.widgets.length()").value(5))
                .andExpect(jsonPath("$.widgets[0].id").value("quick-stats"))
                .andExpect(jsonPath("$.widgets[0].title").value("Quick Statistics"))
                .andExpect(jsonPath("$.widgets[0].type").value("stats"))
                .andExpect(jsonPath("$.widgets[0].size").value("large"))
                .andExpect(jsonPath("$.widgets[0].data.students").value(1234))
                .andExpect(jsonPath("$.widgets[0].data.teachers").value(89))
                .andExpect(jsonPath("$.widgets[0].data.classes").value(45))
                .andExpect(jsonPath("$.widgets[0].data.attendance").value(98.2))
                .andExpect(jsonPath("$.widgets[1].id").value("recent-activities"))
                .andExpect(jsonPath("$.widgets[1].title").value("Recent Activities"))
                .andExpect(jsonPath("$.widgets[1].type").value("timeline"))
                .andExpect(jsonPath("$.widgets[1].size").value("medium"))
                .andExpect(jsonPath("$.widgets[1].data").exists())
                .andExpect(jsonPath("$.widgets[2].id").value("attendance-chart"))
                .andExpect(jsonPath("$.widgets[2].title").value("Weekly Attendance"))
                .andExpect(jsonPath("$.widgets[2].type").value("chart"))
                .andExpect(jsonPath("$.widgets[2].chartType").value("line"))
                .andExpect(jsonPath("$.widgets[2].size").value("medium"))
                .andExpect(jsonPath("$.widgets[2].data.labels[0]").value("Mon"))
                .andExpect(jsonPath("$.widgets[2].data.values[0]").value(98.2))
                .andExpect(jsonPath("$.widgets[3].id").value("top-performers"))
                .andExpect(jsonPath("$.widgets[3].title").value("Top Performing Classes"))
                .andExpect(jsonPath("$.widgets[3].type").value("list"))
                .andExpect(jsonPath("$.widgets[3].size").value("small"))
                .andExpect(jsonPath("$.widgets[3].data[0].name").value("12A"))
                .andExpect(jsonPath("$.widgets[3].data[0].score").value(87.5))
                .andExpect(jsonPath("$.widgets[4].id").value("system-health"))
                .andExpect(jsonPath("$.widgets[4].title").value("System Health"))
                .andExpect(jsonPath("$.widgets[4].type").value("health"))
                .andExpect(jsonPath("$.widgets[4].size").value("small"))
                .andExpect(jsonPath("$.widgets[4].data.database").value("UP"))
                .andExpect(jsonPath("$.widgets[4].data.backend").value("UP"))
                .andExpect(jsonPath("$.widgets[4].data.authentication").value("UP"))
                .andExpect(jsonPath("$.widgets[4].data.fileSystem").value("UP"))
                .andExpect(jsonPath("$.layout.columns").value(12))
                .andExpect(jsonPath("$.layout.rows").value("auto"))
                .andExpect(jsonPath("$.layout.responsive").value(true))
                .andExpect(jsonPath("$.timestamp").exists());

        // No mocks to verify for this endpoint
    }
}