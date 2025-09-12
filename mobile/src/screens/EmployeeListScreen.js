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
} from 'react-native';
import Icon from '@expo/vector-icons/Ionicons';
import { apiService } from '../services/apiService';

const EmployeeListScreen = ({ navigation }) => {
  const [employees, setEmployees] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [departments, setDepartments] = useState([]);
  const [selectedDepartment, setSelectedDepartment] = useState(null);

  useEffect(() => {
    loadEmployees();
    loadDepartments();
  }, []);

  useEffect(() => {
    filterEmployees();
  }, [employees, searchQuery, selectedDepartment]);

  const loadEmployees = async () => {
    try {
      const data = await apiService.getEmployees();
      setEmployees(data.content || data);
    } catch (error) {
      Alert.alert('Error', 'Failed to load employees');
      console.log('Load employees error:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadDepartments = async () => {
    try {
      const data = await apiService.getDepartments();
      setDepartments(data);
    } catch (error) {
      console.log('Load departments error:', error);
    }
  };

  const filterEmployees = () => {
    let filtered = employees;

    // Apply search filter
    if (searchQuery.trim()) {
      filtered = filtered.filter(employee =>
        employee.firstName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        employee.lastName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        employee.email?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        employee.employeeId?.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Apply department filter
    if (selectedDepartment) {
      filtered = filtered.filter(employee =>
        employee.department?.id === selectedDepartment
      );
    }

    setFilteredEmployees(filtered);
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadEmployees();
    setRefreshing(false);
  };

  const handleEmployeePress = (employee) => {
    navigation.navigate('EmployeeDetail', { employee });
  };

  const renderEmployeeItem = ({ item }) => (
    <TouchableOpacity
      style={styles.employeeCard}
      onPress={() => handleEmployeePress(item)}
    >
      <View style={styles.employeeAvatar}>
        <Icon name="person-outline" size={24} color="#007bff" />
      </View>
      <View style={styles.employeeInfo}>
        <Text style={styles.employeeName}>
          {item.firstName} {item.lastName}
        </Text>
        <Text style={styles.employeeDetails}>
          ID: {item.employeeId}
        </Text>
        <Text style={styles.employeeDetails}>
          {item.designation?.name} - {item.department?.name}
        </Text>
        <Text style={styles.employeeEmail}>
          {item.email}
        </Text>
      </View>
      <View style={styles.employeeStatus}>
        <View style={[styles.statusIndicator, { backgroundColor: '#28a745' }]} />
        <Text style={styles.statusText}>Active</Text>
      </View>
    </TouchableOpacity>
  );

  const renderDepartmentFilter = () => (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      style={styles.filterScroll}
    >
      <TouchableOpacity
        style={[styles.filterChip, !selectedDepartment && styles.filterChipActive]}
        onPress={() => setSelectedDepartment(null)}
      >
        <Text style={[styles.filterChipText, !selectedDepartment && styles.filterChipTextActive]}>
          All
        </Text>
      </TouchableOpacity>
      {departments.map(dept => (
        <TouchableOpacity
          key={dept.id}
          style={[styles.filterChip, selectedDepartment === dept.id && styles.filterChipActive]}
          onPress={() => setSelectedDepartment(dept.id)}
        >
          <Text style={[styles.filterChipText, selectedDepartment === dept.id && styles.filterChipTextActive]}>
            {dept.name}
          </Text>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <Icon name="people-outline" size={60} color="#007bff" />
          <Text style={styles.loadingText}>Loading Employees...</Text>
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
            placeholder="Search employees..."
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

      {/* Department Filter */}
      {renderDepartmentFilter()}

      {/* Employee Count */}
      <View style={styles.countContainer}>
        <Text style={styles.countText}>
          {filteredEmployees.length} Employee{filteredEmployees.length !== 1 ? 's' : ''}
        </Text>
      </View>

      {/* Employee List */}
      <FlatList
        data={filteredEmployees}
        renderItem={renderEmployeeItem}
        keyExtractor={(item) => item.id.toString()}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        contentContainerStyle={styles.listContainer}
        showsVerticalScrollIndicator={false}
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Icon name="person-outline" size={50} color="#ccc" />
            <Text style={styles.emptyText}>No employees found</Text>
          </View>
        }
      />

      {/* Add Employee Button */}
      <TouchableOpacity style={styles.addButton}>
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
  employeeCard: {
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
  employeeAvatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#e9ecef',
    alignItems: 'center',
    justifyContent: 'center',
  },
  employeeInfo: {
    flex: 1,
    marginLeft: 15,
  },
  employeeName: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 2,
  },
  employeeDetails: {
    fontSize: 14,
    color: '#666',
    marginBottom: 1,
  },
  employeeEmail: {
    fontSize: 12,
    color: '#999',
  },
  employeeStatus: {
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

export default EmployeeListScreen;