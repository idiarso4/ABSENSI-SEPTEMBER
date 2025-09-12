import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  FlatList,
  RefreshControl,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const ReportsScreen = () => {
  const [reports, setReports] = useState([]);
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    loadReportsData();
  }, []);

  const loadReportsData = async () => {
    try {
      const [stats, ...reportList] = await Promise.all([
        apiService.getEmployeeStatistics(),
        // Add other report loading calls here
      ]);

      setStatistics(stats || {});
      setReports([
        {
          id: '1',
          title: 'Employee Distribution',
          description: 'Department-wise employee breakdown',
          icon: 'people-outline',
          type: 'chart',
          lastUpdated: '2024-09-04'
        },
        {
          id: '2',
          title: 'Payroll Summary',
          description: 'Monthly payroll reports',
          icon: 'document-text-outline',
          type: 'report',
          lastUpdated: '2024-09-04'
        },
        {
          id: '3',
          title: 'Leave Analytics',
          description: 'Employee leave patterns and trends',
          icon: 'calendar-outline',
          type: 'analytics',
          lastUpdated: '2024-09-03'
        },
        {
          id: '4',
          title: 'Performance Metrics',
          description: 'Employee performance statistics',
          icon: 'trophy-outline',
          type: 'chart',
          lastUpdated: '2024-09-02'
        },
        {
          id: '5',
          title: 'Attendance Report',
          description: 'Monthly attendance summaries',
          icon: 'time-outline',
          type: 'report',
          lastUpdated: '2024-09-04'
        },
        {
          id: '6',
          title: 'Recruitment Analytics',
          description: 'Hiring trends and statistics',
          icon: 'person-add-outline',
          type: 'analytics',
          lastUpdated: '2024-09-01'
        }
      ]);
    } catch (error) {
      console.log('Error loading reports:', error);
      setStatistics({
        totalEmployees: 150,
        departments: 5,
        designations: 12,
        activeEmployees: 142
      });
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadReportsData();
    setRefreshing(false);
  };

  const getIconColor = (type) => {
    switch (type) {
      case 'chart': return '#007bff';
      case 'report': return '#28a745';
      case 'analytics': return '#ffc107';
      default: return '#6c757d';
    }
  };

  const renderStatisticCard = (icon, title, value) => (
    <View style={styles.statCard}>
      <Icon name={icon} size={24} color="#007bff" />
      <View style={styles.statContent}>
        <Text style={styles.statValue}>{value || '0'}</Text>
        <Text style={styles.statTitle}>{title}</Text>
      </View>
    </View>
  );

  const renderReportItem = ({ item }) => (
    <TouchableOpacity
      style={styles.reportCard}
      onPress={() => handleReportPress(item)}
    >
      <View style={styles.reportCardLeft}>
        <View style={[styles.reportIconContainer, { borderColor: getIconColor(item.type) }]}>
          <Icon name={item.icon} size={24} color={getIconColor(item.type)} />
        </View>
        <View style={styles.reportContent}>
          <Text style={styles.reportTitle}>{item.title}</Text>
          <Text style={styles.reportDescription}>{item.description}</Text>
          <Text style={styles.reportDate}>
            Updated: {item.lastUpdated}
          </Text>
        </View>
      </View>
      <View style={styles.reportCardRight}>
        <Icon name="chevron-forward" size={20} color="#ccc" />
      </View>
    </TouchableOpacity>
  );

  const handleReportPress = (report) => {
    // Handle navigation to specific report screens
    console.log('Navigate to report:', report.title);
  };

  const exportReport = (reportType) => {
    switch (reportType) {
      case 'employee_list':
        // Export employee list
        break;
      case 'payroll':
        // Export payroll data
        break;
      case 'leave':
        // Export leave data
        break;
      default:
        break;
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('id-ID');
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <Icon name="document-outline" size={60} color="#007bff" />
          <Text style={styles.loadingText}>Loading Reports...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>HR Reports</Text>
        <Text style={styles.headerSubtitle}>Analytics & Insights</Text>
      </View>

      <ScrollView
        style={styles.scrollView}
        showsVerticalScrollIndicator={false}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Quick Statistics */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Statistics Overview</Text>
          <View style={styles.statsGrid}>
            {renderStatisticCard('people-outline', 'Total Employees', statistics?.totalEmployees)}
            {renderStatisticCard('business-outline', 'Departments', statistics?.departments)}
          </View>
          <View style={styles.statsGrid}>
            {renderStatisticCard('medal-outline', 'Designations', statistics?.designations)}
            {renderStatisticCard('person-outline', 'Active Employees', statistics?.activeEmployees)}
          </View>
        </View>

        {/* Quick Actions */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Quick Export</Text>
          <View style={styles.quickActions}>
            <TouchableOpacity
              style={[styles.actionButton, { backgroundColor: '#007bff' }]}
              onPress={() => exportReport('employee_list')}
            >
              <Icon name="download-outline" size={20} color="#fff" />
              <Text style={styles.actionButtonText}>Employee List</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.actionButton, { backgroundColor: '#28a745' }]}
              onPress={() => exportReport('payroll')}
            >
              <Icon name="document-text-outline" size={20} color="#fff" />
              <Text style={styles.actionButtonText}>Payroll Data</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.actionButton, { backgroundColor: '#ffc107' }]}
              onPress={() => exportReport('leave')}
            >
              <Icon name="calendar-outline" size={20} color="#fff" />
              <Text style={styles.actionButtonText}>Leave Reports</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Available Reports */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Available Reports</Text>
          <FlatList
            data={reports}
            renderItem={renderReportItem}
            keyExtractor={(item) => item.id}
            showsVerticalScrollIndicator={false}
            scrollEnabled={false}
          />
        </View>

        {/* Recent Activity */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Recent Reports</Text>
          <View style={styles.activityContainer}>
            <View style={styles.activityItem}>
              <Icon name="document-outline" size={20} color="#007bff" />
              <Text style={styles.activityText}>Employee distribution report generated</Text>
            </View>
            <View style={styles.activityItem}>
              <Icon name="trending-up-outline" size={20} color="#28a745" />
              <Text style={styles.activityText}>Payroll analytics updated</Text>
            </View>
            <View style={styles.activityItem}>
              <Icon name="time-outline" size={20} color="#ffc107" />
              <Text style={styles.activityText}>Leave patterns analysis completed</Text>
            </View>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  header: {
    backgroundColor: '#007bff',
    padding: 20,
    paddingTop: 30,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 5,
  },
  headerSubtitle: {
    fontSize: 14,
    color: '#e3f2fd',
  },
  scrollView: {
    flex: 1,
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
  statsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  statCard: {
    flex: 1,
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    marginHorizontal: 5,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    flexDirection: 'row',
    alignItems: 'center',
  },
  statContent: {
    marginLeft: 15,
    flex: 1,
  },
  statValue: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
  },
  statTitle: {
    fontSize: 12,
    color: '#666',
    marginTop: 2,
  },
  quickActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  actionButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#007bff',
    borderRadius: 10,
    padding: 15,
    marginHorizontal: 5,
  },
  actionButtonText: {
    color: '#fff',
    fontWeight: 'bold',
    marginLeft: 8,
  },
  reportCard: {
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
    justifyContent: 'space-between',
  },
  reportCardLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  reportIconContainer: {
    width: 50,
    height: 50,
    borderRadius: 10,
    borderWidth: 2,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 15,
  },
  reportContent: {
    flex: 1,
  },
  reportTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 2,
  },
  reportDescription: {
    fontSize: 14,
    color: '#666',
    marginBottom: 4,
  },
  reportDate: {
    fontSize: 12,
    color: '#999',
  },
  reportCardRight: {
    marginLeft: 15,
  },
  activityContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.08,
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

export default ReportsScreen;