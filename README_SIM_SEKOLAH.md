# 🎓 SIM SEKOLAH - Sistem Informasi Manajemen Sekolah

Sistem Informasi Manajemen Sekolah lengkap untuk SMK SIJA dengan fitur absensi canggih, face recognition, dan GPS tracking.

## 🚀 Quick Start

### Jalankan Sistem (Development)
```bash
# 🚀 START SISTEM - Klik 2x atau jalankan:
start.bat

# 🛑 STOP SISTEM:
stop.bat

# 📊 CHECK STATUS:
status.bat

# ❓ HELP & GUIDE:
help.bat
```

### 🧪 Smoke Test Excel Imports
Validasi cepat untuk endpoint import Excel menggunakan dryRun (tidak mengubah database):
```powershell
# Jalankan smoke test (butuh backend running):
.\tools\smoke_imports.ps1 -LoginIdentifier admin@simsekolah.com -LoginPassword admin123

# Output contoh:
# [STEP] Pulling latest templates â€¦
# [SMOKE] Students â†’ /api/v1/students/excel/import
#   status= totalRows=1 ok=1 failed=0 createdItems=1 errors=0
```

### Docker (Production Only)
```bash
# Jalankan dengan Docker untuk production:
start_docker.bat
```

## 📋 Prerequisites

- **Java 17+** (untuk Backend)
- **Node.js 16+** (untuk Frontend)
- **PostgreSQL/MySQL** (untuk Database)
- **Docker** (opsional, untuk containerized deployment)

## 🏗️ Arsitektur Sistem

```
SIM_CLONE/
├── backend/           # Java Spring Boot API
├── frontend/          # Express.js Web App
├── mobile/            # React Native Mobile App
├── docker-compose.yml # Container orchestration
├── start.bat         # Quick start script
├── stop.bat          # Stop all services
└── start_docker.bat  # Docker startup
```

## 🎯 Fitur Utama

### ✅ Sistem Absensi Canggih (5 Tipe)
- **ARRIVAL_DEPARTURE** - Datang/pulang dengan face recognition
- **CLASS** - Absensi kelas oleh guru
- **PRAYER** - Absensi shalat dzuhur
- **PERMISSION** - Izin keluar masuk (guru piket)
- **PKL** - Praktik Kerja Lapangan (guru pembimbing)

### ✅ Multi-Role System
- **ADMIN** - Administrator sekolah
- **TEACHER** - Guru biasa
- **DUTY_TEACHER** - Guru piket
- **SUPERVISING_TEACHER** - Guru pembimbing PKL
- **PARENT** - Orang tua siswa

### ✅ Manajemen Akademik Lengkap
- Student Management (CRUD, search, class assignment)
- Teacher Management (CRUD, subject assignment)
- Schedule Management (conflict detection)
- Grading System (assessment, nilai, rapor)
- Academic Reporting & Analytics

### ✅ **🆕 Menu & Submenu Baru (Admin Only)**

#### **🏭 Manajemen PKL (Praktik Kerja Lapangan)**
Sistem manajemen praktik kerja lapangan untuk siswa SMK dengan fitur:
- **Absensi PKL** - Tracking kehadiran siswa di tempat PKL
- **Kunjungan Pembimbing** - Jadwal dan laporan kunjungan guru pembimbing
- **Kegiatan PKL** - Dokumentasi kegiatan harian siswa PKL
- **Laporan Harian** - Sistem pelaporan progress siswa PKL

#### **📝 Perizinan Siswa (Student Permissions)**
Sistem perizinan siswa dengan kontrol ketat:
- **Izin Masuk** - Perizinan keterlambatan masuk sekolah
- **Izin Keluar** - Perizinan keluar selama jam sekolah
- **Laporan Guru Piket** - Monitoring dan approval oleh guru piket

#### **🛡️ Manajemen BK (Bimbingan Konseling)**
Sistem bimbingan konseling siswa yang terintegrasi:
- **Penanganan Siswa** - Case management siswa bermasalah
- **Daftar Kunjungan** - Tracking kunjungan siswa ke BK
- **Surat Kesepakatan** - Dokumen perjanjian siswa dengan sekolah

#### **📚 LMS (Learning Management System)**
Platform pembelajaran digital terintegrasi:
- **Absensi Guru** - Monitoring kehadiran pengajar
- **Materi Pembelajaran** - Repository materi ajar digital
- **Daftar Soal** - Bank soal untuk ujian dan kuis
- **Ulangan & Tes** - Sistem penilaian online

## 🔧 Konfigurasi Database

### PostgreSQL Setup
```sql
-- Buat database
CREATE DATABASE simsekolah_db;

-- Buat user
CREATE USER simuser WITH PASSWORD 'simpass';

-- Berikan privileges
GRANT ALL PRIVILEGES ON DATABASE simsekolah_db TO simuser;
```

### Update application.properties
```properties
# backend/src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/simsekolah_db
spring.datasource.username=simuser
spring.datasource.password=simpass
```

## 🚀 Menjalankan Sistem

### 1. Jalankan Backend
```bash
cd backend
mvn spring-boot:run
```
**API akan berjalan di:** http://localhost:8080

### 2. Jalankan Frontend
```bash
cd frontend
npm install
npm start
```
**Web app akan berjalan di:** http://localhost:3000

### 3. Jalankan Mobile App (Opsional)
```bash
cd mobile
npm install
npx react-native run-android  # Untuk Android
npx react-native run-ios      # Untuk iOS
```

## 📊 API Endpoints

### Authentication
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/logout
GET  /api/auth/profile
```

### Student Management
```
GET  /api/students
POST /api/students
GET  /api/students/{id}
PUT  /api/students/{id}
DEL  /api/students/{id}
GET  /api/students/search?name=
```

### Attendance System
```
GET  /api/attendance
POST /api/attendance
GET  /api/attendance/student/{id}
GET  /api/attendance/class/{id}
POST /api/attendance/permission
POST /api/attendance/pkl
```

### Academic Management
```
GET  /api/schedules
POST /api/schedules
GET  /api/grades
POST /api/grades
GET  /api/subjects
GET  /api/classes
```

### 📥 Excel Import (Baru)

Gunakan endpoint berikut untuk impor data dari file .xlsx (Excel):

- Subjects: `POST /api/v1/subjects/excel/import`
   - Kolom: `Code`, `Name`, `Type` (THEORY/PRACTICE/MIXED), `Description`
   - Template: `GET /api/v1/subjects/excel/template`

- Teachers: `POST /api/v1/teachers/excel/import`
   - Kolom: `NIP`, `Full Name`, `Email`, `Phone`, `Address`, `Status` (ACTIVE/AKTIF atau INACTIVE)
   - Template: `GET /api/v1/teachers/excel/template`
   - Catatan: Password default akan dibuat otomatis (mis. `Password123!`), disarankan segera reset.

- Classrooms: `POST /api/v1/classrooms/excel/import`
   - Kolom: `Class Name`, `Grade Level`, `Academic Year`, `Semester`, `Capacity`, `Location`, `Status`, `Major` (by Code or Name), `Homeroom Teacher` (by NIP or Email)
   - Template: `GET /api/v1/classrooms/excel/template`

  Opsi: tambahkan query `?dryRun=true` untuk validasi tanpa menyimpan perubahan (tetap mengembalikan ringkasan sukses/error).

Semua endpoint import mengembalikan ringkasan: `totalRows`, `successfulImports`, `failedImports`, daftar yang dibuat, dan `errors` (baris, field, pesan).

#### Konfigurasi Import (application.properties)

```properties
# Batas tipe file dan ukuran sudah diatur oleh Spring multipart
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB

# Pengaturan khusus import Excel
app.import.excel.allowed-content-types=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
app.import.max-rows=5000
app.import.teacher.default-password=Password123!
```

Catatan:
- `app.import.teacher.default-password` dipakai sebagai password awal untuk guru yang diimpor; segera minta reset.
- `app.import.max-rows` membatasi jumlah baris data (tidak termasuk header) untuk mencegah beban berlebih.
 - Import Classrooms: kolom `Major` akan dicocokkan dengan Major `code` atau `name` yang ada; `Homeroom Teacher` akan dicocokkan dengan `NIP` atau `Email` guru yang sudah terdaftar. Jika tidak ditemukan, baris akan ditandai error dan dilewati.

Template Notes:
- Semua template kini menyertakan 1-2 baris contoh. Classroom template menunjukkan contoh `Major` via Kode (RPL) dan Nama (Teknik Komputer Jaringan), serta `Homeroom Teacher` via NIP dan Email.
- Subject/Teacher templates juga memiliki contoh baris (kode mapel, tipe THEORY/PRACTICE, dan contoh guru lengkap).

Postman & Fixtures:
- Postman Collection: `postman/SIM-Sekolah_Excel_Imports.postman_collection.json`
- Environment (Local): `postman/SIM-Sekolah_Local.postman_environment.json` (set `baseUrl` dan `fixturesPath`)
- Tempatkan file contoh `.xlsx` di folder `fixtures/` (lihat `fixtures/README.md`).

### 🆕 PKL Management (Admin Only)
```
GET  /api/pkl/attendance          # Daftar absensi PKL
POST /api/pkl/attendance          # Input absensi PKL
GET  /api/pkl/visits              # Daftar kunjungan pembimbing
POST /api/pkl/visits              # Input kunjungan pembimbing
GET  /api/pkl/activities          # Daftar kegiatan PKL
POST /api/pkl/activities          # Input kegiatan PKL
GET  /api/pkl/daily-reports       # Laporan harian PKL
POST /api/pkl/daily-reports       # Submit laporan harian
```

### 🆕 Student Permissions (Admin Only)
```
GET  /api/permissions/entry        # Daftar izin masuk
POST /api/permissions/entry        # Ajukan izin masuk
GET  /api/permissions/exit         # Daftar izin keluar
POST /api/permissions/exit         # Ajukan izin keluar
GET  /api/permissions/duty-reports # Laporan guru piket
POST /api/permissions/approve      # Approve izin
POST /api/permissions/reject       # Reject izin
```

### 🆕 Counseling Management (Admin Only)
```
GET  /api/bk/cases                 # Daftar kasus siswa
POST /api/bk/cases                 # Input kasus baru
GET  /api/bk/visits                # Daftar kunjungan BK
POST /api/bk/visits                # Input kunjungan BK
GET  /api/bk/agreements            # Daftar surat kesepakatan
POST /api/bk/agreements            # Generate surat kesepakatan
```

### 🆕 Learning Management System (Admin Only)
```
GET  /api/lms/teacher-attendance   # Absensi guru
POST /api/lms/teacher-attendance   # Input absensi guru
GET  /api/lms/materials            # Daftar materi
POST /api/lms/materials            # Upload materi
GET  /api/lms/questions            # Bank soal
POST /api/lms/questions            # Tambah soal
GET  /api/lms/tests                # Daftar ulangan
POST /api/lms/tests                # Buat ulangan
POST /api/lms/tests/submit         # Submit jawaban
```

## 🎨 User Interface

### Web Dashboard
- **URL:** http://localhost:3000
- Responsive design dengan Bootstrap
- Real-time charts dan statistics
- Student/teacher management interface
- Attendance tracking dashboard

### 🆕 Navigation Structure (Role-Based)
```
📊 Dashboard
├── 🎓 Akademik (Academic)
│   ├── Data Siswa & Guru
│   │   ├── Kelola Siswa
│   │   └── Kelola Guru
│   ├── Kurikulum
│   │   ├── Mata Pelajaran
│   │   ├── Ruang Kelas
│   │   ├── Penilaian
│   │   ├── Nilai Siswa
│   │   └── Jadwal Pelajaran
│   └── 📅 Kalender Akademik (Admin)
│       └── 📝 Manajemen Semester (Admin)
│
├── 👥 Kehadiran (Attendance)
│   ├── Kehadiran Harian
│   ├── Kehadiran Kelas
│   ├── Laporan Kehadiran
│   └── 📍 Kalender Kehadiran (Admin)
│
├── 💰 Keuangan (Finance)
│   ├── Pembayaran SPP
│   ├── Kegiatan
│   ├── Seragam
│   ├── Buku
│   ├── Laporan Keuangan
│   └── 📄 Penagihan/Invoice (Admin)
│       └── 🚫 Tunggakan (Admin)
│
├── 📊 Laporan (Reports)
│   ├── Laporan Akademik
│   │   ├── Nilai Akademik
│   │   ├── Kehadiran
│   │   ├── Performa Siswa
│   │   └── 📋 Rapor (Admin)
│   │       └── 📈 Analitik Nilai (Admin)
│   ├── Laporan Keuangan
│   │   ├── Pembayaran
│   │   └── Keuangan Sekolah
│   └── 📈 Analytics Dashboard (Admin)
│       └── 📤 Export (PDF/Excel) (Admin)
│
├── 🏭 Manajemen PKL (Admin Only)
│   ├── 📝 Absensi PKL
│   ├── 👁️ Kunjungan Pembimbing
│   ├── 📋 Kegiatan PKL
│   └── 📄 Laporan Harian
│
├── 📝 Perizinan Siswa (Admin Only)
│   ├── 🚪 Izin Masuk
│   ├── 🚶 Izin Keluar
│   └── 👨‍🏫 Laporan Guru Piket
│
├── 🛡️ Manajemen BK (Admin Only)
│   ├── 👤 Penanganan Siswa
│   ├── 📅 Daftar Kunjungan
│   └── 📄 Surat Kesepakatan
│
├── 📚 LMS (Admin Only)
│   ├── 👨‍🏫 Absensi Guru
│   ├── 📖 Materi Pembelajaran
│   ├── ❓ Daftar Soal
│   └── 📝 Ulangan & Tes
│
└── ⚙️ Sistem (System)
    ├── 👥 Kelola Pengguna
    ├── 🏷️ Peran & Izin
    ├── 📋 Manajemen Tugas
    ├── 🏫 Pengaturan Akademik
    ├── 🔧 Sistem
    ├── 💾 Backup & Restore
    └── 🌐 Bahasa
```

### API Documentation
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs

## 📱 Mobile Features

- React Native dengan modern UI
- Offline capability
- Push notifications
- Camera integration untuk face recognition
- GPS tracking untuk location validation

## 🐳 Docker Deployment

### Jalankan dengan Docker
```bash
# Build dan jalankan semua services
docker-compose up -d

# Lihat status services
docker-compose ps

# Lihat logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Docker Services
- **Backend:** http://localhost:8080
- **Frontend:** http://localhost:3000
- **Database:** localhost:5432
- **Redis:** localhost:6379

## 🔐 Default Credentials

### Admin User
- **Username:** admin@simsekolah.com
- **Password:** admin123
- **Role:** ADMIN

### Sample Users
- **Teacher:** teacher@simsekolah.com / teacher123
- **Parent:** parent@simsekolah.com / parent123

## 📊 Database Schema

Sistem menggunakan **172+ entities** dengan relationships yang kompleks:
- Users → Students/Teachers
- Classes → Students → Attendance
- Teachers → Subjects → Schedules
- Grades → Students → Subjects
- Attendance → Location coordinates

## 🛠️ Development

### Backend Development
```bash
cd backend
mvn clean compile
mvn test
mvn spring-boot:run
```

### Frontend Development
```bash
cd frontend
npm install
npm run dev  # Development mode
npm start    # Production mode
```

### Testing
```bash
# Backend tests
cd backend
mvn test

# Frontend tests (Comprehensive Test Suite)
cd frontend
npm test                    # Run all tests
npm test -- --testNamePattern="navbar"  # Test specific component
npm test -- --coverage      # Generate coverage report
```

### 🆕 Frontend Test Coverage
Sistem testing frontend yang komprehensif dengan:
- **✅ 43 test cases** untuk Navbar component
- **✅ 200+ test cases** untuk semua komponen utama
- **✅ JSDOM environment** untuk DOM manipulation testing
- **✅ Mock APIs** untuk isolated testing
- **✅ Browser API mocks** (localStorage, sessionStorage, fetch)
- **✅ Error handling** dan edge case testing

**Test Files:**
- `navbar.test.js` - Navigation & menu testing
- `dashboard.test.js` - Dashboard functionality
- `students.test.js` - Student management (500+ lines)
- `teachers.test.js` - Teacher management (450+ lines)
- `users.test.js` - User management (500+ lines)
- `subjects.test.js` - Subject management (400+ lines)

## 📈 Performance & Scalability

- ✅ **Spring Boot** dengan optimal configuration
- ✅ **Redis caching** untuk performance
- ✅ **Database connection pooling**
- ✅ **Async processing** untuk heavy operations
- ✅ **Docker containerization** siap production

## 🔧 Troubleshooting

### Common Issues

1. **Port already in use**
   ```bash
   # Kill process on port 8080
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

2. **Database connection failed**
   ```bash
   # Check PostgreSQL service
   services.msc
   # Start PostgreSQL service
   ```

3. **Node modules issues**
   ```bash
   cd frontend
   rm -rf node_modules package-lock.json
   npm install
   ```

### Logs & Debugging
```bash
# Backend logs
cd backend
tail -f logs/spring.log

# Frontend logs
cd frontend
npm run dev  # Development with detailed logs

# Docker logs
docker-compose logs -f backend
```

## 📞 Support

Untuk pertanyaan atau masalah:
1. Check logs di folder `logs/`
2. Lihat dokumentasi di `TROUBLESHOOT.md`
3. Test dengan Postman collection
4. Check GitHub issues

## 🎯 Roadmap

### Fitur Mendatang
- [ ] Face recognition integration
- [ ] Advanced GPS tracking
- [ ] Mobile push notifications
- [ ] Parent mobile app
- [ ] Advanced reporting dashboard
- [ ] Integration dengan e-learning platform

---

**🎓 SIM SEKOLAH SMK SIJA - Ready for Production! 🚀**