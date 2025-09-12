// Routes untuk sistem penilaian (assessments)
// Base URL: /api/assessments

// 1. Mendapatkan semua penilaian
GET /api/assessments

// 2. Mendapatkan penilaian berdasarkan ID
GET /api/assessments/{id}

// 3. Membuat penilaian baru
POST /api/assessments
Content-Type: application/json

// 4. Memperbarui penilaian berdasarkan ID
PUT /api/assessments/{id}
Content-Type: application/json

// 5. Menghapus penilaian berdasarkan ID
DELETE /api/assessments/{id}

// 6. Mendapatkan penilaian berdasarkan ID siswa
GET /api/assessments/student/{studentId}

// 7. Mendapatkan penilaian berdasarkan ID mata pelajaran
GET /api/assessments/subject/{subjectId}

// 8. Mendapatkan penilaian berdasarkan ID siswa dan ID mata pelajaran
GET /api/assessments/student/{studentId}/subject/{subjectId}

/*
Contoh struktur body untuk POST dan PUT requests:
{
  "studentId": 123,
  "subjectId": 456,
  "assessmentType": "UH", // UH, UTS, UAS, TUGAS, PROJECT, dll.
  "assessmentDate": "2023-10-15T08:30:00",
  "maxScore": 100.0,
  "score": 85.5,
  "description": "Ulangan Harian Bab 1"
}
*/