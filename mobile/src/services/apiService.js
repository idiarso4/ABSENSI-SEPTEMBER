import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Platform } from 'react-native';

class ApiService {
  constructor() {
    // Determine host for emulator/simulator
    const host = Platform.OS === 'android' ? '10.0.2.2' : 'localhost';
    this.baseURL = `http://${host}:8080/api`; // Adjust if your backend runs elsewhere
    this.client = axios.create({
      baseURL: this.baseURL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add response interceptor to handle common errors
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          // Token expired, remove it and redirect to login
          AsyncStorage.removeItem('userToken');
          this.authToken = null;
        }
        return Promise.reject(error);
      }
    );
  }

  setAuthToken(token) {
    this.authToken = token;
    if (token) {
      this.client.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      delete this.client.defaults.headers.common['Authorization'];
    }
  }

  // Authentication
  async login(credentials) {
    try {
      const response = await this.client.post('/auth/login', credentials);
      const { token } = response.data;
      this.setAuthToken(token);
      await AsyncStorage.setItem('userToken', token);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Login failed';
    }
  }

  // Student Management
  async getStudents(params = {}) {
    try {
      const response = await this.client.get('/students', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch students';
    }
  }

  async getStudentById(id) {
    try {
      const response = await this.client.get(`/students/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch student';
    }
  }

  async getStudentByNis(nis) {
    try {
      const response = await this.client.get(`/students/nis/${nis}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch student';
    }
  }

  async createStudent(studentData) {
    try {
      const response = await this.client.post('/students', studentData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create student';
    }
  }

  async updateStudent(id, studentData) {
    try {
      const response = await this.client.put(`/students/${id}`, studentData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update student';
    }
  }

  async deleteStudent(id) {
    try {
      const response = await this.client.delete(`/students/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete student';
    }
  }

  async getStudentStatistics() {
    try {
      const response = await this.client.get('/students/statistics');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch student statistics';
    }
  }

  // Teacher Management
  async getTeachers(params = {}) {
    try {
      const response = await this.client.get('/teachers', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch teachers';
    }
  }

  async getTeacherById(id) {
    try {
      const response = await this.client.get(`/teachers/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch teacher';
    }
  }

  async createTeacher(teacherData) {
    try {
      const response = await this.client.post('/teachers', teacherData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create teacher';
    }
  }

  async updateTeacher(id, teacherData) {
    try {
      const response = await this.client.put(`/teachers/${id}`, teacherData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update teacher';
    }
  }

  async deleteTeacher(id) {
    try {
      const response = await this.client.delete(`/teachers/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete teacher';
    }
  }

  // Class Room Management
  async getClassRooms(params = {}) {
    try {
      const response = await this.client.get('/classrooms', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch class rooms';
    }
  }

  async getClassRoomById(id) {
    try {
      const response = await this.client.get(`/classrooms/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch class room';
    }
  }

  async createClassRoom(classRoomData) {
    try {
      const response = await this.client.post('/classrooms', classRoomData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create class room';
    }
  }

  async updateClassRoom(id, classRoomData) {
    try {
      const response = await this.client.put(`/classrooms/${id}`, classRoomData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update class room';
    }
  }

  async deleteClassRoom(id) {
    try {
      const response = await this.client.delete(`/classrooms/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete class room';
    }
  }

  // Subject Management
  async getSubjects(params = {}) {
    try {
      const response = await this.client.get('/subjects', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch subjects';
    }
  }

  async getSubjectById(id) {
    try {
      const response = await this.client.get(`/subjects/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch subject';
    }
  }

  async createSubject(subjectData) {
    try {
      const response = await this.client.post('/subjects', subjectData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create subject';
    }
  }

  async updateSubject(id, subjectData) {
    try {
      const response = await this.client.put(`/subjects/${id}`, subjectData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update subject';
    }
  }

  async deleteSubject(id) {
    try {
      const response = await this.client.delete(`/subjects/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete subject';
    }
  }

  // Attendance Management
  async getAttendance(params = {}) {
    try {
      const response = await this.client.get('/attendance', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch attendance';
    }
  }

  async markAttendance(attendanceData) {
    try {
      const response = await this.client.post('/attendance', attendanceData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to mark attendance';
    }
  }

  // Payroll
  async getPayroll(employeeId) {
    try {
      const response = await this.client.get(`/payroll/employee/${employeeId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch payroll';
    }
  }

  // Leave Management
  async getLeaves(employeeId) {
    try {
      const response = await this.client.get(`/leaves/employee/${employeeId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch leaves';
    }
  }

  async requestLeave(leaveData) {
    try {
      const response = await this.client.post('/leaves', leaveData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to submit leave request';
    }
  }

  // Dashboard - Aggregate data from multiple endpoints
  async getDashboardData() {
    try {
      // Fetch statistics from multiple endpoints simultaneously
      const [studentStats, teacherStats, classStats, subjectStats] = await Promise.all([
        this.client.get('/students/statistics').then(r => r.data).catch(() => ({ totalStudents: 0, activeStudents: 0 })),
        this.client.get('/teachers/stats').then(r => r.data).catch(() => ({ totalTeachers: 0, activeTeachers: 0 })),
        this.client.get('/classrooms/stats').then(r => r.data).catch(() => ({ totalClassrooms: 0, activeClassrooms: 0 })),
        this.client.get('/subjects/statistics').then(r => r.data).catch(() => ({ totalCount: 0, activeCount: 0 }))
      ]);

      // Aggregate the data
      const dashboardData = {
        totalStudents: studentStats.totalStudents || studentStats.totalCount || 0,
        activeStudents: studentStats.activeStudents || 0,
        newStudentsThisMonth: studentStats.newStudentsThisMonth || 0,
        totalTeachers: teacherStats.totalTeachers || 0,
        activeTeachers: teacherStats.activeTeachers || 0,
        newTeachersThisMonth: teacherStats.recentTeachers || 0,
        totalClassrooms: classStats.totalClassrooms || classStats.totalCount || 0,
        activeClassrooms: classStats.activeClassrooms || 0,
        totalSubjects: subjectStats.totalCount || 0,
        activeSubjects: subjectStats.activeCount || 0,
        // Recent activity placeholders
        recentActivities: [
          { type: 'student', message: `${studentStats.newStudentsThisMonth || 0} new students this month`, color: '#28a745' },
          { type: 'teacher', message: `${teacherStats.recentTeachers || 0} new teachers this month`, color: '#007bff' },
          { type: 'enrollment', message: 'Student enrollment completed', color: '#17a2b8' }
        ]
      };

      return dashboardData;
    } catch (error) {
      // Fallback to default data
      return {
        totalStudents: 0,
        activeStudents: 0,
        newStudentsThisMonth: 0,
        totalTeachers: 0,
        activeTeachers: 0,
        newTeachersThisMonth: 0,
        totalClassrooms: 0,
        activeClassrooms: 0,
        totalSubjects: 0,
        activeSubjects: 0,
        recentActivities: []
      };
    }
  }

  // Individual statistics methods
  async getStudentStatistics() {
    try {
      const response = await this.client.get('/students/statistics');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch student statistics';
    }
  }

  async getTeacherStatistics() {
    try {
      const response = await this.client.get('/teachers/stats');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch teacher statistics';
    }
  }

  async getClassRoomStatistics() {
    try {
      const response = await this.client.get('/classrooms/stats');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch classroom statistics';
    }
  }

  async getSubjectStatistics() {
    try {
      const response = await this.client.get('/subjects/statistics');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch subject statistics';
    }
  }

  // Reports
  async generatePaySlip(employeeId, period) {
    try {
      const response = await this.client.get('/reports/payslip', {
        params: { employeeId, period },
        responseType: 'blob'
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to generate payslip';
    }
  }

  // Advanced Search
  async searchEmployees(searchParams) {
    try {
      const response = await this.client.get('/reports/search-employees', {
        params: searchParams
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Search failed';
    }
  }

  // Employee Statistics
  async getEmployeeStatistics() {
    try {
      const response = await this.client.get('/reports/employee-statistics');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch statistics';
    }
  }
}

// Create singleton instance
export const apiService = new ApiService();