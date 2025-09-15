# SIM Sekolah API - Postman Collection & Sample Files

## Overview
This directory contains a comprehensive Postman collection for testing the SIM Sekolah API, along with sample data files for import testing.

## Files Included

### Postman Collection
- `SIM_Sekolah_Postman_Collection.json` - Complete Postman collection with all API endpoints

### Sample Data Files
- `sample_students.csv` - Sample student data for import testing
- `sample_teachers.csv` - Sample teacher data for import testing
- `sample_subjects.csv` - Sample subject data for import testing
- `sample_classrooms.csv` - Sample classroom data for import testing

## How to Use

### 1. Import Postman Collection
1. Open Postman
2. Click "Import" button
3. Select "File" tab
4. Choose `SIM_Sekolah_Postman_Collection.json`
5. Click "Import"

### 2. Configure Environment Variables
Create a new environment in Postman with these variables:
- `base_url`: `http://localhost:8080` (or your server URL)
- `auth_token`: (will be set automatically after login)
- `refresh_token`: (will be set automatically after login)

### 3. Authentication Flow
1. Use the "Login" request in the Authentication folder
2. Update the credentials as needed (default: admin@simsekolah.com / admin123)
3. Execute the login - tokens will be saved automatically
4. All other requests will use the saved auth token

### 4. Testing Import Functionality
1. Start with the template download endpoints to get properly formatted Excel files
2. Use the sample CSV files as reference for data format
3. Test both `dryRun=true` (validation only) and `dryRun=false` (actual import)
4. Test both `mode=create` (insert only) and `mode=upsert` (update if exists)

## API Endpoints Covered

### Authentication
- POST /api/v1/auth/login
- POST /api/v1/auth/refresh
- GET /api/v1/auth/me
- GET /api/v1/auth/validate
- POST /api/v1/auth/change-password
- POST /api/v1/auth/logout

### Students Management
- CRUD operations (Create, Read, Update, Delete)
- Advanced search and filtering
- Class assignment operations
- Excel import/export with dry-run and upsert modes
- Statistics and reporting

### Teachers Management
- CRUD operations
- Search and filtering
- Excel import/export
- Department-based queries

### Subjects Management
- CRUD operations
- Type-based filtering (THEORY, PRACTICE, MIXED)
- Excel import/export
- Statistics

### Class Rooms Management
- CRUD operations
- Grade and major-based filtering
- Excel import/export
- Capacity management

### Dashboard & Reports
- Summary statistics
- Student, attendance, and grade reports
- Excel export functionality

### System Health
- Health checks
- System information

## Sample Data Format

### Students CSV Format
```csv
NIS,Nama Lengkap,Kelas,Tempat Lahir,Tanggal Lahir,Jenis Kelamin,Agama,Alamat,Nama Ayah,Nama Ibu,No HP Orang Tua,Alamat Orang Tua,Tahun Masuk,Asal Sekolah,Status
12345678,Ahmad Rahman,X IPA 1,Jakarta,2005-05-15,LAKI_LAKI,Islam,Jl. Sudirman No. 123,Rahman Ahmad,Siti Aminah,08123456789,Jl. Sudirman No. 123,2023,SMP Negeri 1 Jakarta,ACTIVE
87654321,Sari Dewi,X IPS 2,Bandung,2005-08-20,PEREMPUAN,Islam,Jl. Asia Afrika No. 456,Dewi Sari,Aminah Sari,08198765432,Jl. Asia Afrika No. 456,2023,SMP Negeri 2 Bandung,ACTIVE
```

### Teachers CSV Format
```csv
NIP,Full Name,Email,Phone,Address,Status
1980010112345678,Dr. Siti Nurhaliza,siti.nurhaliza@school.com,08123456789,Jl. Education No. 123,ACTIVE
1985010123456789,Drs. Ahmad Rahman,ahmad.rahman@school.com,08198765432,Jl. Knowledge No. 456,ACTIVE
```

### Subjects CSV Format
```csv
Code,Name,Type,Description
MAT101,Matematika Dasar,THEORY,Mata pelajaran matematika tingkat dasar
BIO101,Biologi Dasar,PRACTICE,Mata pelajaran biologi dengan praktikum
ENG101,Bahasa Inggris,THEORY,Mata pelajaran bahasa inggris komunikatif
```

### Class Rooms CSV Format
```csv
Class Name,Grade Level,Academic Year,Semester,Capacity,Location,Status,Major,Homeroom Teacher
X IPA 1,10,2024/2025,GANJIL,30,Gedung A Lantai 2,ACTIVE,IPA,1980010112345678
X IPS 1,10,2024/2025,GANJIL,32,Gedung A Lantai 3,ACTIVE,IPS,1985010123456789
XI IPA 1,11,2024/2025,GANJIL,28,Gedung B Lantai 1,ACTIVE,IPA,1980010112345678
```

## Import Features

### Dry Run Mode
- Set `dryRun=true` to validate data without saving
- Returns detailed validation results
- Shows potential errors and warnings
- Safe for testing data quality

### Upsert Mode
- Set `mode=upsert` to update existing records
- Creates new records if they don't exist
- Updates existing records based on unique keys:
  - Students: NIS
  - Teachers: NIP
  - Subjects: Code
  - Class Rooms: Name

### Error Handling
- Detailed error messages per row
- Field-level validation errors
- Duplicate detection
- Capacity validation for class assignments

## Testing Workflow

1. **Authentication**: Login first to get JWT token
2. **Download Templates**: Get Excel templates for proper format
3. **Dry Run Import**: Test data validation without saving
4. **Actual Import**: Perform real import with `dryRun=false`
5. **Verify Results**: Check created/updated records
6. **Export Data**: Verify data was imported correctly

## Troubleshooting

### Common Issues
- **401 Unauthorized**: Check if login was successful and token is valid
- **400 Bad Request**: Check request body format and required fields
- **404 Not Found**: Verify endpoint URL and HTTP method
- **500 Internal Server Error**: Check server logs for detailed error information

### Import Errors
- **File format errors**: Ensure .xlsx format and proper headers
- **Validation errors**: Check required fields and data types
- **Duplicate errors**: Use upsert mode or remove duplicates
- **Capacity errors**: Check class room capacity before assignment

## Support

For API documentation and additional help:
- Check the Swagger UI at `http://localhost:8080/swagger-ui.html`
- Review the roadmap.md file for feature status
- Check application logs for detailed error information