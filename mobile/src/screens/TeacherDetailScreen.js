import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  Alert,
  Linking,
  ActivityIndicator,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const TeacherDetailScreen = ({ route, navigation }) => {
  const [teacher, setTeacher] = useState(route.params?.teacher || {});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // If no teacher in params, fetch by ID
    if (!route.params?.teacher?.id && route.params?.id) {
      loadTeacherDetails(route.params.id);
    }
  }, []);

  const loadTeacherDetails = async (id) => {
    setLoading(true);
    try {
      const data = await apiService.getTeacherById(id);
      setTeacher(data);
    } catch (error) {
      Alert.alert('Error', 'Failed to load teacher details');
    } finally {
      setLoading(false);
    }
  };

  const callTeacher = async () => {
    const phone = (teacher.phone || '').toString().trim();
    if (!phone) {
      Alert.alert('Info', 'No phone number available');
      return;
    }
    const url = `tel:${phone}`;
    try {
      const supported = await Linking.canOpenURL(url);
      if (supported) {
        await Linking.openURL(url);
      } else {
        Alert.alert('Error', 'Calling is not supported on this device');
      }
    } catch (e) {
      Alert.alert('Error', 'Failed to initiate call');
    }
  };

  const emailTeacher = async () => {
    const email = (teacher.email || '').toString().trim();
    if (!email) {
      Alert.alert('Info', 'No email address available');
      return;
    }
    const url = `mailto:${email}`;
    try {
      const supported = await Linking.canOpenURL(url);
      if (supported) {
        await Linking.openURL(url);
      } else {
        Alert.alert('Error', 'Email is not supported on this device');
      }
    } catch (e) {
      Alert.alert('Error', 'Failed to open email client');
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
          <ActivityIndicator size="large" color="#007bff" />
          <Text style={styles.loadingText}>Loading teacher...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      {/* Header with Actions */}
      <View style={styles.header}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          style={styles.backButton}
        >
          <Icon name="arrow-back" size={24} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{teacher.name}</Text>
        <View style={styles.headerActions}>
          <TouchableOpacity onPress={callTeacher} style={styles.actionButton}>
            <Icon name="call" size={20} color="#fff" />
          </TouchableOpacity>
          <TouchableOpacity onPress={emailTeacher} style={styles.actionButton}>
            <Icon name="mail" size={20} color="#fff" />
          </TouchableOpacity>
        </View>
      </View>

      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Profile Section */}
        <View style={styles.profileCard}>
          <View style={styles.avatarContainer}>
            <Icon name="person-outline" size={60} color="#007bff" />
          </View>
          <Text style={styles.teacherName}>
            {teacher.name}
          </Text>
          <Text style={styles.teacherNip}>NIP: {teacher.nip}</Text>
          <View style={styles.statusBadge}>
            <View style={styles.statusDot} />
            <Text style={styles.statusText}>{teacher.isActive ? 'Active' : 'Inactive'}</Text>
          </View>
        </View>

        {/* Employment Information */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Teacher Information</Text>

          <View style={styles.infoGrid}>
            <View style={styles.infoItem}>
              <Icon name="school-outline" size={20} color="#666" />
              <Text style={styles.infoLabel}>User Type</Text>
              <Text style={styles.infoValue}>
                {teacher.userType || 'TEACHER'}
              </Text>
            </View>

            <View style={styles.infoItem}>
              <Icon name="calendar-outline" size={20} color="#666" />
              <Text style={styles.infoLabel}>Join Date</Text>
              <Text style={styles.infoValue}>
                {formatDate(teacher.createdAt)}
              </Text>
            </View>

            <View style={styles.infoItem}>
              <Icon name="time-outline" size={20} color="#666" />
              <Text style={styles.infoLabel}>Last Updated</Text>
              <Text style={styles.infoValue}>
                {formatDate(teacher.updatedAt)}
              </Text>
            </View>

            <View style={styles.infoItem}>
              <Icon name="checkmark-circle-outline" size={20} color="#666" />
              <Text style={styles.infoLabel}>Status</Text>
              <Text style={styles.infoValue}>
                {teacher.isActive ? 'Active' : 'Inactive'}
              </Text>
            </View>
          </View>
        </View>

        {/* Personal Information */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Contact Information</Text>

          <View style={styles.infoRow}>
            <View style={styles.infoItemHorizontal}>
              <Icon name="mail-outline" size={16} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Email</Text>
                <Text style={styles.infoValue}>{teacher.email || 'N/A'}</Text>
              </View>
            </View>

            <View style={styles.infoItemHorizontal}>
              <Icon name="call-outline" size={16} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Phone</Text>
                <Text style={styles.infoValue}>{teacher.phone || 'N/A'}</Text>
              </View>
            </View>

            <View style={styles.infoItemHorizontal}>
              <Icon name="location-outline" size={16} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Address</Text>
                <Text style={styles.infoValue}>
                  {teacher.address || 'N/A'}
                </Text>
              </View>
            </View>
          </View>
        </View>

        {/* Quick Actions */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Quick Actions</Text>
          <View style={styles.actionContainer}>
            <TouchableOpacity style={styles.quickActionButton} onPress={() => Alert.alert('Coming soon', 'View teaching schedule will be available soon')}>
              <Icon name="calendar-outline" size={24} color="#007bff" />
              <Text style={styles.quickActionText}>Schedule</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.quickActionButton} onPress={() => Alert.alert('Coming soon', 'View attendance records will be available soon')}>
              <Icon name="checkmark-circle-outline" size={24} color="#28a745" />
              <Text style={styles.quickActionText}>Attendance</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.quickActionButton} onPress={() => Alert.alert('Coming soon', 'Performance evaluation will be available soon')}>
              <Icon name="star-outline" size={24} color="#ffc107" />
              <Text style={styles.quickActionText}>Performance</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.quickActionButton} onPress={() => Alert.alert('Coming soon', 'Edit teacher profile will be available soon')}>
              <Icon name="create-outline" size={24} color="#dc3545" />
              <Text style={styles.quickActionText}>Edit Profile</Text>
            </TouchableOpacity>
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
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#007bff',
    padding: 15,
    paddingTop: 20,
  },
  backButton: {
    marginRight: 15,
  },
  headerTitle: {
    flex: 1,
    fontSize: 18,
    fontWeight: 'bold',
    color: '#fff',
  },
  headerActions: {
    flexDirection: 'row',
  },
  actionButton: {
    marginLeft: 15,
    padding: 8,
  },
  scrollView: {
    flex: 1,
  },
  profileCard: {
    backgroundColor: '#fff',
    margin: 15,
    borderRadius: 15,
    padding: 20,
    alignItems: 'center',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
  },
  avatarContainer: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#e9ecef',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 15,
  },
  teacherName: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 5,
  },
  teacherNip: {
    fontSize: 14,
    color: '#666',
    marginBottom: 10,
  },
  statusBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#e8f5e8',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 15,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: '#28a745',
    marginRight: 6,
  },
  statusText: {
    fontSize: 12,
    color: '#28a745',
    fontWeight: 'bold',
  },
  section: {
    margin: 15,
    marginTop: 0,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 15,
  },
  infoGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginHorizontal: -7.5,
  },
  infoItem: {
    width: '50%',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    margin: 7.5,
    alignItems: 'center',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  infoItemHorizontal: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  infoRow: {},
  infoContent: {
    flex: 1,
    marginLeft: 10,
  },
  infoLabel: {
    fontSize: 12,
    color: '#666',
    marginBottom: 2,
  },
  infoValue: {
    fontSize: 14,
    color: '#333',
    fontWeight: '500',
  },
  actionContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginHorizontal: -5,
  },
  quickActionButton: {
    width: '45%',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    margin: 5,
    alignItems: 'center',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  quickActionText: {
    fontSize: 12,
    color: '#333',
    marginTop: 8,
    textAlign: 'center',
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

export default TeacherDetailScreen;