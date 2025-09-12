import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

class ApiService {
  constructor() {
    this.baseURL = 'http://localhost:8080/api'; // Update with your backend URL
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

  // Employee Management
  async getEmployees(params = {}) {
    try {
      const response = await this.client.get('/employees', { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch employees';
    }
  }

  async getEmployeeById(id) {
    try {
      const response = await this.client.get(`/employees/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch employee';
    }
  }

  async createEmployee(employeeData) {
    try {
      const response = await this.client.post('/employees', employeeData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create employee';
    }
  }

  async updateEmployee(id, employeeData) {
    try {
      const response = await this.client.put(`/employees/${id}`, employeeData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update employee';
    }
  }

  async deleteEmployee(id) {
    try {
      const response = await this.client.delete(`/employees/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete employee';
    }
  }

  // Departments
  async getDepartments() {
    try {
      const response = await this.client.get('/departments');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch departments';
    }
  }

  // Designations
  async getDesignations() {
    try {
      const response = await this.client.get('/designations');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch designations';
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

  // Dashboard
  async getDashboardData() {
    try {
      const response = await this.client.get('/dashboard');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch dashboard data';
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