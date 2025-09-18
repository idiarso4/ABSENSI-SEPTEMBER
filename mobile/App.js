import React, { useEffect, useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { StyleSheet, View } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Icon from '@expo/vector-icons/Ionicons';

// Import Screens
import LoginScreen from './src/screens/LoginScreen';
import DashboardScreen from './src/screens/DashboardScreen';
import StudentListScreen from './src/screens/StudentListScreen';
import StudentDetailScreen from './src/screens/StudentDetailScreen';
import TeacherListScreen from './src/screens/TeacherListScreen';
import TeacherDetailScreen from './src/screens/TeacherDetailScreen';
import AttendanceScreen from './src/screens/AttendanceScreen';
import ProfileScreen from './src/screens/ProfileScreen';
import ReportsScreen from './src/screens/ReportsScreen';

// Import Services
import { apiService } from './src/services/apiService';
import { AuthContext } from './src/services/AuthContext';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function MainTabs() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName;

          if (route.name === 'Dashboard') {
            iconName = focused ? 'home' : 'home-outline';
          } else if (route.name === 'Students') {
            iconName = focused ? 'school' : 'school-outline';
          } else if (route.name === 'Attendance') {
            iconName = focused ? 'checkmark-circle' : 'checkmark-circle-outline';
          } else if (route.name === 'Reports') {
            iconName = focused ? 'document-text' : 'document-text-outline';
          }

          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: '#007bff',
        tabBarInactiveTintColor: 'gray',
        headerStyle: {
          backgroundColor: '#007bff',
        },
        headerTintColor: '#fff',
        headerTitleStyle: {
          fontWeight: 'bold',
        },
      })}
    >
      <Tab.Screen name="Dashboard" component={DashboardScreen} />
      <Tab.Screen name="Students" component={StudentListScreen} />
      <Tab.Screen name="Attendance" component={AttendanceScreen} />
      <Tab.Screen name="Reports" component={ReportsScreen} />
    </Tab.Navigator>
  );
}

export default function App() {
  const authContext = React.useMemo(() => ({
    signOut: async () => {
      try {
        await AsyncStorage.removeItem('userToken');
        setUserToken(null);
        apiService.setAuthToken(null);
      } catch (error) {
        console.log('Error during logout:', error);
      }
    },
    signIn: async (token) => {
      try {
        await AsyncStorage.setItem('userToken', token);
        setUserToken(token);
        apiService.setAuthToken(token);
      } catch (error) {
        console.log('Error during login token set:', error);
      }
    }
  }), []);
  const [isLoading, setIsLoading] = useState(true);
  const [userToken, setUserToken] = useState(null);

  useEffect(() => {
    // Check if user is logged in
    checkUserToken();
  }, []);

  const checkUserToken = async () => {
    try {
      const token = await AsyncStorage.getItem('userToken');
      if (token) {
        setUserToken(token);
        apiService.setAuthToken(token);
      }
    } catch (error) {
      console.log('Error checking token:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogin = (token) => {
    setUserToken(token);
  };

  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('userToken');
      setUserToken(null);
      apiService.setAuthToken(null);
    } catch (error) {
      console.log('Error during logout:', error);
    }
  };

  if (isLoading) {
    return (
      <View style={styles.container}>
        <StatusBar style="auto" />
      </View>
    );
  }

  return (
    <AuthContext.Provider value={authContext}>
      <NavigationContainer>
        <Stack.Navigator>
          {userToken ? (
          <Stack.Screen
            name="MainTabs"
            component={MainTabs}
            options={{ headerShown: false }}
          />
        ) : (
            <Stack.Screen
              name="Login"
              options={{ headerShown: false }}
            >
              {(props) => <LoginScreen {...props} onLogin={handleLogin} />}
            </Stack.Screen>
          )}
          <Stack.Screen
            name="StudentDetail"
            component={StudentDetailScreen}
            options={{
              title: 'Student Details',
              headerStyle: { backgroundColor: '#007bff' },
              headerTintColor: '#fff'
            }}
          />
          <Stack.Screen
            name="TeacherDetail"
            component={TeacherDetailScreen}
            options={{
              title: 'Teacher Details',
              headerStyle: { backgroundColor: '#007bff' },
              headerTintColor: '#fff'
            }}
          />
          <Stack.Screen
            name="Teachers"
            component={TeacherListScreen}
            options={{
              title: 'Teachers',
              headerStyle: { backgroundColor: '#007bff' },
              headerTintColor: '#fff'
            }}
          />
          <Stack.Screen
            name="Attendance"
            component={AttendanceScreen}
            options={{
              title: 'Mark Attendance',
              headerStyle: { backgroundColor: '#007bff' },
              headerTintColor: '#fff'
            }}
          />
          <Stack.Screen
            name="LeaveRequest"
            component={require('./src/screens/LeaveRequestScreen').default}
            options={{
              title: 'Leave Request',
              headerStyle: { backgroundColor: '#007bff' },
              headerTintColor: '#fff'
            }}
          />
        </Stack.Navigator>
        <StatusBar style="auto" />
      </NavigationContainer>
    </AuthContext.Provider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});