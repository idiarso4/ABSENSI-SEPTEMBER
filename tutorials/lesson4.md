# Pembelajaran 4: Membangun Frontend (JavaScript-based)

## Pengenalan
Pembelajaran ini membahas pembangunan frontend aplikasi SIM_CLONE menggunakan teknologi web sederhana: HTML untuk struktur, CSS untuk styling, dan JavaScript untuk interaksi dan dynamic content. Frontend ini bersifat statis, artinya tidak memerlukan server-side rendering seperti React atau Vue, melainkan di-serve sebagai file statis yang terhubung ke backend Java melalui API REST. Struktur folder frontend/ mencakup public/css untuk stylesheet, public/js untuk script, views/ untuk halaman HTML utama, dan views/partials/ untuk komponen reusable seperti header dan footer. Pendekatan ini cocok untuk tutorial dasar, mengajarkan konsep MVC (Model-View-Controller) di client-side di mana backend sebagai model, views sebagai tampilan, dan JS sebagai controller.

Keuntungan frontend statis: Mudah deploy, performa cepat, dan mudah dipahami siswa pemula. Namun, untuk app kompleks, pertimbangkan framework seperti React nanti. Kita akan fokus pada layout responsif, form handling, dan AJAX calls ke backend. Estimasi waktu: 3-5 jam. Pastikan backend dari lesson 3 running di localhost:8080 dengan API /api/auth dan /api/students tersedia.

Troubleshooting umum: Browser cache? Hard refresh Ctrl+F5. CORS error? Sudah dihandle di SecurityConfig lesson 3.

## Langkah 1: Setup Struktur Frontend
1. Navigasi ke folder frontend/ di proyek root. Jika kosong, buat subfolder: public/, public/css/, public/js/, views/, views/partials/.

2. Buat file utama index.html di frontend/views/. Ini adalah entry point app, load CSS dan JS.

3. Buat partials untuk modularitas: header.html, footer.html, sidebar.html di views/partials/. Partials adalah snippet HTML yang di-include dynamically menggunakan JavaScript atau server-side (tapi karena statis, gunakan JS).

Contoh struktur lengkap:
- frontend/views/index.html (halaman dashboard)
- frontend/views/login.html (halaman login)
- frontend/views/partials/header.html (navigasi bar)
- frontend/views/partials/footer.html (copyright dan links)
- frontend/public/css/style.css (global styles)
- frontend/public/js/script.js (common functions)
- frontend/public/js/auth.js (login logic)
- frontend/public/js/data.js (API calls untuk students, etc.)

Penjelasan: Struktur ini memisahkan concern: views untuk UI, public untuk assets statis. Gunakan relative path seperti "../public/css/style.css" dari views/.

Troubleshooting: Path salah? Gunakan absolute path dari root atau serve dengan live server di VS Code extension "Live Server".

## Langkah 2: Buat Halaman Utama
Buat frontend/views/index.html sebagai halaman dashboard utama. Include partials menggunakan JavaScript (karena no server-side):
```html
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIM Sekolah - Dashboard</title>
    <link rel="stylesheet" href="../public/css/style.css">
</head>
<body>
    <div id="header-placeholder"></div>  <!-- Placeholder untuk header -->
    <main>
        <h1>Selamat Datang di SIM Sekolah</h1>
        <div id="students-list"></div>  <!-- Tempat tampilkan data siswa -->
        <button onclick="loadStudents()">Load Siswa</button>
    </main>
    <div id="footer-placeholder"></div>
    <script src="../public/js/partials.js"></script>  <!-- Script untuk load partials -->
    <script src="../public/js/data.js"></script>
</body>
</html>
```
Penjelasan: <meta viewport> untuk responsif mobile. Placeholder diisi dengan JS. Button trigger function dari data.js.

Untuk load partials, buat frontend/public/js/partials.js:
```javascript
function loadPartials() {
  fetch('../views/partials/header.html')
    .then(response => response.text())
    .then(html => document.getElementById('header-placeholder').innerHTML = html);
  fetch('../views/partials/footer.html')
    .then(response => response.text())
    .then(html => document.getElementById('footer-placeholder').innerHTML = html);
}

window.onload = loadPartials;
```
Penjelasan: Fetch load HTML partial dan insert ke DOM. Ini simulasi include.

Contoh header.html di views/partials/header.html:
```html
<header class="header">
    <nav>
        <a href="index.html">Dashboard</a>
        <a href="login.html">Login</a>
        <a href="students.html">Siswa</a>
    </nav>
</header>
```

Troubleshooting: Fetch gagal? Check path relative. Gunakan VS Code Live Server untuk serve dari frontend/.

## Langkah 3: Styling dengan CSS
Buat frontend/public/css/style.css untuk tampilan profesional. Gunakan CSS Grid atau Flexbox untuk layout.
```css
/* Reset dan global */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: 'Arial', sans-serif;
    line-height: 1.6;
    color: #333;
    background-color: #f4f4f4;
}

.header {
    background-color: #007bff;
    color: white;
    padding: 1rem;
    text-align: center;
}

.header nav a {
    color: white;
    margin: 0 1rem;
    text-decoration: none;
}

.header nav a:hover {
    text-decoration: underline;
}

main {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 1rem;
}

#students-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
}

.student-card {
    background: white;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

footer {
    background-color: #333;
    color: white;
    text-align: center;
    padding: 1rem;
    margin-top: 2rem;
}

/* Responsif */
@media (max-width: 768px) {
    .header nav a {
        display: block;
        margin: 0.5rem 0;
    }
}
```
Penjelasan: Grid untuk list siswa. Media query untuk mobile. Gunakan Bootstrap jika ingin cepat, tapi di sini vanilla CSS untuk belajar dasar.

Troubleshooting: Style tidak apply? Check selector dan path CSS. Gunakan browser dev tools (F12) untuk inspect.

## Langkah 4: JavaScript untuk Interaksi
Buat frontend/public/js/script.js untuk common functions, dan data.js untuk API logic. Gunakan modern ES6 dengan async/await.

Di public/js/data.js:
```javascript
// Base URL backend
const API_BASE = 'http://localhost:8080/api';

// Function load students dengan auth token
async function loadStudents(token = null) {
  const headers = {
    'Content-Type': 'application/json'
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(`${API_BASE}/students`, { headers });
    if (!response.ok) {
      throw new Error('Gagal load data');
    }
    const students = await response.json();
    displayStudents(students);
  } catch (error) {
    console.error('Error:', error);
    alert('Error loading students: ' + error.message);
  }
}

// Tampilkan students di UI
function displayStudents(students) {
  const list = document.getElementById('students-list');
  list.innerHTML = students.map(student => `
    <div class="student-card">
      <h3>${student.name}</h3>
      <p>Email: ${student.email}</p>
      <button onclick="deleteStudent(${student.id})">Hapus</button>
    </div>
  `).join('');
}

// Contoh delete
async function deleteStudent(id) {
  const token = localStorage.getItem('token');
  if (confirm('Hapus siswa?')) {
    await fetch(`${API_BASE}/students/${id}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    loadStudents(token);  // Reload list
  }
}
```
Penjelasan: Fetch dengan headers untuk auth. Try-catch untuk error handling. map() untuk generate HTML dari data JSON. localStorage untuk simpan token dari login.

Di public/js/auth.js untuk login:
```javascript
async function login(username, password) {
  try {
    const response = await fetch(`${API_BASE}/auth/signin`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      window.location.href = 'index.html';
    } else {
      alert('Login gagal');
    }
  } catch (error) {
    console.error('Login error:', error);
  }
}
```
Penjelasan: POST login, simpan token jika sukses, redirect.

Troubleshooting: Fetch CORS? Pastikan backend allow origin. Token invalid? Check expiration di JwtUtils.

## Langkah 5: Menggunakan Partials
Karena frontend statis, gunakan JS untuk load partials seperti di langkah 2. Alternatif: Gunakan template engine seperti Handlebars.js (tambah CDN di HTML):
```html
<script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
```
Buat template untuk student card di partials/student-template.hbs:
```handlebars
<div class="student-card">
  <h3>{{name}}</h3>
  <p>{{email}}</p>
</div>
```
Di script.js:
```javascript
const template = Handlebars.compile(document.getElementById('student-template').innerHTML);
const html = template(studentData);
document.getElementById('students-list').innerHTML = html;
```
Penjelasan: Handlebars compile template dan render dengan data, lebih efisien untuk list dinamis.

Troubleshooting: Template tidak load? Check script load order.

## Latihan
1. Setup struktur folder frontend/ lengkap dengan views untuk login, dashboard, students.html.
2. Buat partials/header.html, footer.html, dan load dengan JS di semua halaman.
3. Tambah CSS lengkap untuk layout grid responsif, termasuk tema biru untuk sekolah.
4. Implement JS untuk login form di login.html (input fields, button call login()).
5. Buat halaman students.html dengan button loadStudents(), tampilkan list dengan cards.
6. Test lokal: Jalankan Live Server di frontend/, login, load data (pastikan backend running), check console untuk error.
7. Tambah form add student di dashboard dengan fetch POST.

Tips: Gunakan Bootstrap CDN untuk styling cepat: <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">. Validasi form dengan JS sebelum submit. Commit per file ke Git.

Lanjut ke Pembelajaran 5 untuk integrasi mendalam dengan backend.