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
        totalStudents: 450,
        activeStudents: 420,
        newStudentsThisMonth: 15,
        totalTeachers: 35,
        activeTeachers: 30,
        newTeachersThisMonth: 2,
        totalClassrooms: 25,
        activeClassrooms: 22,
        totalSubjects: 18,
        activeSubjects: 18,
        recentActivities: [
          { type: 'student', message: '15 new students this month', color: '#28a745' },
          { type: 'teacher', message: '2 new teachers this month', color: '#007bff' },
          { type: 'enrollment', message: 'Student enrollment completed', color: '#17a2b8' }
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
           <Icon name="school-outline" size={60} color="#007bff" />
           <Text style={styles.loadingText}>Loading SIM Sekolah Dashboard...</Text>
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
           <Icon name="school-outline" size={30} color="#007bff" />
           <Text style={styles.headerTitle}>SIM Sekolah Dashboard</Text>
           <Text style={styles.headerSubtitle}>Sistem Informasi Sekolah</Text>
         </View>

        {/* Quick Stats */}
        <View style={styles.statsContainer}>
           {renderStatCard('school-outline', 'Total Students', dashboardData?.totalStudents || 0)}
           {renderStatCard('person-add-outline', 'New Students (This Month)', dashboardData?.newStudentsThisMonth || 0, '#28a745')}
           {renderStatCard('people-outline', 'Total Teachers', dashboardData?.totalTeachers || 0, '#007bff')}
           {renderStatCard('business-outline', 'Active Classes', dashboardData?.activeClassrooms || 0, '#17a2b8')}
         </View>

        {/* Subject Overview */}
        <View style={styles.section}>
           <Text style={styles.sectionTitle}>Subject Overview</Text>
           <View style={styles.departmentContainer}>
             <View style={styles.departmentCard}>
               <Text style={styles.departmentName}>Total Subjects</Text>
               <Text style={styles.departmentCount}>{dashboardData?.totalSubjects || 0} subjects</Text>
             </View>
             <View style={styles.departmentCard}>
               <Text style={styles.departmentName}>Active Subjects</Text>
               <Text style={styles.departmentCount}>{dashboardData?.activeSubjects || 0} subjects</Text>
             </View>
           </View>
         </View>

        {/* Quick Actions */}
        <View style={styles.section}>
           <Text style={styles.sectionTitle}>Quick Actions</Text>
           <View style={styles.actionsContainer}>
             <TouchableOpacity
               style={styles.actionButton}
               onPress={() => navigation.navigate('Students')}
             >
               <Icon name="school-outline" size={24} color="#007bff" />
               <Text style={styles.actionButtonText}>Manage Students</Text>
             </TouchableOpacity>

             <TouchableOpacity
               style={styles.actionButton}
               onPress={() => navigation.navigate('Attendance')}
             >
               <Icon name="checkmark-circle-outline" size={24} color="#28a745" />
               <Text style={styles.actionButtonText}>Mark Attendance</Text>
             </TouchableOpacity>

             <TouchableOpacity
               style={styles.actionButton}
               onPress={() => navigation.navigate('Reports')}
             >
               <Icon name="document-text-outline" size={24} color="#17a2b8" />
               <Text style={styles.actionButtonText}>View Reports</Text>
             </TouchableOpacity>
           </View>
         </View>

        {/* Recent Activity */}
        <View style={styles.section}>
           <Text style={styles.sectionTitle}>Recent Activity</Text>
           <View style={styles.activityContainer}>
             {dashboardData?.recentActivities?.map((activity, index) => (
               <View key={index} style={styles.activityItem}>
                 <Icon name={
                   activity.type === 'student' ? 'school-outline' :
                   activity.type === 'teacher' ? 'people-outline' :
                   activity.type === 'enrollment' ? 'checkmark-circle-outline' :
                   'information-circle-outline'
                 } size={20} color={activity.color} />
                 <Text style={styles.activityText}>{activity.message}</Text>
               </View>
             )) || (
               <>
                 <View style={styles.activityItem}>
                   <Icon name="school-outline" size={20} color="#28a745" />
                   <Text style={styles.activityText}>New student enrollments processed</Text>
                 </View>
                 <View style={styles.activityItem}>
                   <Icon name="document-outline" size={20} color="#007bff" />
                   <Text style={styles.activityText}>Monthly attendance reports generated</Text>
                 </View>
                 <View style={styles.activityItem}>
                   <Icon name="checkmark-circle-outline" size={20} color="#17a2b8" />
                   <Text style={styles.activityText}>Class schedules updated</Text>
                 </View>
               </>
             )}
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