# Roadmap Mobile App - SIM Sekolah

Checklist kelengkapan aplikasi mobile untuk Sistem Informasi Sekolah (SIM Sekolah). Berdasarkan fitur backend yang sudah ada dan implementasi mobile saat ini.

## Legend
- [x] Sudah diimplementasi
- [ ] Belum diimplementasi
- [-] Dalam pengerjaan

## 1. Setup & Infrastructure
- [x] React Native project setup (App.js, package.json, metro.config.js)
- [x] Navigation setup (src/navigation/)
- [x] API Service integration (src/services/apiService.js)
- [x] Authentication Context (src/services/AuthContext.js)
- [ ] Redux/MobX state management
- [ ] Offline data persistence
- [ ] Push notifications setup

## 2. Authentication & Security
- [x] Login Screen (LoginScreen.js)
- [x] JWT token handling (terintegrasi dengan backend JwtTokenProvider)
- [x] Logout functionality
- [ ] Password reset
- [ ] Biometric authentication
- [ ] Session management

## 3. Dashboard & Navigation
- [x] Dashboard Screen (DashboardScreen.js)
- [x] Statistics display (basic with fallback)
- [x] Quick action buttons (basic)
- [x] Menu navigation
- [x] User profile header

## 4. User Management
- [x] Profile Screen (ProfileScreen.js - perlu diverifikasi)
- [ ] User profile editing
- [ ] Role-based UI (Admin, Teacher, Student)
- [ ] User permissions handling

## 5. Student Management
- [ ] Student List Screen
- [ ] Student Detail Screen
- [ ] Student registration
- [ ] Student search & filter
- [ ] Student attendance view
- [ ] Student grades view
- [ ] Student payment status

## 6. Teacher Management
- [x] Employee List Screen (EmployeeListScreen.js - kemungkinan untuk guru)
- [x] Employee Detail Screen (EmployeeDetailScreen.js)
- [ ] Teacher schedule view
- [ ] Teacher attendance tracking
- [ ] Teacher subject assignment

## 7. Subject & Curriculum Management
- [ ] Subject list
- [ ] Subject details
- [ ] Curriculum overview
- [ ] Learning materials access

## 8. Class Management
- [ ] Class list
- [ ] Class details
- [ ] Class student roster
- [ ] Class schedule

## 9. Attendance System
- [ ] Student attendance marking
- [ ] Attendance history view
- [ ] Teacher attendance tracking
- [ ] Attendance reports

## 10. Assessment & Grades
- [ ] Test/Exam list
- [ ] Grade input (for teachers)
- [ ] Grade view (for students)
- [ ] Assessment analytics

## 11. Payments & Invoices
- [ ] Payment list
- [ ] Invoice viewing
- [ ] Payment status tracking
- [ ] Fee structure display

## 12. Reports & Analytics
- [x] Reports Screen (ReportsScreen.js)
- [ ] Academic reports
- [ ] Attendance reports
- [ ] Financial reports
- [ ] Student performance analytics

## 13. Extracurricular Activities
- [ ] Activity list
- [ ] Activity registration
- [ ] Activity attendance
- [ ] Activity reports

## 14. Counseling System
- [ ] Counseling case management
- [ ] Counseling appointments
- [ ] Counseling reports

## 15. PKL (Praktik Kerja Lapangan)
- [ ] PKL activity tracking
- [ ] PKL reports
- [ ] PKL attendance

## 16. Notifications
- [ ] In-app notifications
- [ ] Push notifications
- [ ] Announcement system

## 17. Settings
- [ ] App settings
- [ ] Notification preferences
- [ ] Language selection
- [ ] Theme selection

## 18. Testing & Quality Assurance
- [ ] Unit tests
- [ ] Integration tests
- [ ] UI/UX testing
- [ ] Performance testing
- [ ] Security testing

## 19. Deployment
- [ ] Android APK build
- [ ] iOS IPA build
- [ ] App Store deployment
- [ ] Play Store deployment
- [ ] CI/CD pipeline

## Prioritas Implementasi
### Phase 1 (Core Features)
1. Complete authentication flow
2. Basic dashboard with statistics
3. User profile management
4. Student list and basic CRUD
5. Teacher list and basic CRUD
6. Basic attendance marking

### Phase 2 (Advanced Features)
1. Assessment and grading system
2. Payment and invoice management
3. Comprehensive reports
4. Offline functionality

### Phase 3 (Enhancements)
1. Push notifications
2. Advanced analytics
3. Multi-language support
4. Theme customization

## Backend Integration Status
- [x] REST API endpoints available
- [x] Authentication with JWT
- [x] CRUD operations for all entities
- [ ] File upload handling
- [ ] Real-time notifications
- [ ] API documentation (Swagger)

## Quick Wins (2 Minggu Ke Depan)
- [ ] .env baseURL untuk API (dev/stg/prod) + fallback emulator host (Android: 10.0.2.2)
- [x] Form Pengajuan Cuti sederhana (requestLeave) + hubungkan dari Employee Detail → Leave Request
- [ ] Edit Employee dasar (nama, email, phone, department, designation) dari Employee Detail
- [ ] Reports: Preview/Download Payslip (expo-file-system / WebView)
- [ ] Error handling standar (toast) + indikator offline (NetInfo)

## Catatan Lingkungan
- Emulator Android gunakan host 10.0.2.2 (sudah diimplementasi di apiService).
- iOS Simulator gunakan localhost.
- Pertimbangkan pemisahan baseURL via app.config.js atau react-native-dotenv.

Terakhir diperbarui: 2025-09-16 05:47
