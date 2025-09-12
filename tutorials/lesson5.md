# Pembelajaran 5: Integrasi Frontend dengan Backend

## Pengenalan
Pembelajaran ini membahas integrasi mendalam antara frontend JavaScript statis dengan backend Java Spring Boot menggunakan API RESTful. Integrasi ini memungkinkan frontend mengirim request HTTP (GET, POST, PUT, DELETE) ke endpoint backend, menerima JSON response, dan update UI dynamically tanpa reload halaman. Kita akan menggunakan native Fetch API (built-in browser) untuk AJAX calls, handle JWT token untuk authentication (dari lesson 3), dan implement form submission untuk create/update data seperti siswa. Fokus pada single-page application (SPA) sederhana dengan JS routing dasar.

Keuntungan: Decouple frontend-backend, skalabilitas, dan real-time feel. Tantangan: Handle async operations, error seperti 401 (unauthorized), dan CORS (sudah dihandle di SecurityConfig). Estimasi waktu: 4-6 jam. Pastikan backend running dengan endpoint /api/auth/signin dan /api/students (GET/POST), dan frontend dari lesson 4 siap dengan login dan dashboard.

Troubleshooting umum: Network error? Check backend port dan CORS. Token expired? Implement logout dan refresh. Use browser dev tools Network tab untuk debug request/response.

## Langkah 1: Setup API Endpoints di Backend
Sebelum integrasi, pastikan backend punya controller untuk data. Buat StudentController di backend/src/main/java/com/simsekolah/controller/StudentController.java jika belum ada:
```java
package com.simsekolah.controller;

import com.simsekolah.model.Student;
import com.simsekolah.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")  // Allow frontend origin, ubah jika perlu
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
}
```
Penjelasan: @RestController return JSON. @RequestBody parse JSON ke object Student. @PathVariable untuk URL param. @CrossOrigin allow frontend calls. Pastikan StudentService implement method dengan repository dari lesson 2. Test dengan Postman: GET /api/students expect array JSON.

Untuk auth, gunakan @PreAuthorize("hasRole('ADMIN')") pada method jika perlu, dari SecurityConfig lesson 3.

Troubleshooting: 404? Check @RequestMapping path. 500? Log error di console backend.

## Langkah 2: Handle Authentication di Frontend
1. Buat halaman login lengkap di frontend/views/login.html dengan form:
```html
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SIM Sekolah</title>
    <link rel="stylesheet" href="../public/css/style.css">
</head>
<body>
    <div id="header-placeholder"></div>
    <main class="login-container">
        <h2>Login Sistem</h2>
        <form id="loginForm">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">Login</button>
        </form>
        <p id="loginError" style="color: red; display: none;"></p>
    </main>
    <div id="footer-placeholder"></div>
    <script src="../public/js/partials.js"></script>
    <script src="../public/js/auth.js"></script>
</body>
</html>
```
Penjelasan: Form dengan required validation HTML5. Error div untuk tampil pesan gagal.

2. Di frontend/public/js/auth.js, implement login dengan fetch dan validation:
```javascript
// Base URL
const API_BASE = 'http://localhost:8080/api';

// Login function
async function login(username, password) {
  if (!username || !password) {
    showError('Username dan password wajib diisi');
    return;
  }

  try {
    const response = await fetch(`${API_BASE}/auth/signin`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      localStorage.setItem('userId', data.id);
      localStorage.setItem('username', data.username);
      window.location.href = 'index.html';  // Redirect ke dashboard
    } else {
      const errorData = await response.json();
      showError(errorData.message || 'Login gagal');
    }
  } catch (error) {
    console.error('Login error:', error);
    showError('Koneksi error, cek backend');
  }
}

// Show error message
function showError(message) {
  const errorEl = document.getElementById('loginError');
  errorEl.textContent = message;
  errorEl.style.display = 'block';
}

// Form submit handler
document.getElementById('loginForm').addEventListener('submit', function(e) {
  e.preventDefault();
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;
  login(username, password);
});

// Logout function
function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('userId');
  localStorage.removeItem('username');
  window.location.href = 'login.html';
}
```
Penjelasan: Prevent default form submit untuk async. Validate input sebelum fetch. localStorage simpan token (note: untuk production gunakan HttpOnly cookie untuk security). Catch error untuk network issues. showError tampil pesan user-friendly.

3. Untuk request authenticated, selalu tambah header Authorization. Buat helper function di auth.js:
```javascript
function getAuthHeaders() {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
  };
}
```
Gunakan di fetch lain: fetch(url, { headers: getAuthHeaders() }).

Penjelasan: Spread operator (...) conditional tambah header jika token ada. Ini centralize auth logic.

Troubleshooting: Token tidak tersimpan? Check response data.token. 401? Token invalid, implement auto-logout jika response.status == 401.

## Langkah 3: Load Data dari Backend
Di frontend/public/js/data.js, implement loadStudents dengan token dari localStorage. Asumsikan di index.html ada <table id="studentsTable"><tr><th>Nama</th><th>Email</th></tr></table>.

```javascript
// Load students
async function loadStudents() {
  const token = localStorage.getItem('token');
  if (!token) {
    alert('Harap login dulu');
    window.location.href = 'login.html';
    return;
  }

  try {
    const response = await fetch(`${API_BASE}/students`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const students = await response.json();
      displayStudents(students);
    } else if (response.status === 401) {
      alert('Session expired, login ulang');
      logout();
    } else {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
  } catch (error) {
    console.error('Load students error:', error);
    alert('Gagal load data siswa: ' + error.message);
  }
}

// Display students in table
function displayStudents(students) {
  const tableBody = document.getElementById('studentsTable').tBodies[0] || document.getElementById('studentsTable').createTBody();
  tableBody.innerHTML = students.map(student => `
    <tr>
      <td>${student.name}</td>
      <td>${student.email}</td>
      <td>${student.birthDate}</td>
      <td><button onclick="editStudent(${student.id})">Edit</button></td>
      <td><button onclick="deleteStudent(${student.id})">Hapus</button></td>
    </tr>
  `).join('');
}

// Call on page load if logged in
if (localStorage.getItem('token')) {
  window.onload = loadStudents;
}
```
Penjelasan: Check token sebelum fetch. Handle 401 dengan logout. map() generate row table. createTBody() jika table kosong. onload auto-load jika sudah login.

Troubleshooting: Data tidak tampil? Check JSON structure match display. Console error? Inspect response di Network tab.

## Langkah 4: Update Data dan Form Submission
Buat form add/edit student di index.html:
```html
<form id="studentForm">
  <input type="hidden" id="studentId">
  <div class="form-group">
    <label for="name">Nama:</label>
    <input type="text" id="name" required>
  </div>
  <div class="form-group">
    <label for="email">Email:</label>
    <input type="email" id="email" required>
  </div>
  <div class="form-group">
    <label for="birthDate">Tanggal Lahir:</label>
    <input type="date" id="birthDate">
  </div>
  <button type="submit">Simpan Siswa</button>
</form>
```

Di data.js:
```javascript
// Submit form
document.getElementById('studentForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  const id = document.getElementById('studentId').value;
  const student = {
    name: document.getElementById('name').value,
    email: document.getElementById('email').value,
    birthDate: document.getElementById('birthDate').value
  };

  try {
    const url = id ? `${API_BASE}/students/${id}` : `${API_BASE}/students`;
    const method = id ? 'PUT' : 'POST';
    const response = await fetch(url, {
      method,
      headers: getAuthHeaders(),
      body: JSON.stringify(student)
    });

    if (response.ok) {
      loadStudents();  // Reload list
      this.reset();  // Clear form
    } else {
      throw new Error('Gagal simpan');
    }
  } catch (error) {
    alert('Error: ' + error.message);
  }
});

// Edit student
function editStudent(id) {
  fetch(`${API_BASE}/students/${id}`, { headers: getAuthHeaders() })
    .then(response => response.json())
    .then(student => {
      document.getElementById('studentId').value = student.id;
      document.getElementById('name').value = student.name;
      document.getElementById('email').value = student.email;
      document.getElementById('birthDate').value = student.birthDate;
    });
}

// Delete (dari langkah 3)
async function deleteStudent(id) {
  if (confirm('Yakin hapus siswa?')) {
    try {
      const response = await fetch(`${API_BASE}/students/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
      });
      if (response.ok) {
        loadStudents();
      }
    } catch (error) {
      alert('Error delete: ' + error.message);
    }
  }
}
```
Penjelasan: Form submit detect id untuk update atau create. PUT untuk update. confirm() untuk delete safety. Reset form setelah sukses.

Troubleshooting: Form tidak submit? Check event listener attach setelah DOM load. Validation? Tambah JS check sebelum fetch.

## Langkah 5: Error Handling dan Security
Handle error global dengan window.onerror atau try-catch di function. Untuk 401, auto-logout. Tambah loading spinner:
```javascript
function showLoading(show) {
  document.getElementById('loading').style.display = show ? 'block' : 'none';
}
```
Di fetch: showLoading(true) sebelum, false setelah.

Security: Jangan simpan sensitive data di localStorage. Token di header saja. Sanitize input untuk XSS: gunakan textContent bukan innerHTML jika perlu.

Untuk routing sederhana (tanpa framework), gunakan hashchange event untuk switch views tanpa reload.

Troubleshooting: Infinite loop? Check async calls. Performance lambat? Cache data dengan localStorage untuk read-only.

## Latihan
1. Implement login form di login.html dengan validation JS (min length password).
2. Di index.html, tambah form add student dan table display, test loadStudents() setelah login.
3. Implement edit dan delete dengan confirm, handle error 401 dengan logout.
4. Tambah loading spinner dan global error handler.
5. Test integrasi: Login, add student, refresh page (token persist), load data, logout.
6. Tambah page students.html dengan search by name menggunakan query param di fetch (?name=john).

Tips: Gunakan Axios library jika fetch terlalu verbose: npm install axios (tapi karena statis, gunakan CDN). Log request dengan console.log untuk debug. Commit per fitur.

Lanjut ke Pembelajaran 6 untuk aplikasi Flutter mobile.