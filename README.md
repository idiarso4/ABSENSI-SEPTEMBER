# HRM & Payroll Management System

This is a migrated HRM (Human Resource Management) and Payroll system from Laravel/PHP to Spring Boot (backend) and Express.js (frontend).

## Project Structure

```
├── backend/              # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hrm/
│   │   │   │   ├── config/        # Security and JWT configuration
│   │   │   │   ├── controller/    # REST API controllers
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── exception/     # Exception handling
│   │   │   │   ├── model/         # JPA entities
│   │   │   │   ├── repository/    # Spring Data JPA repositories
│   │   │   │   ├── service/       # Business logic services
│   │   │   │   └── HrmBackendApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/             # Express.js frontend application
│   ├── public/           # Static assets
│   │   ├── css/
│   │   ├── js/
│   │   └── images/
│   ├── views/            # EJS templates
│   ├── server.js         # Express.js server
│   └── package.json
└── README.md
```

## Technology Stack

### Backend
- **Spring Boot 3.2.0** - Java framework for building web applications
- **Spring Security 6.x** - Authentication and authorization with modern lambda DSL
- **Spring Data JPA** - Database operations with Hibernate ORM
- **JJWT 0.12.5** - Secure token-based authentication
- **BCrypt** - Password encryption hashing
- **H2 Database** - In-memory database for development (easily configurable for production)
- **Java 17** - Modern Java features utilization

### Frontend
- **Express.js 4.x** - Node.js web application framework
- **EJS** - Embedded JavaScript templating engine
- **Bootstrap 5.3.0** - Mobile-first responsive CSS framework
- **JavaScript Validation Library** - Comprehensive client-side validation
- **Axios 1.x** - HTTP client for API requests

## Features

1. **User Management**
   - User authentication and authorization
   - Role-based access control (RBAC)

2. **Employee Management**
   - Employee CRUD operations
   - Department and designation management

3. **Attendance Management**
   - Clock-in/clock-out functionality
   - Attendance tracking and reporting

4. **Leave Management**
   - Leave application and approval workflow
   - Leave type configuration

5. **Payroll Management**
   - Salary calculation
   - Payslip generation
   - Benefit and deduction management

## SIM Sekolah – Penambahan Terbaru

Fitur frontend berikut telah ditambahkan untuk mendukung kebutuhan SIM Sekolah:

- Navigasi Baru di Navbar (frontend/views/partials/navbar.ejs)
  - Manajemen PKL: Absensi, Kunjungan, Kegiatan, Laporan Harian
  - Perizinan Siswa: Izin Masuk, Izin Keluar, Laporan Guru Piket
  - Manajemen BK: Penanganan Siswa, Daftar Kunjungan, Surat Kesepakatan
  - LMS: Absensi Guru, Materi, Daftar Soal, Ulangan
  - Departemen (admin-only) di menu Sistem → Pengaturan

- Absensi Face + Lokasi (OpenStreetMap/Leaflet)
  - Halaman: `/attendance/face` → `frontend/views/attendance-face.ejs`
  - Menggunakan kamera (getUserMedia) dan geolokasi (Geolocation API)
  - Peta OpenStreetMap via Leaflet (CDN)
  - Siap kirim payload: `{ snapshotBase64, lat, lng, timestamp, nisn? }` ke backend (`POST /api/attendance/face-checkin`)

- Update Foto Referensi Siswa (untuk face recognition)
  - Halaman admin: `/students/photo-update` → `frontend/views/students-photo-update.ejs`
  - Ambil foto dari kamera atau unggah file, simpan ke backend (`POST /api/students/{nisn}/photo`)

- Manajemen “Close Update Foto” (Admin)
  - Halaman: `/students/photo-update/manage` → `frontend/views/students-photo-update-manage.ejs`
  - Aksi: Tutup/Buka per siswa, per kelas, atau semua
  - Endpoint yang disarankan:
    - `POST /api/photo-updates/close` `{ scope: 'student'|'class'|'all', nisn?, classId? }`
    - `POST /api/photo-updates/open` `{ scope: 'student'|'class'|'all', nisn?, classId? }`
    - `GET  /api/photo-updates/status?classId=&q=` → daftar status per siswa (locked/open, updatedAt)

Catatan Role-based Access:
- Item “Departemen”, “Update Foto Siswa”, dan “Manajemen Update Foto” ditampilkan hanya untuk admin di navbar.

Persyaratan Browser:
- Pengguna perlu memberikan izin kamera dan lokasi pada browser.
- Leaflet/OSM di-load via CDN, tidak memerlukan API key.

Integrasi selanjutnya (opsional):
- Face recognition dapat diimplementasikan di backend (mis. OpenCV) atau frontend (mis. face-api.js) dengan menyimpan embedding, bukan gambar mentah.

## Getting Started

### Prerequisites
- Java 17 or higher (Java 21 recommended)
- Node.js 18 or higher (latest LTS preferred)
- Maven 3.8 or higher (Portable Maven included)
- Git (for version control)
- Web browser with JavaScript enabled

### Backend Setup

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Database Configuration:**
   - The application uses **H2 in-memory database** by default (data persists during runtime)
   - For **production deployment**, update `src/main/resources/application.properties`:
   ```properties
   # H2 Database (Development - Default)
   spring.datasource.url=jdbc:h2:mem:hrmdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=

   # MySQL/PostgreSQL (Production - Uncomment to use)
   # spring.datasource.url=jdbc:mysql://localhost:3306/hrm_db?useSSL=false&serverTimezone=UTC
   # spring.datasource.username=your_username
   # spring.datasource.password=your_password
   # spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   ```

3. **Run with portable Maven:**
   ```bash
   .\maven-portable\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run
   ```

4. **Access H2 Console:**
   - URL: `http://localhost:8080/h2-console`
   - Driver: `org.h2.Driver`
   - URL: `jdbc:h2:mem:hrmdb`
   - Username: `sa`
   - Password: (leave empty)

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the server:
   ```bash
   npm start
   ```

4. Access the application at `http://localhost:3000`

## Current System Status ✅

### **✅ VERIFIED FULLY OPERATIONAL (As of 2025-09-04)**

- **Backend**: Spring Boot 3.2.0 running on port 8080 with JWT authentication
- **Frontend**: Express.js server with EJS templates and Bootstrap 5.3.0 responsive design
- **Database**: H2 in-memory with pre-seeded data (2 users, 4 departments, 4 designations)
- **Security**: JWT + BCrypt authentication system working perfectly
- **API**: All REST endpoints functional with proper error handling
- **UI/UX**: Mobile-responsive design with comprehensive client-side validation

### **🎯 Ready for Development**

The system has completed all high-priority tasks:
- ✅ Frontend proxy verified working
- ✅ End-to-end authentication flow confirmed
- ✅ Database seeding verified (4 departments + 4 designations)
- ✅ Bootstrap 5.3.0 responsive design validated
- ✅ Comprehensive error handling and user feedback implemented

### **🏆 Pre-seeded Credentials**

**Administrator Account:**
- Email: `admin@sim.edu`
- Password: `admin123`
- Role: Admin

**Employee Account:**
- Email: `john.doe@company.com`
- Password: `employee123`
- Role: Employee

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Users
- `GET /api/users` - List all users
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Employees
- `GET /api/employees` - List all employees
- `POST /api/employees` - Create new employee
- `GET /api/employees/{id}` - Get employee by ID
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Departments
- `GET /api/departments` - List all departments
- `POST /api/departments` - Create new department
- `GET /api/departments/{id}` - Get department by ID
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

### Attendance
- `GET /api/attendance` - List attendance records
- `POST /api/attendance/clock-in` - Clock in
- `POST /api/attendance/clock-out` - Clock out
- `GET /api/attendance/employee/{employeeId}` - Get attendance by employee

### Leave
- `GET /api/leaves` - List leave applications
- `POST /api/leaves` - Submit leave application
- `GET /api/leaves/{id}` - Get leave by ID
- `PUT /api/leaves/{id}/approve` - Approve leave
- `PUT /api/leaves/{id}/reject` - Reject leave

### Payroll
- `GET /api/payrolls` - List payroll records
- `POST /api/payrolls` - Generate payroll
- `GET /api/payrolls/{id}` - Get payroll by ID
- `POST /api/payrolls/{id}/payment` - Record payment

## Testing

To run backend tests:
```bash
cd backend
mvn test
```

## Deployment

### Backend
1. Package the application:
   ```bash
   cd backend
   mvn clean package
   ```

2. Run the JAR file:
   ```bash
   java -jar target/hrm-backend-0.0.1-SNAPSHOT.jar
   ```

### Frontend
1. Install dependencies and build:
   ```bash
   cd frontend
   npm install
   npm run build
   ```

2. Run the server:
   ```bash
   npm start
   ```

## Default Credentials

For testing purposes, you can use the following default credentials:
- **Email**: admin@sim.edu
- **Password**: admin123

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to all contributors who participated in the migration from Laravel/PHP to Spring Boot/Express.js
# ABSENSI-SEPTEMBER
