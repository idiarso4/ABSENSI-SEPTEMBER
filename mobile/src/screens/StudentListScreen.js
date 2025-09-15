import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  TextInput,
  RefreshControl,
  SafeAreaView,
  Alert,
  ScrollView,
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const StudentListScreen = ({ navigation }) => {
  const [students, setStudents] = useState([]);
  const [filteredStudents, setFilteredStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [classRooms, setClassRooms] = useState([]);
  const [selectedClassRoom, setSelectedClassRoom] = useState(null);

  useEffect(() => {
    loadStudents();
    loadClassRooms();
  }, []);

  useEffect(() => {
    filterStudents();
  }, [students, searchQuery, selectedClassRoom]);

  const loadStudents = async () => {
    try {
      const data = await apiService.getStudents();
      setStudents(data.content || data);
    } catch (error) {
      Alert.alert('Error', 'Failed to load students');
      console.log('Load students error:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadClassRooms = async () => {
    try {
      const data = await apiService.getClassRooms();
      setClassRooms(data.content || data);
    } catch (error) {
      console.log('Load class rooms error:', error);
    }
  };

  const filterStudents = () => {
    let filtered = students;

    // Apply search filter
    if (searchQuery.trim()) {
      filtered = filtered.filter(student =>
        student.namaLengkap?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        student.nis?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        student.email?.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Apply class room filter
    if (selectedClassRoom) {
      filtered = filtered.filter(student =>
        student.classRoom?.id === selectedClassRoom
      );
    }

    setFilteredStudents(filtered);
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadStudents();
    setRefreshing(false);
  };

  const handleStudentPress = (student) => {
    navigation.navigate('StudentDetail', { student });
  };

  const renderStudentItem = ({ item }) => (
    <TouchableOpacity
      style={styles.studentCard}
      onPress={() => handleStudentPress(item)}
    >
      <View style={styles.studentAvatar}>
        <Icon name="school-outline" size={24} color="#007bff" />
      </View>
      <View style={styles.studentInfo}>
        <Text style={styles.studentName}>
          {item.namaLengkap}
        </Text>
        <Text style={styles.studentDetails}>
          NIS: {item.nis}
        </Text>
        <Text style={styles.studentDetails}>
          Class: {item.classRoom?.name || 'Not Assigned'}
        </Text>
        <Text style={styles.studentEmail}>
          {item.email}
        </Text>
      </View>
      <View style={styles.studentStatus}>
        <View style={[styles.statusIndicator, {
          backgroundColor: item.status === 'ACTIVE' ? '#28a745' : '#ffc107'
        }]} />
        <Text style={styles.statusText}>
          {item.status === 'ACTIVE' ? 'Active' : item.status}
        </Text>
      </View>
    </TouchableOpacity>
  );

  const renderClassRoomFilter = () => (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      style={styles.filterScroll}
    >
      <TouchableOpacity
        style={[styles.filterChip, !selectedClassRoom && styles.filterChipActive]}
        onPress={() => setSelectedClassRoom(null)}
      >
        <Text style={[styles.filterChipText, !selectedClassRoom && styles.filterChipTextActive]}>
          All Classes
        </Text>
      </TouchableOpacity>
      {classRooms.map(classRoom => (
        <TouchableOpacity
          key={classRoom.id}
          style={[styles.filterChip, selectedClassRoom === classRoom.id && styles.filterChipActive]}
          onPress={() => setSelectedClassRoom(classRoom.id)}
        >
          <Text style={[styles.filterChipText, selectedClassRoom === classRoom.id && styles.filterChipTextActive]}>
            {classRoom.name}
          </Text>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
           <Icon name="school-outline" size={60} color="#007bff" />
           <Text style={styles.loadingText}>Loading Students...</Text>
         </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      {/* Search Bar */}
      <View style={styles.searchContainer}>
        <View style={styles.searchInputContainer}>
          <Icon name="search-outline" size={20} color="#666" style={styles.searchIcon} />
          <TextInput
            style={styles.searchInput}
            placeholder="Search students..."
            value={searchQuery}
            onChangeText={setSearchQuery}
          />
          {searchQuery ? (
            <TouchableOpacity onPress={() => setSearchQuery('')}>
              <Icon name="close-circle" size={20} color="#666" />
            </TouchableOpacity>
          ) : null}
        </View>
      </View>

      {/* Class Room Filter */}
      {renderClassRoomFilter()}

      {/* Student Count */}
      <View style={styles.countContainer}>
        <Text style={styles.countText}>
          {filteredStudents.length} Student{filteredStudents.length !== 1 ? 's' : ''}
        </Text>
      </View>

      {/* Student List */}
      <FlatList
        data={filteredStudents}
        renderItem={renderStudentItem}
        keyExtractor={(item) => item.id.toString()}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        contentContainerStyle={styles.listContainer}
        showsVerticalScrollIndicator={false}
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Icon name="school-outline" size={50} color="#ccc" />
            <Text style={styles.emptyText}>No students found</Text>
          </View>
        }
      />

      {/* Add Student Button */}
      <TouchableOpacity style={styles.addButton} onPress={() => Alert.alert('Coming soon', 'Add Student form will be available soon')}>
        <Icon name="add" size={24} color="#fff" />
      </TouchableOpacity>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  searchContainer: {
    padding: 15,
    backgroundColor: '#fff',
  },
  searchInputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
    borderRadius: 10,
    paddingHorizontal: 15,
    height: 45,
  },
  searchIcon: {
    marginRight: 10,
  },
  searchInput: {
    flex: 1,
    fontSize: 16,
    color: '#333',
  },
  filterScroll: {
    paddingHorizontal: 15,
    paddingVertical: 10,
  },
  filterChip: {
    backgroundColor: '#f8f9fa',
    borderRadius: 20,
    paddingHorizontal: 15,
    paddingVertical: 8,
    marginRight: 10,
    borderWidth: 1,
    borderColor: '#e9ecef',
  },
  filterChipActive: {
    backgroundColor: '#007bff',
    borderColor: '#007bff',
  },
  filterChipText: {
    fontSize: 14,
    color: '#666',
  },
  filterChipTextActive: {
    color: '#fff',
    fontWeight: 'bold',
  },
  countContainer: {
    paddingHorizontal: 15,
    paddingVertical: 10,
  },
  countText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
  },
  listContainer: {
    padding: 15,
    paddingBottom: 80, // Extra padding for FAB
  },
  studentCard: {
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
  },
  studentAvatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#e9ecef',
    alignItems: 'center',
    justifyContent: 'center',
  },
  studentInfo: {
    flex: 1,
    marginLeft: 15,
  },
  studentName: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 2,
  },
  studentDetails: {
    fontSize: 14,
    color: '#666',
    marginBottom: 1,
  },
  studentEmail: {
    fontSize: 12,
    color: '#999',
  },
  studentStatus: {
    alignItems: 'center',
  },
  statusIndicator: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginBottom: 2,
  },
  statusText: {
    fontSize: 10,
    color: '#666',
  },
  emptyContainer: {
    alignItems: 'center',
    padding: 50,
  },
  emptyText: {
    fontSize: 16,
    color: '#666',
    marginTop: 10,
  },
  addButton: {
    position: 'absolute',
    right: 20,
    bottom: 20,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#007bff',
    alignItems: 'center',
    justifyContent: 'center',
    elevation: 5,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
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

export default StudentListScreen;