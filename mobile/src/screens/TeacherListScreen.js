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

const TeacherListScreen = ({ navigation }) => {
  const [teachers, setTeachers] = useState([]);
  const [filteredTeachers, setFilteredTeachers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState(null);

  useEffect(() => {
    loadTeachers();
  }, []);

  useEffect(() => {
    filterTeachers();
  }, [teachers, searchQuery, selectedStatus]);

  const loadTeachers = async () => {
    try {
      const data = await apiService.getTeachers();
      setTeachers(data.content || data);
    } catch (error) {
      Alert.alert('Error', 'Failed to load teachers');
      console.log('Load teachers error:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterTeachers = () => {
    let filtered = teachers;

    // Apply search filter
    if (searchQuery.trim()) {
      filtered = filtered.filter(teacher =>
        teacher.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        teacher.nip?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        teacher.email?.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Apply status filter
    if (selectedStatus) {
      const statusFilter = selectedStatus === 'ACTIVE';
      filtered = filtered.filter(teacher =>
        teacher.isActive === statusFilter
      );
    }

    setFilteredTeachers(filtered);
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadTeachers();
    setRefreshing(false);
  };

  const handleTeacherPress = (teacher) => {
    navigation.navigate('TeacherDetail', { teacher });
  };

  const renderTeacherItem = ({ item }) => (
    <TouchableOpacity
      style={styles.teacherCard}
      onPress={() => handleTeacherPress(item)}
    >
      <View style={styles.teacherAvatar}>
        <Icon name="person-outline" size={24} color="#007bff" />
      </View>
      <View style={styles.teacherInfo}>
        <Text style={styles.teacherName}>{item.name}</Text>
        <Text style={styles.teacherDetails}>
          NIP: {item.nip}
        </Text>
        <Text style={styles.teacherDetails}>
          {item.email}
        </Text>
        <Text style={styles.teacherEmail}>
          Status: {item.isActive ? 'Active' : 'Inactive'}
        </Text>
      </View>
      <View style={styles.teacherStatus}>
        <View style={[styles.statusIndicator, {
          backgroundColor: item.isActive ? '#28a745' : '#6c757d'
        }]} />
        <Text style={styles.statusText}>
          {item.isActive ? 'Active' : 'Inactive'}
        </Text>
      </View>
    </TouchableOpacity>
  );

  const renderStatusFilter = () => (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      style={styles.filterScroll}
    >
      <TouchableOpacity
        style={[styles.filterChip, !selectedStatus && styles.filterChipActive]}
        onPress={() => setSelectedStatus(null)}
      >
        <Text style={[styles.filterChipText, !selectedStatus && styles.filterChipTextActive]}>
          All
        </Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={[styles.filterChip, selectedStatus === 'ACTIVE' && styles.filterChipActive]}
        onPress={() => setSelectedStatus('ACTIVE')}
      >
        <Text style={[styles.filterChipText, selectedStatus === 'ACTIVE' && styles.filterChipTextActive]}>
          Active
        </Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={[styles.filterChip, selectedStatus === 'INACTIVE' && styles.filterChipActive]}
        onPress={() => setSelectedStatus('INACTIVE')}
      >
        <Text style={[styles.filterChipText, selectedStatus === 'INACTIVE' && styles.filterChipTextActive]}>
          Inactive
        </Text>
      </TouchableOpacity>
    </ScrollView>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <Icon name="school-outline" size={60} color="#007bff" />
          <Text style={styles.loadingText}>Loading Teachers...</Text>
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
            placeholder="Search teachers..."
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

      {/* Status Filter */}
      {renderStatusFilter()}

      {/* Teacher Count */}
      <View style={styles.countContainer}>
        <Text style={styles.countText}>
          {filteredTeachers.length} Teacher{filteredTeachers.length !== 1 ? 's' : ''}
        </Text>
      </View>

      {/* Teacher List */}
      <FlatList
        data={filteredTeachers}
        renderItem={renderTeacherItem}
        keyExtractor={(item) => item.id.toString()}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        contentContainerStyle={styles.listContainer}
        showsVerticalScrollIndicator={false}
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Icon name="school-outline" size={50} color="#ccc" />
            <Text style={styles.emptyText}>No teachers found</Text>
          </View>
        }
      />

      {/* Add Teacher Button */}
      <TouchableOpacity style={styles.addButton} onPress={() => Alert.alert('Coming soon', 'Add Teacher form will be available soon')}>
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
  teacherCard: {
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
  teacherAvatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#e9ecef',
    alignItems: 'center',
    justifyContent: 'center',
  },
  teacherInfo: {
    flex: 1,
    marginLeft: 15,
  },
  teacherName: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 2,
  },
  teacherDetails: {
    fontSize: 14,
    color: '#666',
    marginBottom: 1,
  },
  teacherEmail: {
    fontSize: 12,
    color: '#999',
  },
  teacherStatus: {
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

export default TeacherListScreen;