# HRM Mobile App

A React Native mobile application for Human Resource Management System

## Features

- 🔐 JWT Authentication
- 📊 Dashboard with HR Statistics
- 👥 Employee Management
- 📱 Responsive Mobile UI
- 🌐 Integration with Spring Boot Backend

## Setup Instructions

### Prerequisites
- Node.js (v18 or higher)
- npm or yarn
- React Native CLI
- Android Studio (for Android development)
- Xcode (for iOS development)

### Installation

1. **Clone and navigate to mobile app directory**
   ```bash
   cd mobile-app
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Set up Expo**
   ```bash
   npx expo install --fix
   ```

### Backend Configuration

Update the API base URL in `src/services/apiService.js`:
```javascript
this.baseURL = 'http://YOUR_BACKEND_IP:8080/api'; // Change to your backend URL
```

#### For Local Development:
- **Android Emulator**: `http://10.0.2.2:8080/api` (special alias for localhost)
- **Physical Device**: `http://[YOUR_LOCAL_IP]:8080/api`
- **Network WiFi**: Ensure both device and computer are on same network

#### For Production:
- Update to your production server URL
- Configure HTTPS for secure communication
- Update authentication endpoints if needed

### Environment Setup

The app is configured for multiple backend environments:

**Development**: Local backend
**Staging**: Staging server
**Production**: Production server

Update `app.json` for different environments:
```json
{
  "expo": {
    "extra": {
      "API_BASE_URL": "http://your-production-server:8080/api"
    }
  }
}
```

### Running the App

#### Development
```bash
npm start
# or
npx expo start
```

#### For Android
```bash
npm run android
# or
npx expo start --android
```

#### For iOS
```bash
npm run ios
# or
npx expo start --ios
```

#### For Web
```bash
npm run web
# or
npx expo start --web
```

## Project Structure

```
mobile-app/
├── src/
│   ├── screens/
│   │   ├── DashboardScreen.js      # Main dashboard
│   │   ├── LoginScreen.js          # Authentication
│   │   ├── EmployeeListScreen.js   # Employee list
│   │   ├── EmployeeDetailScreen.js # Employee details
│   │   ├── ProfileScreen.js        # User profile
│   │   └── ReportsScreen.js        # Reports and analytics
│   ├── components/
│   │   └── # Reusable UI components
│   ├── services/
│   │   └── apiService.js           # API integration
│   ├── navigation/
│   │   └── # Navigation configurations
│   ├── utils/
│   │   └── # Utility functions
│   └── styles/
│       └── # Style constants
├── App.js                          # Main app component
├── package.json                    # Dependencies
├── app.json                        # Expo configuration
└── README.md                       # This file
```

## Key Features Implemented

### Authentication
- Secure JWT token-based authentication
- Automatic token refresh and logout on expiry
- Persisted login state using AsyncStorage

### Employee Management
- View all employees with search and filter
- Employee details and profile information
- Department-wise filtering
- Real-time data updates

### Dashboard
- Key HR statistics and metrics
- Department distribution overview
- Quick action buttons
- Recent activity feed

### API Integration
- Axios-based HTTP client
- Automatic authentication header management
- Error handling and retry logic
- Offline data handling capabilities

## Backend Integration

This mobile app is designed to work with the Java Spring Boot HRM backend. Key endpoints used:

- `POST /api/auth/login` - User authentication
- `GET /api/employees` - Employee listings
- `GET /api/dashboard` - Dashboard statistics
- `GET /api/departments` - Department data
- `GET /api/reports/*` - Report generation

## Technologies Used

- **React Native**: Cross-platform mobile development
- **Expo**: Development platform and runtime
- **Axios**: HTTP client for API communication
- **AsyncStorage**: Local data persistence
- **React Navigation**: Navigation and routing
- **Vector Icons**: Icon library

## Development Setup

### Prerequisites Detailed
- **Node.js**: v18.0.0 or higher (check with `node -v`)
- **NPM**: Usually comes with Node.js (check with `npm -v`)
- **Expo CLI**: `npm install -g @expo/cli`
- **Git**: For version control

### Environment Variables
Create `.env` file in mobile-app root:
```env
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080/api
EXPO_PUBLIC_APP_NAME=HRM Mobile
```

### Installing Expo CLI (if not installed)
```bash
npm install -g @expo/cli
# or using npx
npx expo-cli --version
```

### Common Setup Issues

#### Issue: Metro bundler not starting
```bash
# Clear cache and restart
npx expo start --clear
```

#### Issue: Android emulator issues
```bash
# Start Android Studio -> Open Android Device Manager -> Start emulator
# Check if ADB is running
adb devices
```

#### Issue: Network connectivity issues
```bash
# Check if backend is running
curl http://localhost:8080/api/health
```

#### Issue: Package installation problems
```bash
# Clean npm cache
npm cache clean --force
# Delete node_modules and reinstall
rm -rf node_modules && npm install
```

## Architecture

- **MVVM Pattern**: Separation of concerns
- **Service Layer**: API communication abstraction
- **State Management**: React hooks and context
- **Error Handling**: Comprehensive error boundaries
- **Performance Optimization**: FlatList virtualization, memoization

## Development Notes

- The app is built using Expo managed workflow for easier development
- Uses modern React hooks and functional components
- Implements responsive design patterns
- Supports both portrait and landscape orientations
- Includes loading states and error handling

## Contributing

When other team members work on the dashboard page, ensure:

1. All components follow the established styling patterns
2. API calls are properly abstracted through the apiService
3. Error handling is implemented consistently
4. Navigation is handled through the central navigation structure
5. State management follows the established patterns

## Testing

### Running Tests
```bash
# Run unit tests
npm test

# Run with coverage
npm test -- --coverage

# Watch mode
npm test -- --watch
```

### Manual Testing Checklist
- [ ] Login with valid credentials
- [ ] Dashboard loads employee statistics
- [ ] Employee list shows properly
- [ ] Employee search and filtering works
- [ ] Employee detail view displays correctly
- [ ] Profile screen shows user information
- [ ] Logout functionality works
- [ ] Network error handling
- [ ] Pull-to-refresh functionality

## Deployment

### Build for Production

#### Android APK
```bash
# Build APK
npx expo build:android

# Download from Expo servers or use adb install
adb install my-app.apk
```

#### iOS (requires Apple Developer Account)
```bash
# Build for iOS
npx expo build:ios
```

#### Web Version
```bash
# Build for web
npx expo build:web
```

### Over-The-Air Updates
Expo allows sending updates without app store submissions:
```bash
# Publish update
npx expo publish
```

## API Integration Guidelines

When extending the mobile app, follow these API integration patterns:

1. **All API calls through apiService.js**
2. **JWT token auto-management**
3. **Error handling centralized**
4. **Loading states for user feedback**
5. **Offline data handling consideration**

### Adding New API Endpoints

Example in `src/services/apiService.js`:
```javascript
async getEmployeeAttendance(employeeId, date) {
  try {
    const response = await this.client.get(`/attendance/employee/${employeeId}`, {
      params: { date }
    });
    return response.data;
  } catch (error) {
    throw error.response?.data?.message || 'Failed to fetch attendance';
  }
}
```

## Performance Optimization

- **Image Optimization**: Use appropriate image sizes
- **List Virtualization**: Implemented with FlatList
- **Memoization**: Use React.memo for performance
- **Bundle Splitting**: Code splitting for better performance
- **Caching**: Implement response caching for frequently used data

## Security Best Practices

- **Token Storage**: Secure storage using AsyncStorage
- **Certificate Pinning**: Consider implementing for production
- **Input Validation**: Validate all user inputs
- **Error Logging**: Implement error logging service
- **Data Sanitization**: Clean data before display

## Next Steps for Development

- [ ] Add offline data synchronization
- [ ] Implement push notifications
- [ ] Add biometric authentication
- [ ] Expand reporting capabilities
- [ ] Add employee photo upload functionality
- [ ] Implement chat/messaging features
- [ ] Add multi-language support
- [ ] Implement dark theme support
- [ ] Add employee clock-in/out functionality
- [ ] Integrate with calendar for leave requests

## Support

For technical support or questions:
1. Check the troubleshooting section above
2. Review the backend system logs
3. Check Expo documentation
4. Contact development team

## License

© 2024 PT. COCOK GAN - All rights reserved.

## Cara Menjalankan Aplikasi (Where does the app run?)

Aplikasi ini berjalan menggunakan Expo. Anda bisa menjalankannya di:

- Android (Emulator Android Studio atau perangkat fisik via Expo Go)
- iOS (Simulator Xcode di macOS atau perangkat fisik via Expo Go)
- Web (browser)

### Langkah Cepat
1. Jalankan backend Spring Boot Anda terlebih dahulu dan pastikan `baseURL` di `src/services/apiService.js` sudah benar.
   - Emulator Android: gunakan `http://10.0.2.2:8080/api`
   - Perangkat fisik di jaringan yang sama: `http://ALAMAT_IP_KOMPUTER:8080/api`
2. Install dependency (sekali saja):
   - `npm install`
3. Start Expo:
   - `npm start`

Saat Metro Bundler terbuka (jendela terminal Expo), gunakan tombol pintas:
- Tekan `a` untuk membuka di Android Emulator (Windows/Linux butuh Android Studio dan AVD terpasang)
- Tekan `i` untuk membuka di iOS Simulator (hanya di macOS dengan Xcode)
- Tekan `w` untuk membuka versi Web di browser

Atau gunakan skrip ini langsung:
- Android: `npm run android`
- iOS: `npm run ios` (di macOS)
- Web: `npm run web`

### Menjalankan di Perangkat Fisik
- Install aplikasi **Expo Go** dari Play Store / App Store.
- Jalankan `npm start`, lalu scan QR code yang muncul menggunakan Expo Go.
- Pastikan perangkat dan komputer berada pada jaringan Wi‑Fi yang sama.

### Catatan Penting Backend
- Default `baseURL` adalah `http://localhost:8080/api`. Ini tidak bisa diakses dari Emulator Android atau perangkat fisik.
  - Emulator Android: ganti menjadi `http://10.0.2.2:8080/api`.
  - Perangkat fisik: ganti menjadi `http://IP_KOMPUTER:8080/api`.
- Setelah mengubah `src/services/apiService.js`, simpan file; hot reload akan memperbarui aplikasi.

Jika butuh bantuan lebih lanjut (contoh set up Android Studio/AVD atau konfigurasi jaringan), beri tahu platform yang Anda gunakan (Windows/macOS/Linux) dan target (Emulator/Perangkat/Web).
