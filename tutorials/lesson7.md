# Pembelajaran 7: Implementasi Fitur Mobile dan Koneksi ke Backend

## Pengenalan
Pembelajaran ini membahas implementasi fitur utama di aplikasi Flutter mobile untuk SIM Sekolah, seperti screen login, dashboard, manajemen siswa (list, add, edit, delete), dan integrasi dengan backend Java Spring Boot. Kita akan menggunakan package http untuk melakukan HTTP requests ke API REST, shared_preferences untuk menyimpan JWT token secara lokal (mirip localStorage di web), dan model Dart untuk parse JSON response. Fokus pada state management sederhana dengan setState, navigation dengan Navigator, dan error handling untuk koneksi jaringan.

Konsep kunci: Flutter app adalah tree of widgets; StatefulWidget untuk UI yang berubah (e.g., list students). FutureBuilder atau async/await untuk handle async API calls. Provider bisa digunakan untuk global state seperti token, tapi di sini gunakan shared_preferences untuk simplicity. Estimasi waktu: 6-8 jam. Pastikan lesson 6 selesai, proyek Flutter di mobile/ running, dan backend di localhost:8080 dengan endpoint /api/auth/signin dan /api/students (GET/POST/PUT/DELETE) dari lesson 5.

Troubleshooting umum: API call gagal? Check URL (ganti localhost dengan 10.0.2.2 untuk Android emulator). Token expired? Implement refresh atau logout. Permission denied? Tambah internet permission di android/app/src/main/AndroidManifest.xml <uses-permission android:name="android.permission.INTERNET" />.

## Langkah 1: Setup Package dan Model
Pastikan pubspec.yaml sudah punya dependencies dari lesson 6. Tambah jika perlu:
```yaml
dependencies:
  flutter:
    sdk: flutter
  http: ^1.1.0  # Update ke versi terbaru untuk HTTP client
  shared_preferences: ^2.2.2  # Simpan token, user data
  intl: ^0.18.1  # Format date untuk birthDate
  json_annotation: ^4.8.1  # Untuk JSON serialization (opsional, gunakan fromJson manual)
dev_dependencies:
  json_serializable: ^6.7.1  # Jika gunakan auto serialization
  build_runner: ^2.4.7
```
Jalankan `flutter pub get`. Untuk serialization, run `flutter packages pub run build_runner build` jika gunakan json_annotation.

Buat folder lib/models/ dan buat student_model.dart:
```dart
class Student {
  final int id;
  final String name;
  final String email;
  final String? birthDate;  // Format YYYY-MM-DD
  final String? phone;

  Student({
    required this.id,
    required this.name,
    required this.email,
    this.birthDate,
    this.phone,
  });

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      birthDate: json['birthDate'],
      phone: json['phone'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'email': email,
      'birthDate': birthDate,
      'phone': phone,
    };
  }
}
```
Penjelasan: fromJson parse JSON ke object. toJson untuk send ke backend. ? untuk nullable fields. Buat model serupa untuk User, Grade, dll., jika perlu.

Buat lib/services/auth_service.dart untuk centralize API calls:
```dart
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/student.dart';

class AuthService {
  static const String API_BASE = 'http://10.0.2.2:8080/api';  // 10.0.2.2 for Android emulator

  Future<String?> login(String username, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$API_BASE/auth/signin'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'username': username, 'password': password}),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final token = data['token'];
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('token', token);
        await prefs.setString('username', data['username']);
        return token;
      }
      return null;
    } catch (e) {
      print('Login error: $e');
      return null;
    }
  }

  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
    await prefs.remove('username');
  }

  Future<List<Student>> getStudents() async {
    final token = await getToken();
    if (token == null) throw Exception('No token');

    final response = await http.get(
      Uri.parse('$API_BASE/students'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((json) => Student.fromJson(json)).toList();
    } else {
      if (response.statusCode == 401) logout();
      throw Exception('Failed to load students: ${response.statusCode}');
    }
  }

  Future<void> createStudent(Student student) async {
    final token = await getToken();
    if (token == null) throw Exception('No token');

    final response = await http.post(
      Uri.parse('$API_BASE/students'),
      headers: {'Content-Type': 'application/json', 'Authorization': 'Bearer $token'},
      body: json.encode(student.toJson()),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to create student: ${response.statusCode}');
    }
  }

  // Similar for update and delete
  Future<void> updateStudent(int id, Student student) async {
    final token = await getToken();
    if (token == null) throw Exception('No token');

    final response = await http.put(
      Uri.parse('$API_BASE/students/$id'),
      headers: {'Content-Type': 'application/json', 'Authorization': 'Bearer $token'},
      body: json.encode(student.toJson()),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to update student: ${response.statusCode}');
    }
  }

  Future<void> deleteStudent(int id) async {
    final token = await getToken();
    if (token == null) throw Exception('No token');

    final response = await http.delete(
      Uri.parse('$API_BASE/students/$id'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to delete student: ${response.statusCode}');
    }
  }
}
```
Penjelasan: Service centralize API logic, handle token, error. 10.0.2.2 adalah special IP untuk emulator akses host machine. Throw exception untuk handle di UI.

Troubleshooting: Connection refused? Backend tidak running atau salah IP (untuk physical device gunakan WiFi IP). JSON parse error? Check response body di print.

## Langkah 2: Implement Login Screen
Buat folder lib/screens/ dan lib/services/. Edit main.dart untuk navigation:
```dart
import 'package:flutter/material.dart';
import 'screens/login_screen.dart';
import 'screens/dashboard_screen.dart';

void main() {
  runApp(const SimSekolahApp());
}

class SimSekolahApp extends StatelessWidget {
  const SimSekolahApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SIM Sekolah Mobile',
      theme: ThemeData(primarySwatch: Colors.blue),
      initialRoute: '/login',
      routes: {
        '/login': (context) => const LoginScreen(),
        '/dashboard': (context) => const DashboardScreen(),
      },
    );
  }
}
```
Penjelasan: routes map path ke widget. initialRoute start dengan login.

Buat lib/screens/login_screen.dart:
```dart
import 'package:flutter/material.dart';
import '../services/auth_service.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final AuthService _authService = AuthService();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;
  String? _error;

  Future<void> _login() async {
    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      final token = await _authService.login(
        _usernameController.text,
        _passwordController.text,
      );
      if (token != null) {
        Navigator.pushReplacementNamed(context, '/dashboard');
      } else {
        setState(() { _error = 'Login gagal. Cek username/password.'; });
      }
    } catch (e) {
      setState(() { _error = 'Error: $e'; });
    } finally {
      setState(() { _isLoading = false; });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Login SIM Sekolah')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: _usernameController,
              decoration: const InputDecoration(labelText: 'Username', border: OutlineInputBorder()),
              keyboardType: TextInputType.text,
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _passwordController,
              decoration: const InputDecoration(labelText: 'Password', border: OutlineInputBorder()),
              obscureText: true,
              keyboardType: TextInputType.visiblePassword,
            ),
            const SizedBox(height: 16),
            if (_isLoading) const CircularProgressIndicator(),
            if (_error != null) Text(_error!, style: const TextStyle(color: Colors.red)),
            ElevatedButton(
              onPressed: _isLoading ? null : _login,
              child: const Text('Login'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }
}
```
Penjelasan: TextEditingController manage input. setState update UI untuk loading dan error. Padding untuk spacing. dispose() clean resources.

Troubleshooting: Button tidak work? Check onPressed. Keyboard tidak muncul? Check keyboardType.

## Langkah 3: Dashboard dan Load Data Siswa
Buat lib/screens/dashboard_screen.dart:
```dart
import 'package:flutter/material.dart';
import '../services/auth_service.dart';
import '../models/student.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final AuthService _authService = AuthService();
  List<Student> students = [];
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadStudents();
  }

  Future<void> _loadStudents() async {
    try {
      setState(() { _isLoading = true; _error = null; });
      final loadedStudents = await _authService.getStudents();
      setState(() { students = loadedStudents; });
    } catch (e) {
      setState(() { _error = 'Gagal load siswa: $e'; });
    } finally {
      setState(() { _isLoading = false; });
    }
  }

  Future<void> _logout() async {
    await _authService.logout();
    Navigator.pushReplacementNamed(context, '/login');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard Siswa'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: _logout,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(_error!, style: const TextStyle(color: Colors.red)),
                    ElevatedButton(onPressed: _loadStudents, child: const Text('Coba Lagi')),
                  ],
                ))
              : students.isEmpty
                  ? const Center(child: Text('Tidak ada siswa'))
                  : RefreshIndicator(
                      onRefresh: _loadStudents,
                      child: ListView.builder(
                        itemCount: students.length,
                        itemBuilder: (context, index) {
                          final student = students[index];
                          return Card(
                            child: ListTile(
                              title: Text(student.name),
                              subtitle: Text(student.email),
                              trailing: Row(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  IconButton(
                                    icon: const Icon(Icons.edit),
                                    onPressed: () => _editStudent(student),
                                  ),
                                  IconButton(
                                    icon: const Icon(Icons.delete, color: Colors.red),
                                    onPressed: () => _deleteStudent(student.id),
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      ),
                    ),
      floatingActionButton: FloatingActionButton(
        onPressed: _addStudent,
        child: const Icon(Icons.add),
      ),
    );
  }

  void _addStudent() {
    // Navigate to add screen or show dialog
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Tambah Siswa'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(decoration: const InputDecoration(labelText: 'Nama')),
            TextField(decoration: const InputDecoration(labelText: 'Email')),
            // Add more fields
          ],
        ),
        actions: [
          ElevatedButton(onPressed: () => Navigator.pop(context), child: const Text('Batal')),
          ElevatedButton(onPressed: () async {
            // Implement create with _authService.createStudent
            Navigator.pop(context);
            _loadStudents();
          }, child: const Text('Simpan')),
        ],
      ),
    );
  }

  void _editStudent(Student student) {
    // Similar to add, prefill fields
  }

  Future<void> _deleteStudent(int id) async {
    if (await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Hapus Siswa'),
        content: const Text('Yakin hapus siswa ini?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Batal')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Hapus')),
        ],
      ),
    ) == true) {
      try {
        await _authService.deleteStudent(id);
        _loadStudents();
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error: $e')));
      }
    }
  }
}
```
Penjelasan: ListView.builder untuk scrollable list. Card dan ListTile untuk UI Material. RefreshIndicator untuk pull-to-refresh. AlertDialog untuk confirm delete. FloatingActionButton untuk add. ScaffoldMessenger untuk toast message error.

Troubleshooting: List tidak update? Call setState. Navigation error? Check routes di main.dart.

## Langkah 4: Koneksi ke Backend
- Ganti localhost:8080 dengan 10.0.2.2:8080 untuk emulator Android (alias ke host). Untuk physical device, gunakan IP komputer (ipconfig) dan pastikan backend bind 0.0.0.0.
- Handle error dengan try-catch dan UI feedback (loading, error message).
- Loading state dengan FutureBuilder:
```dart
FutureBuilder<List<Student>>(
  future: _authService.getStudents(),
  builder: (context, snapshot) {
    if (snapshot.connectionState == ConnectionState.waiting) {
      return const CircularProgressIndicator();
    } else if (snapshot.hasError) {
      return Text('Error: ${snapshot.error}');
    } else if (snapshot.hasData) {
      return ListView.builder(...);
    } else {
      return const Text('No data');
    }
  },
)
```
Penjelasan: FutureBuilder handle async lifecycle. connectionState untuk loading, hasError untuk error.

Untuk refresh token jika expired, tambah method di AuthService.

Troubleshooting: SSL error? Gunakan http bukan https untuk local. Permission internet sudah di Manifest.

## Latihan
1. Buat lib/services/auth_service.dart dengan method login, getStudents, createStudent, updateStudent, deleteStudent.
2. Buat lib/models/student.dart dengan fromJson/toJson.
3. Implement login_screen.dart dengan form validation (e.g., empty check), loading spinner, error text.
4. Implement dashboard_screen.dart dengan ListView students, add/edit/delete dengan dialog, logout button.
5. Test app: Run backend, flutter run, login, load students, add new, delete, check shared_preferences (flutter run -v untuk verbose log).
6. Tambah error handling: Jika no token, redirect login; pull-to-refresh; snackbar untuk success/error.

Tips: Gunakan Provider untuk global auth state jika app besar. Test di physical device dengan `flutter run -d <device-id>`. Commit changes, tambah .gitignore untuk /build/.

Lanjut ke Pembelajaran 8 untuk deployment dan testing akhir.