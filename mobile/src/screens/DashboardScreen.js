import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  RefreshControl,
  SafeAreaView,
  Alert,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const DashboardScreen = ({ navigation }) => {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const data = await apiService.getDashboardData();
      setDashboardData(data);
    } catch (error) {
      console.log('Dashboard data error:', error);
      // Provide basic user feedback on network error
      try { Alert.alert('Network Error', 'Failed to load dashboard data. Showing cached/default data.'); } catch (_) {}
      // Set default data if API fails
      setDashboardData({
        totalEmployees: 150,
        newHiresThisMonth: 12,
        employeesOnLeave: 8,
        pendingLeaveRequests: 5,
        departments: [
          { name: 'Engineering', count: 45 },
          { name: 'HR', count: 12 },
          { name: 'Finance', count: 8 },
          { name: 'Marketing', count: 15 },
          { name: 'Operations', count: 25 },
        ]
      });
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadDashboardData();
    setRefreshing(false);
  };

  const renderStatCard = (icon, title, value, color = '#007bff') => (
    <View style={[styles.statCard, { borderLeftColor: color }]}>
      <Icon name={icon} size={24} color={color} />
      <View style={styles.statContent}>
        <Text style={styles.statValue}>{value}</Text>
        <Text style={styles.statTitle}>{title}</Text>
      </View>
    </View>
  );

  const renderDepartmentCard = (department) => (
    <View key={department.name} style={styles.departmentCard}>
      <Text style={styles.departmentName}>{department.name}</Text>
      <Text style={styles.departmentCount}>{department.count} employees</Text>
    </View>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <Icon name="business-outline" size={60} color="#007bff" />
          <Text style={styles.loadingText}>Loading Dashboard...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        style={styles.scrollView}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Header */}
        <View style={styles.header}>
          <Icon name="business-outline" size={30} color="#007bff" />
          <Text style={styles.headerTitle}>HR Dashboard</Text>
          <Text style={styles.headerSubtitle}>PT. COCOK GAN</Text>
        </View>

        {/* Quick Stats */}
        <View style={styles.statsContainer}>
          {renderStatCard('people-outline', 'Total Employees', dashboardData?.totalEmployees || 0)}
          {renderStatCard('person-add-outline', 'New Hires (This Month)', dashboardData?.newHiresThisMonth || 0, '#28a745')}
          {renderStatCard('airplane-outline', 'On Leave', dashboardData?.employeesOnLeave || 0, '#ffc107')}
          {renderStatCard('document-text-outline', 'Pending Requests', dashboardData?.pendingLeaveRequests || 0, '#dc3545')}
        </View>

        {/* Department Overview */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Department Overview</Text>
          <View style={styles.departmentContainer}>
            {dashboardData?.departments?.map(renderDepartmentCard) || []}
          </View>
        </View>

        {/* Quick Actions */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Quick Actions</Text>
          <View style={styles.actionsContainer}>
            <TouchableOpacity
              style={styles.actionButton}
              onPress={() => navigation.navigate('Employees')}
            >
              <Icon name="people-outline" size={24} color="#007bff" />
              <Text style={styles.actionButtonText}>Manage Employees</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.actionButton}
              onPress={() => navigation.navigate('Reports')}
            >
              <Icon name="document-text-outline" size={24} color="#28a745" />
              <Text style={styles.actionButtonText}>View Reports</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.actionButton}
              onPress={() => navigation.navigate('Profile')}
            >
              <Icon name="person-outline" size={24} color="#ffc107" />
              <Text style={styles.actionButtonText}>Profile</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Recent Activity */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Recent Activity</Text>
          <View style={styles.activityContainer}>
            <View style={styles.activityItem}>
              <Icon name="person-add-outline" size={20} color="#28a745" />
              <Text style={styles.activityText}>3 new employees onboarded this week</Text>
            </View>
            <View style={styles.activityItem}>
              <Icon name="document-outline" size={20} color="#007bff" />
              <Text style={styles.activityText}>15 payroll reports generated</Text>
            </View>
            <View style={styles.activityItem}>
              <Icon name="checkmark-circle-outline" size={20} color="#28a745" />
              <Text style={styles.activityText}>5 leave requests approved</Text>
            </View>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

// Note: TouchableOpacity was not imported, adding it to the imports
import { TouchableOpacity } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  scrollView: {
    flex: 1,
  },
  header: {
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#007bff',
    marginTop: 10,
  },
  headerSubtitle: {
    fontSize: 14,
    color: '#666',
    marginTop: 5,
  },
  statsContainer: {
    padding: 15,
  },
  statCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    borderLeftWidth: 4,
  },
  statContent: {
    marginLeft: 15,
    flex: 1,
  },
  statValue: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
  },
  statTitle: {
    fontSize: 14,
    color: '#666',
    marginTop: 2,
  },
  section: {
    padding: 15,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 15,
  },
  departmentContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  departmentCard: {
    width: '48%',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    alignItems: 'center',
  },
  departmentName: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 5,
  },
  departmentCount: {
    fontSize: 12,
    color: '#666',
  },
  actionsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
  actionButton: {
    alignItems: 'center',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    width: '30%',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  actionButtonText: {
    fontSize: 12,
    color: '#333',
    marginTop: 5,
    textAlign: 'center',
  },
  activityContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  activityItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  activityText: {
    fontSize: 14,
    color: '#333',
    marginLeft: 10,
    flex: 1,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: 10,
    fontSize: 16,
    color: '#666',
  },
});

export default DashboardScreen;