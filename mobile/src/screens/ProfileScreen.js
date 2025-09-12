import React, { useState, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  Alert,
} from 'react-native';
// AsyncStorage is managed by AuthContext
// import AsyncStorage from '@react-native-async-storage/async-storage';
import Icon from '@expo/vector-icons/Ionicons';
import { AuthContext } from '../services/AuthContext';

const ProfileScreen = ({ navigation }) => {
  const { signOut } = useContext(AuthContext);
  const [userProfile] = useState({
    name: 'HR Manager',
    email: 'hr@ptcocokgan.com',
    role: 'Human Resources',
    department: 'Human Resources',
    joinDate: '2023-01-01',
  });

  const handleLogout = async () => {
    Alert.alert(
      'Logout',
      'Are you sure you want to logout?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Logout',
          style: 'destructive',
          onPress: async () => {
            try {
              await signOut();
            } catch (error) {
              console.log('Logout error:', error);
            }
          },
        },
      ]
    );
  };

  const menuItems = [
    {
      icon: 'person-outline',
      title: 'Personal Information',
      description: 'Update your personal details',
      action: () => Alert.alert('Info', 'Personal Information feature coming soon'),
    },
    {
      icon: 'lock-closed-outline',
      title: 'Change Password',
      description: 'Update your account password',
      action: () => Alert.alert('Info', 'Change Password feature coming soon'),
    },
    {
      icon: 'notifications-outline',
      title: 'Notifications',
      description: 'Manage notification preferences',
      action: () => Alert.alert('Info', 'Notifications feature coming soon'),
    },
    {
      icon: 'information-circle-outline',
      title: 'About',
      description: 'App version and information',
      action: () => Alert.alert('About', 'HRM Mobile App v1.0.0\nPT. COCOK GAN'),
    },
    {
      icon: 'help-circle-outline',
      title: 'Help & Support',
      description: 'Get help and contact support',
      action: () => Alert.alert('Info', 'Help & Support feature coming soon'),
    },
  ];

  const renderMenuItem = (item) => (
    <TouchableOpacity
      key={item.title}
      style={styles.menuItem}
      onPress={item.action}
    >
      <View style={styles.menuItemLeft}>
        <View style={styles.menuIconContainer}>
          <Icon name={item.icon} size={24} color="#007bff" />
        </View>
        <View style={styles.menuContent}>
          <Text style={styles.menuTitle}>{item.title}</Text>
          <Text style={styles.menuDescription}>{item.description}</Text>
        </View>
      </View>
      <Icon name="chevron-forward" size={20} color="#ccc" />
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        {/* Profile Header */}
        <View style={styles.profileHeader}>
          <View style={styles.avatarContainer}>
            <Icon name="person-outline" size={50} color="#007bff" />
          </View>
          <Text style={styles.userName}>{userProfile.name}</Text>
          <Text style={styles.userEmail}>{userProfile.email}</Text>
          <View style={styles.roleBadge}>
            <Text style={styles.roleBadgeText}>{userProfile.role}</Text>
          </View>
        </View>

        {/* Account Information */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Account Information</Text>

          <View style={styles.infoCard}>
            <View style={styles.infoItem}>
              <Icon name="business-outline" size={20} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Department</Text>
                <Text style={styles.infoValue}>{userProfile.department}</Text>
              </View>
            </View>

            <View style={styles.infoItem}>
              <Icon name="calendar-outline" size={20} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Join Date</Text>
                <Text style={styles.infoValue}>
                  {new Date(userProfile.joinDate).toLocaleDateString('id-ID')}
                </Text>
              </View>
            </View>

            <View style={styles.infoItem}>
              <Icon name="shield-checkmark-outline" size={20} color="#666" />
              <View style={styles.infoContent}>
                <Text style={styles.infoLabel}>Account Status</Text>
                <View style={styles.statusContainer}>
                  <View style={[styles.statusDot, styles.statusActive]} />
                  <Text style={styles.statusText}>Active</Text>
                </View>
              </View>
            </View>
          </View>
        </View>

        {/* Menu Items */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Settings</Text>
          <View style={styles.menuContainer}>
            {menuItems.map(renderMenuItem)}
          </View>
        </View>

        {/* Logout Button */}
        <View style={styles.logoutSection}>
          <TouchableOpacity
            style={styles.logoutButton}
            onPress={handleLogout}
          >
            <Icon name="log-out-outline" size={20} color="#dc3545" />
            <Text style={styles.logoutButtonText}>Logout</Text>
          </TouchableOpacity>
        </View>

        {/* Version Info */}
        <View style={styles.versionSection}>
          <Text style={styles.versionText}>
            HRM Mobile App v1.0.0
          </Text>
          <Text style={styles.copyrightText}>
            © 2024 PT. COCOK GAN
          </Text>
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
  profileHeader: {
    backgroundColor: '#007bff',
    alignItems: 'center',
    paddingVertical: 40,
    paddingHorizontal: 20,
  },
  avatarContainer: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 15,
  },
  userName: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 5,
  },
  userEmail: {
    fontSize: 16,
    color: '#e3f2fd',
    marginBottom: 15,
  },
  roleBadge: {
    backgroundColor: 'rgba(255,255,255,0.2)',
    paddingHorizontal: 15,
    paddingVertical: 8,
    borderRadius: 20,
  },
  roleBadgeText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 14,
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
  infoCard: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  infoItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
  },
  infoContent: {
    flex: 1,
    marginLeft: 15,
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
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 8,
  },
  statusActive: {
    backgroundColor: '#28a745',
  },
  statusText: {
    fontSize: 12,
    color: '#28a745',
    fontWeight: 'bold',
  },
  menuContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  menuItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
    justifyContent: 'space-between',
  },
  menuItemLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  menuIconContainer: {
    width: 40,
    height: 40,
    borderRadius: 8,
    backgroundColor: '#f0f8ff',
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 15,
  },
  menuContent: {
    flex: 1,
  },
  menuTitle: {
    fontSize: 16,
    fontWeight: '500',
    color: '#333',
    marginBottom: 2,
  },
  menuDescription: {
    fontSize: 12,
    color: '#666',
  },
  logoutSection: {
    padding: 15,
  },
  logoutButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 15,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    borderWidth: 1,
    borderColor: '#dc3545',
  },
  logoutButtonText: {
    color: '#dc3545',
    fontSize: 16,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  versionSection: {
    padding: 15,
    alignItems: 'center',
  },
  versionText: {
    fontSize: 12,
    color: '#666',
    marginBottom: 5,
  },
  copyrightText: {
    fontSize: 10,
    color: '#999',
  },
});

export default ProfileScreen;