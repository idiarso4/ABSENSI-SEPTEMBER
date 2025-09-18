import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  SafeAreaView,
  Alert,
  ScrollView,
  ActivityIndicator,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const AttendanceScreen = ({ navigation }) => {
  const [teachingActivities, setTeachingActivities] = useState([]);
  const [selectedActivity, setSelectedActivity] = useState(null);
  const [students, setStudents] = useState([]);
  const [attendanceData, setAttendanceData] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [currentDate] = useState(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    loadTeachingActivities();
  }, []);

  useEffect(() => {
    if (selectedActivity) {
      loadStudentsForActivity();
    }
  }, [selectedActivity]);

  const loadTeachingActivities = async () => {
    try {
      // First try to generate today's activities
      try {
        await apiService.generateTodaysActivities();
      } catch (genError) {
        console.log('Generate activities error (might already exist):', genError);
      }

      // Then load pending activities
      const data = await apiService.getMyPendingActivities();
      setTeachingActivities(data.content || data);
    } catch (error) {
      console.log('Load teaching activities error:', error);
      Alert.alert('Error', 'Failed to load teaching activities');
    } finally {
      setLoading(false);
    }
  };

  const loadStudentsForActivity = async () => {
    if (!selectedActivity) return;

    try {
      setLoading(true);
      const studentList = await apiService.getPendingStudentsForAttendance(selectedActivity);

      // Initialize attendance data for each student
      const initialAttendance = {};
      studentList.forEach(student => {
        initialAttendance[student.id] = {
          studentId: student.id,
          status: 'PRESENT', // Default to present
          date: currentDate,
          notes: ''
        };
      });

      setStudents(studentList);
      setAttendanceData(initialAttendance);
    } catch (error) {
      console.log('Load students error:', error);
      Alert.alert('Error', 'Failed to load students for this activity');
    } finally {
      setLoading(false);
    }
  };

  const updateAttendanceStatus = (studentId, status) => {
    setAttendanceData(prev => ({
      ...prev,
      [studentId]: {
        ...prev[studentId],
        status: status
      }
    }));
  };

  const submitAttendance = async () => {
    if (!selectedActivity || students.length === 0) {
      Alert.alert('Error', 'Please select a teaching activity first');
      return;
    }

    try {
      setSaving(true);

      const attendanceRecords = Object.values(attendanceData).map(record => ({
        studentId: record.studentId,
        status: record.status,
        keterangan: record.notes || ''
      }));

      const bulkRequest = {
        teachingActivityId: selectedActivity,
        studentAttendances: attendanceRecords
      };

      // Submit bulk attendance
      await apiService.bulkMarkAttendance(bulkRequest);

      Alert.alert('Success', 'Attendance marked successfully!', [
        {
          text: 'OK',
          onPress: () => {
            // Reset for next activity
            setAttendanceData({});
            setSelectedActivity(null);
            setStudents([]);
            loadTeachingActivities(); // Refresh activities
          }
        }
      ]);
    } catch (error) {
      console.log('Submit attendance error:', error);
      Alert.alert('Error', 'Failed to submit attendance');
    } finally {
      setSaving(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PRESENT': return '#28a745';
      case 'ABSENT': return '#dc3545';
      case 'LATE': return '#ffc107';
      case 'EXCUSED': return '#17a2b8';
      default: return '#6c757d';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PRESENT': return 'checkmark-circle';
      case 'ABSENT': return 'close-circle';
      case 'LATE': return 'time';
      case 'EXCUSED': return 'medical';
      default: return 'help-circle';
    }
  };

  const renderTeachingActivityItem = ({ item }) => (
    <TouchableOpacity
      style={[
        styles.activityCard,
        selectedActivity === item.id && styles.activityCardSelected
      ]}
      onPress={() => setSelectedActivity(item.id)}
    >
      <View style={styles.activityInfo}>
        <Text style={styles.activityName}>{item.subjectName}</Text>
        <Text style={styles.activityDetails}>
          {item.classRoomName} • {item.scheduleTime}
        </Text>
        <Text style={styles.activityDetails}>
          {item.roomName || 'Room TBA'}
        </Text>
      </View>
      {selectedActivity === item.id && (
        <Icon name="checkmark-circle" size={24} color="#007bff" />
      )}
    </TouchableOpacity>
  );

  const renderStudentItem = ({ item }) => {
    const attendance = attendanceData[item.id];
    const status = attendance?.status || 'PRESENT';

    return (
      <View style={styles.studentCard}>
        <View style={styles.studentInfo}>
          <Text style={styles.studentName}>{item.namaLengkap}</Text>
          <Text style={styles.studentDetails}>NIS: {item.nis}</Text>
        </View>

        <View style={styles.attendanceButtons}>
          {['PRESENT', 'ABSENT', 'LATE', 'EXCUSED'].map((statusOption) => (
            <TouchableOpacity
              key={statusOption}
              style={[
                styles.statusButton,
                status === statusOption && {
                  backgroundColor: getStatusColor(statusOption),
                  borderColor: getStatusColor(statusOption)
                }
              ]}
              onPress={() => updateAttendanceStatus(item.id, statusOption)}
            >
              <Icon
                name={getStatusIcon(statusOption)}
                size={16}
                color={status === statusOption ? '#fff' : getStatusColor(statusOption)}
              />
              <Text style={[
                styles.statusButtonText,
                status === statusOption && styles.statusButtonTextActive
              ]}>
                {statusOption.charAt(0)}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>
    );
  };

  const renderTeachingActivitySelector = () => (
    <View style={styles.selectorContainer}>
      <Text style={styles.selectorTitle}>Select Teaching Activity</Text>
      <FlatList
        horizontal
        data={teachingActivities}
        renderItem={renderTeachingActivityItem}
        keyExtractor={(item) => item.id.toString()}
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.activityList}
        ListEmptyComponent={
          <Text style={styles.emptyText}>No pending activities available</Text>
        }
      />
    </View>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <Icon name="school-outline" size={60} color="#007bff" />
          <Text style={styles.loadingText}>Loading Attendance...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          style={styles.backButton}
        >
          <Icon name="arrow-back" size={24} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Mark Attendance</Text>
        <Text style={styles.headerDate}>{currentDate}</Text>
      </View>

      <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
        {/* Teaching Activity Selector */}
        {renderTeachingActivitySelector()}

        {/* Students List */}
        {selectedActivity && (
          <View style={styles.studentsContainer}>
            <View style={styles.studentsHeader}>
              <Text style={styles.studentsTitle}>
                Students ({students.length})
              </Text>
              <Text style={styles.legendText}>
                P: Present • A: Absent • L: Late • E: Excused
              </Text>
            </View>

            <FlatList
              data={students}
              renderItem={renderStudentItem}
              keyExtractor={(item) => item.id.toString()}
              scrollEnabled={false}
              ListEmptyComponent={
                <Text style={styles.emptyText}>No students in this class</Text>
              }
            />
          </View>
        )}

        {/* Submit Button */}
        {selectedActivity && students.length > 0 && (
          <View style={styles.submitContainer}>
            <TouchableOpacity
              style={[styles.submitButton, saving && styles.submitButtonDisabled]}
              onPress={submitAttendance}
              disabled={saving}
            >
              {saving ? (
                <ActivityIndicator color="#fff" size="small" />
              ) : (
                <>
                  <Icon name="checkmark-done" size={20} color="#fff" />
                  <Text style={styles.submitButtonText}>Submit Attendance</Text>
                </>
              )}
            </TouchableOpacity>
          </View>
        )}
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
  headerDate: {
    fontSize: 14,
    color: '#fff',
    opacity: 0.8,
  },
  content: {
    flex: 1,
  },
  selectorContainer: {
    backgroundColor: '#fff',
    margin: 15,
    borderRadius: 10,
    padding: 15,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  selectorTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 15,
  },
  activityList: {
    paddingVertical: 5,
  },
  activityCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
    borderRadius: 8,
    padding: 12,
    marginRight: 10,
    minWidth: 180,
    borderWidth: 2,
    borderColor: 'transparent',
  },
  activityCardSelected: {
    borderColor: '#007bff',
    backgroundColor: '#e7f3ff',
  },
  activityInfo: {
    flex: 1,
  },
  activityName: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#333',
  },
  activityDetails: {
    fontSize: 12,
    color: '#666',
    marginTop: 2,
  },
  studentsContainer: {
    backgroundColor: '#fff',
    margin: 15,
    marginTop: 0,
    borderRadius: 10,
    padding: 15,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  studentsHeader: {
    marginBottom: 15,
  },
  studentsTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
  },
  legendText: {
    fontSize: 12,
    color: '#666',
    marginTop: 5,
  },
  studentCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
    borderRadius: 8,
    padding: 12,
    marginBottom: 8,
  },
  studentInfo: {
    flex: 1,
  },
  studentName: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#333',
  },
  studentDetails: {
    fontSize: 12,
    color: '#666',
    marginTop: 2,
  },
  attendanceButtons: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statusButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#dee2e6',
    borderRadius: 6,
    paddingHorizontal: 8,
    paddingVertical: 6,
    marginLeft: 4,
  },
  statusButtonText: {
    fontSize: 12,
    fontWeight: 'bold',
    color: '#666',
    marginLeft: 2,
  },
  statusButtonTextActive: {
    color: '#fff',
  },
  submitContainer: {
    padding: 15,
  },
  submitButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#28a745',
    borderRadius: 10,
    padding: 15,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
  },
  submitButtonDisabled: {
    backgroundColor: '#6c757d',
  },
  submitButtonText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#fff',
    marginLeft: 8,
  },
  emptyText: {
    fontSize: 14,
    color: '#666',
    textAlign: 'center',
    padding: 20,
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

export default AttendanceScreen;