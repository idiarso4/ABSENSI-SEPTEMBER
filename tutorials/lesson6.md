# Pembelajaran 6: Setup Flutter untuk Aplikasi Mobile

## Pengenalan
Pembelajaran ini mengajarkan cara setup Flutter untuk membangun aplikasi mobile SIM Sekolah. Flutter adalah UI toolkit open-source dari Google yang memungkinkan developer membangun aplikasi native untuk iOS, Android, web, dan desktop dari single codebase menggunakan Dart language. Keuntungan Flutter: Hot reload untuk development cepat, widget-rich (Material Design dan Cupertino), dan performa native karena compile ke machine code. Di proyek ini, folder mobile/ akan menjadi proyek Flutter yang terhubung ke backend Java via HTTP API, menggunakan package seperti http untuk call endpoint /api/students dan handle JWT authentication dari lesson 3.

Konsep kunci: Flutter app terdiri dari widgets (StatelessWidget untuk static UI, StatefulWidget untuk dynamic). pubspec.yaml mengelola dependencies seperti package eksternal. Estimasi waktu: 3-5 jam. Pastikan lesson 5 selesai, backend running. Flutter memerlukan Android Studio untuk emulator, atau physical device.

Troubleshooting umum: Flutter doctor red? Install missing tools. Build gagal? Check pub get dan clean project.

## Langkah 1: Install Flutter SDK
1. Download Flutter SDK terbaru dari situs resmi https://docs.flutter.dev/get-started/install/windows (untuk Windows, sesuaikan OS). Pilih stable channel untuk production.

2. Extract ZIP ke folder tetap seperti C:\flutter (hindari path dengan space). Jangan gunakan Program Files untuk avoid permission issue.

3. Tambah C:\flutter\bin ke system PATH environment variable:
   - Buka System Properties > Advanced > Environment Variables.
   - Edit PATH di System variables, tambah new entry C:\flutter\bin.
   - Restart terminal atau VS Code.

4. Verifikasi instalasi: Jalankan `flutter doctor` di command prompt. Ini check Flutter version, Dart, connected devices, dan tools seperti Android toolchain, VS Code extension, Chrome untuk web.
   - Expected output: Green check untuk Flutter dan Dart. Jika Android Studio belum install, download dari https://developer.android.com/studio.
   - Install VS Code Flutter extension dari marketplace.

Penjelasan: flutter doctor diagnose environment. Jika Android SDK missing, install Android Studio dan accept licenses dengan `flutter doctor --android-licenses`.

Troubleshooting: PATH tidak work? Restart computer. `flutter` command not found? Check echo %PATH% include C:\flutter\bin. Antivirus block? Allow flutter in exceptions.

## Langkah 2: Buat Proyek Flutter
1. Buka terminal di direktori workspace proyek (c:/Users/sija_003/Desktop/SIM_CLONE).

2. Jalankan `flutter create mobile` untuk generate proyek baru di folder mobile/. Ini buat structure lengkap dengan example counter app.

3. Masuk ke folder: `cd mobile`.

4. Jalankan `flutter pub get` untuk download dependencies dari pub.dev. Ini seperti mvn install di Java.

Struktur proyek yang di-generate:
- pubspec.yaml: File konfigurasi, include name, description, dependencies (flutter sdk), dev_dependencies (test tools).
- lib/: Source code Dart.
  - lib/main.dart: Entry point, runApp(MyApp()).
- test/: Unit tests.
- android/ and ios/: Platform-specific code.
- build/: Output build.

Contoh main.dart dasar untuk SIM Sekolah (edit lib/main.dart):
```dart
import 'package:flutter/material.dart';

void main() {
  runApp(const SimSekolahApp());
}

class SimSekolahApp extends StatelessWidget {
  const SimSekolahApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SIM Sekolah Mobile',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: const LoginScreen(),  // Start dengan login screen
      routes: {
        '/dashboard': (context) => const DashboardScreen(),
      },
    );
  }
}

// Example Login Screen (buat file lib/screens/login_screen.dart dan import)
class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Login SIM Sekolah'),
      ),
      body: const Center(
        child: Text('Halaman Login - Implement di lesson 7'),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Navigator.pushNamed(context, '/dashboard'),
        child: const Icon(Icons.login),
      ),
    );
  }
}

// Example Dashboard
class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Dashboard')),
      body: const Center(child: Text('Selamat datang di Dashboard!')),
    );
  }
}
```
Penjelasan: MaterialApp setup theme dan routes untuk navigation. Scaffold adalah basic layout dengan AppBar dan body. FloatingActionButton untuk action. Import 'package:flutter/material.dart'; selalu diperlukan.

Troubleshooting: Create gagal? Check disk space. pub get error? Run `flutter clean` lalu pub get lagi.

## Langkah 3: Tambah Dependencies
Edit pubspec.yaml di root mobile/ untuk tambah package. Indent penting (YAML format):
```yaml
name: mobile
description: A new Flutter project.

publish_to: 'none'

version: 1.0.0+1

environment:
  sdk: '>=3.0.0 <4.0.0'

dependencies:
  flutter:
    sdk: flutter
  cupertino_icons: ^1.0.2
  http: ^1.1.0  # Untuk API calls ke backend Java, update version terbaru
  shared_preferences: ^2.2.2  # Untuk simpan token dan user data lokal
  intl: ^0.18.1  # Untuk format date, number (untuk birthDate, amount)
  provider: ^6.1.1  # State management sederhana untuk app

dev_dependencies:
  flutter_test:
    sdk: flutter
  flutter_lints: ^3.0.0

flutter:
  uses-material-design: true
  assets:
    - assets/images/  # Jika ada images
```
Penjelasan: http untuk HTTP client. shared_preferences seperti localStorage di web. provider untuk manage state global seperti token. intl untuk localization. Jalankan `flutter pub get` setelah edit untuk install.

Untuk add asset, buat assets/images/ dan declare di flutter: assets.

Troubleshooting: Version conflict? Check pub.dev untuk compatible version. pub get stuck? Run `flutter pub cache repair`.

## Langkah 4: Jalankan App
1. Setup emulator: Buka Android Studio > AVD Manager > Create Virtual Device (pilih phone, API 30+).

2. Jalankan `flutter run` di mobile/. Flutter detect device/emulator dan install APK. Gunakan `flutter run -d chrome` untuk web test.

3. Untuk development: Buka VS Code di mobile/, install Flutter extension. Tekan F5 atau Run > Start Debugging untuk hot reload (changes apply tanpa rebuild).

4. Test: App tampil "Login SIM Sekolah" dengan button login. Click untuk navigasi ke dashboard.

Penjelasan: Hot reload simpan waktu, rebuild full untuk structural changes. `flutter devices` list available devices.

Troubleshooting: No device? Run `flutter devices`. Error "Gradle build failed"? Run `flutter clean` dan `flutter pub get`. Untuk iOS, butuh Mac.

## Latihan
1. Install Flutter SDK dan run `flutter doctor`, fix semua issue (install Android Studio jika perlu).
2. Buat proyek Flutter di mobile/ dengan `flutter create mobile --org com.simsekolah`.
3. Edit main.dart untuk tampilkan login screen sederhana dengan form username/password (gunakan TextField dan ElevatedButton).
4. Tambah dependency http dan shared_preferences, buat function test fetch ke /api/auth/signin (print response di console).
5. Test app di Android emulator: Run `flutter run`, navigasi screen, check hot reload work.
6. Setup VS Code untuk Flutter: Install extension, buat launch.json untuk debug, run dari IDE.

Tips: Gunakan `flutter analyze` untuk code quality. Commit proyek Flutter ke Git (tambah .gitignore untuk /build/). Untuk production, gunakan `flutter build apk --release`.

Lanjut ke Pembelajaran 7 untuk implement fitur mobile seperti login dan load data siswa.