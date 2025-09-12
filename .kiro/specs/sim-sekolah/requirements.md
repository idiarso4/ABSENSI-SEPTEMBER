# Requirements Document - Transformasi HRM ke SIM Sekolah

## Introduction

Transformasi aplikasi HRM dan Penggajian menjadi Sistem Informasi Manajemen (SIM) Sekolah dengan mempertahankan fondasi teknologi yang sama namun mengubah domain bisnis dari manajemen karyawan ke manajemen sekolah. Aplikasi akan mengelola data siswa, guru, kelas, mata pelajaran, nilai, dan administrasi sekolah lainnya.

## Requirements

### Requirement 1 - Manajemen Data Siswa

**User Story:** Sebagai admin sekolah, saya ingin mengelola data siswa, sehingga saya dapat memiliki database lengkap semua siswa di sekolah.

#### Acceptance Criteria

1. WHEN admin mengakses menu siswa THEN sistem SHALL menampilkan daftar semua siswa
2. WHEN admin menambah siswa baru THEN sistem SHALL menyimpan data siswa dengan NISN, nama, kelas, alamat, dan data orang tua
3. WHEN admin mengedit data siswa THEN sistem SHALL memperbarui informasi siswa yang dipilih
4. WHEN admin menghapus data siswa THEN sistem SHALL menghapus siswa dari database setelah konfirmasi
5. WHEN admin mencari siswa THEN sistem SHALL menampilkan hasil pencarian berdasarkan nama atau NISN

### Requirement 2 - Manajemen Data Guru dan Staff

**User Story:** Sebagai admin sekolah, saya ingin mengelola data guru dan staff, sehingga saya dapat memiliki informasi lengkap tentang tenaga pendidik dan kependidikan.

#### Acceptance Criteria

1. WHEN admin mengakses menu guru THEN sistem SHALL menampilkan daftar semua guru dan staff
2. WHEN admin menambah guru baru THEN sistem SHALL menyimpan data guru dengan NIP, nama, mata pelajaran, dan informasi kontak
3. WHEN admin mengassign guru ke mata pelajaran THEN sistem SHALL menyimpan relasi guru-mata pelajaran
4. WHEN admin melihat profil guru THEN sistem SHALL menampilkan detail lengkap termasuk jadwal mengajar

### Requirement 3 - Manajemen Kelas dan Mata Pelajaran

**User Story:** Sebagai admin sekolah, saya ingin mengelola kelas dan mata pelajaran, sehingga saya dapat mengorganisir struktur akademik sekolah.

#### Acceptance Criteria

1. WHEN admin membuat kelas baru THEN sistem SHALL menyimpan data kelas dengan nama, tingkat, dan wali kelas
2. WHEN admin menambah mata pelajaran THEN sistem SHALL menyimpan mata pelajaran dengan kode, nama, dan SKS
3. WHEN admin mengassign siswa ke kelas THEN sistem SHALL memperbarui data kelas siswa
4. WHEN admin melihat daftar kelas THEN sistem SHALL menampilkan semua kelas dengan jumlah siswa

### Requirement 4 - Sistem Penilaian dan Rapor

**User Story:** Sebagai guru, saya ingin menginput dan mengelola nilai siswa, sehingga saya dapat melakukan penilaian akademik yang terstruktur.

#### Acceptance Criteria

1. WHEN guru mengakses menu nilai THEN sistem SHALL menampilkan daftar kelas yang diajar
2. WHEN guru menginput nilai siswa THEN sistem SHALL menyimpan nilai dengan kategori (tugas, UTS, UAS)
3. WHEN guru melihat rekap nilai THEN sistem SHALL menampilkan statistik nilai per mata pelajaran
4. WHEN admin generate rapor THEN sistem SHALL membuat laporan nilai siswa per semester

### Requirement 5 - Manajemen Jadwal Pelajaran

**User Story:** Sebagai admin sekolah, saya ingin membuat dan mengelola jadwal pelajaran, sehingga aktivitas belajar mengajar dapat terorganisir dengan baik.

#### Acceptance Criteria

1. WHEN admin membuat jadwal THEN sistem SHALL menyimpan jadwal dengan hari, jam, mata pelajaran, guru, dan kelas
2. WHEN admin melihat jadwal kelas THEN sistem SHALL menampilkan jadwal mingguan untuk kelas tertentu
3. WHEN admin melihat jadwal guru THEN sistem SHALL menampilkan jadwal mengajar guru
4. IF terjadi bentrok jadwal THEN sistem SHALL memberikan peringatan konflik

### Requirement 6 - Sistem Absensi

**User Story:** Sebagai guru, saya ingin mencatat kehadiran siswa, sehingga saya dapat memantau tingkat kehadiran siswa di kelas.

#### Acceptance Criteria

1. WHEN guru membuka absensi THEN sistem SHALL menampilkan daftar siswa di kelas
2. WHEN guru menandai kehadiran THEN sistem SHALL menyimpan status hadir/tidak hadir/izin/sakit
3. WHEN admin melihat laporan absensi THEN sistem SHALL menampilkan statistik kehadiran siswa
4. WHEN siswa tidak hadir lebih dari batas THEN sistem SHALL memberikan notifikasi

### Requirement 7 - Manajemen Keuangan Sekolah

**User Story:** Sebagai admin keuangan, saya ingin mengelola pembayaran SPP dan biaya sekolah, sehingga keuangan sekolah dapat terpantau dengan baik.

#### Acceptance Criteria

1. WHEN admin input pembayaran SPP THEN sistem SHALL mencatat transaksi pembayaran siswa
2. WHEN admin melihat tunggakan THEN sistem SHALL menampilkan daftar siswa yang belum bayar
3. WHEN admin generate laporan keuangan THEN sistem SHALL membuat laporan pemasukan dan pengeluaran
4. WHEN orang tua melihat tagihan THEN sistem SHALL menampilkan status pembayaran anak

### Requirement 8 - Dashboard dan Laporan

**User Story:** Sebagai kepala sekolah, saya ingin melihat dashboard dan laporan sekolah, sehingga saya dapat memantau kinerja sekolah secara keseluruhan.

#### Acceptance Criteria

1. WHEN kepala sekolah login THEN sistem SHALL menampilkan dashboard dengan ringkasan data
2. WHEN admin generate laporan THEN sistem SHALL membuat laporan siswa, guru, dan keuangan
3. WHEN user melihat statistik THEN sistem SHALL menampilkan grafik dan chart yang relevan
4. WHEN user export laporan THEN sistem SHALL menghasilkan file PDF atau Excel

### Requirement 9 - Sistem Autentikasi dan Otorisasi

**User Story:** Sebagai pengguna sistem, saya ingin login dengan role yang sesuai, sehingga saya hanya dapat mengakses fitur yang diizinkan.

#### Acceptance Criteria

1. WHEN user login THEN sistem SHALL memverifikasi kredensial dan role pengguna
2. WHEN admin mengakses menu THEN sistem SHALL menampilkan semua fitur administrasi
3. WHEN guru mengakses sistem THEN sistem SHALL membatasi akses hanya ke fitur guru
4. WHEN orang tua login THEN sistem SHALL hanya menampilkan informasi anak mereka