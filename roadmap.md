# Roadmap API SIM Sekolah

Dokumen ini memetakan Menu dan Submenu dari navbar frontend ke status endpoint backend (CRUD, filter, import/export, statistik, dsb). Tanda: ✔️ sudah ada, ✔️/❓ dasar ada (fitur khusus perlu dicek), ❌ belum ada.

---

## SIM Sekolah
- Landing/Root: Health check, fitur ringkas (HomeController) ✔️

## Dashboard
- Dashboard Ringkas: `ReportController /dashboard/summary`, KPI analytics ✔️

## Akademik
	- Kelola Siswa: CRUD, search, filter, statistik, Excel import/export ✔️
	- Update Foto Siswa: upload/manajemen foto siswa ✔️/❓
	- Manajemen Update Foto: antrean/status proses ✔️/❓
	- Kelola Guru: CRUD, search, filter, statistik, Excel import/export ✔️
	- Mata Pelajaran: CRUD, search, filter, statistik, Excel import/export ✔️ (Import: DONE)
	- Ruang Kelas: CRUD, search, filter, statistik, Excel import/export ✔️ (Import: DONE; mapping Major & Wali ✔️)
	- Penilaian: `AssessmentController` (buat/kelola komponen penilaian) ✔️/❓
	- Nilai Siswa: `GradeController` CRUD, query per siswa/kelas/semester, statistik ✔️
	- Jadwal Pelajaran: `ScheduleController` CRUD, query per kelas/guru/hari ✔️
	- Kalender Akademik: `AcademicCalendarController` ✔️
	- Manajemen Semester: `SemesterController` CRUD, aktivasi/complete, statistik ✔️

### Excel Import (Ringkas)
- Subjects: POST `/api/v1/subjects/excel/import` — Kolom: Code, Name, Type (THEORY/PRACTICE/MIXED), Description. Dukung `?dryRun=true`.
- Teachers: POST `/api/v1/teachers/excel/import` — Kolom: NIP, Full Name, Email, Phone, Address, Status (ACTIVE/AKTIF or INACTIVE). Dukung `?dryRun=true`.
- Classrooms: POST `/api/v1/classrooms/excel/import` — Kolom: Class Name, Grade Level, Academic Year, Semester, Capacity, Location, Status, Major (by Code/Name), Homeroom Teacher (by NIP/Email). Dukung `?dryRun=true` untuk validasi tanpa simpan.
- Template & export tersedia via GET/POST `/excel/template` dan `/excel/export` pada masing-masing controller.

## Prioritas Inti (Status per Menu)
- Siswa: CRUD + Excel import/export ✔️
- Guru: CRUD + Excel import/export ✔️
- Mapel: CRUD + Excel import/export ✔️ (Import DONE)
- Kelas: CRUD + Excel import/export ✔️ (Import DONE; Major & Wali mapping ✔️; dryRun ✔️)
- Kehadiran: Endpoints + laporan ✔️
- Nilai: Endpoints + statistik ✔️
- Jadwal: Endpoints + filter ✔️

## Next Issues
- Excel templates: tambahkan contoh nilai untuk kolom Major (kode/nama) dan Wali Kelas (NIP/Email) agar operator tidak bingung.
- Konsistensi import: samakan skema response error/success di semua modul (student/teacher/subject/classroom) + opsi `dryRun` untuk semua import.
- Upsert mode (opsional): dukung pembaruan data jika record sudah ada (berdasarkan key unik), dengan flag `?mode=upsert`.
- Postman collection: siapkan koleksi endpoint import/export + contoh file .xlsx untuk QA.
- Integrasi frontend: tambah tombol/improve alur upload + notifikasi hasil (berdasarkan ringkasan import yang ada).

## Kehadiran
- Kehadiran Harian: `AttendanceController` CRUD, range tanggal, statistik, laporan ✔️
- Kehadiran Kelas: filter per kelas, ringkasan harian/bulanan ✔️
- Laporan Kehadiran: `AttendanceController` + `ReportController` export Excel ✔️
- Absensi (Face + Lokasi): endpoint absensi tersedia, validasi wajah/lokasi ✔️/❓
- Kalender Kehadiran: `AttendanceCalendarController` ✔️

## Keuangan
- Pembayaran:
	- SPP, Kegiatan, Seragam, Buku: `PaymentController` CRUD, filter, laporan ✔️
	- Penagihan/Invoice: `InvoiceController` CRUD, PDF, statistik, overdue ✔️
	- Tunggakan/Reminder: `PaymentController /overdue`, `/reminders` ✔️
- Laporan Keuangan: `ReportController` payments/financial ✔️

## Laporan
- Akademik: nilai, distribusi nilai, top performers, at-risk ✔️
- Kehadiran: ringkasan harian/bulanan, per kelas/siswa ✔️
- Performa Siswa: analytics performa ✔️
- Rapor: transcript siswa ✔️
- Analitik Nilai: KPI/analytics ✔️
- Export (PDF/Excel): `ExportController`, Excel via modul masing-masing ✔️

## Manajemen PKL
- Absensi PKL: `PklAttendanceController` CRUD, statistik ✔️
- Kunjungan/Kegiatan/Laporan Harian: controller terkait ✔️

## Perizinan Siswa
- Izin Masuk/Keluar: `StudentPermissionController` CRUD, statistik ✔️
- Laporan Guru Piket: `DutyTeacherReportController` laporan & statistik ✔️

## Manajemen BK
- Penanganan Siswa: `CounselingCaseController` CRUD ✔️
- Daftar Kunjungan: `CounselingVisitController` CRUD, search, statistik ✔️
- Surat Kesepakatan: `CounselingAgreementController` CRUD ✔️

## LMS
- Absensi Guru: `TeacherAttendanceController` CRUD, verifikasi, statistik ✔️
- Materi: `LearningMaterialController` CRUD, publish/unpublish, statistik, unduh ✔️
- Daftar Soal: `QuestionBankController` CRUD, statistik ✔️
- Ulangan: `TestController` CRUD, publish, filter, statistik ✔️

## Sistem
- Manajemen Pengguna: `UserController` CRUD, aktivasi, roles, statistik ✔️
- Peran & Izin: `RoleController` CRUD, assign/revoke role ✔️
- Manajemen Tugas: `TaskController` ✔️
- Pengaturan: `SettingsController` (akademik/sistem) ✔️/❓
- Departemen: `DepartmentController` CRUD ✔️
- Backup & Restore: `BackupController` ✔️/❓
- Bahasa: `LanguageController` atau pengaturan i18n ✔️/❓

## Akun
- Profil Saya: `UserController`/`AuthController` get profile ✔️
- Manajemen Akun: `UserController` CRUD ✔️
- Peran & Izin: `UserController` role endpoints ✔️
- Ubah Password: `AuthController /change-password` ✔️
- Logout: `AuthController`/frontend token clear ✔️

---

### Catatan Tambahan
- Fitur bertanda ✔️/❓: endpoint dasar ada; verifikasi kebutuhan khusus (upload foto, face-recognition, lokasi geofence, backup/restore, setting i18n) dan tambahkan jika perlu.
- Export tersedia via controller entitas (Students/Teachers/Subjects/Classrooms) dan juga `ExportController` (Excel/CSV/JSON; beberapa modul PDF via Invoice).

### Legend
- ✔️ = Endpoint CRUD & fitur utama sudah ada
- ✔️/❓ = Endpoint dasar ada, fitur khusus perlu dicek/ditambah
- ❌ = Endpoint belum ada, perlu dibuat

Silakan update roadmap ini bila ada penambahan menu atau perubahan kebutuhan API.

---

## Fokus Kelengkapan (Core Modules)
Prioritas hanya pada modul inti agar tidak melebar ke minor project. Checklist per modul minimal:

- Siswa (Students)
	- [x] CRUD & validasi dasar (required fields)
	- [x] Search/filter + pagination
	- [x] Export + Template Excel
	- [x] Import Excel (dryRun, validasi baris, error detail, classroom mapping)
	- [x] Relasi ke Kelas (assign/bulk assign)
	- [x] Auth/roles akses (ADMIN/TEACHER)
	- [x] Postman + Fixtures
	- [x] Docs (README + catatan kolom)

- Guru (Teachers)
	- [x] CRUD & validasi dasar
	- [x] Search/filter + pagination
	- [x] Export + Template Excel (dengan contoh)
	- [x] Import Excel (dryRun, validasi, duplicate checks)
	- [ ] Relasi opsional (departemen/role lanjutan)
	- [x] Auth/roles akses
	- [x] Postman + Fixtures
	- [x] Docs

- Mapel (Subjects)
	- [x] CRUD & validasi dasar
	- [x] Search/filter + pagination
	- [x] Export + Template Excel (dengan contoh)
	- [x] Import Excel (dryRun, type parsing, duplicate checks)
	- [ ] Relasi ke Jadwal (nanti)
	- [x] Auth/roles akses
	- [x] Postman + Fixtures
	- [x] Docs

- Kelas (Classrooms)
	- [x] CRUD & validasi dasar
	- [x] Search/filter + pagination
	- [x] Export + Template Excel (contoh Major & Wali Kelas)
	- [x] Import Excel (dryRun, mapping Major & Wali Kelas)
	- [ ] Relasi ke Jadwal & Siswa (rekap)
	- [x] Auth/roles akses
	- [x] Postman + Fixtures
	- [x] Docs

- Kehadiran (Attendance)
	- [ ] CRUD & filter per siswa/kelas/tanggal
	- [ ] Laporan ringkas + export
	- [ ] Auth/roles akses
	- [ ] Postman + Docs

- Nilai (Grades)
	- [ ] CRUD & query per siswa/kelas/semester
	- [ ] Statistik ringkas + export
	- [ ] Auth/roles akses
	- [ ] Postman + Docs

- Jadwal (Schedules)
	- [ ] CRUD & filter per kelas/guru/hari
	- [ ] Validasi konflik dasar
	- [ ] Export ringkas
	- [ ] Auth/roles akses
	- [ ] Postman + Docs

## Acceptance Criteria (Minimal Viable)
- Import Excel (Mapel, Guru, Kelas, Siswa):
	- Header jelas, contoh baris ada, validasi tipe & ukuran file, max rows dibatasi.
	- dryRun men-simulasikan tanpa perubahan DB, status SUCCESS_DRY_RUN saat bersih.
	- Error per-baris berisi row, field, value, message.
	- Mapping relasi: Kelas → Major (kode/nama), Wali Kelas (NIP/Email) wajib jika kolom diisi.
	- Upsert mode: `?mode=upsert` untuk update record yang sudah ada berdasarkan key unik (NIS untuk siswa, NIP untuk guru, Code untuk mapel, Class Name untuk kelas).
- Export: GET/POST tersedia, kolom konsisten dengan template.
- Auth: ADMIN dapat semua, TEACHER read/ops terbatas.
- Postman & Fixtures: koleksi siap jalan, contoh file tersedia/ditarik via script.

## Sprint Fokus Berikutnya (High Impact)
1) ✅ Student import: persistence + dryRun gating selesai, classroom mapping dengan validasi kapasitas.
2) ✅ Unifikasi response import: struktur field sama di semua modul (status, totalRows, successfulImports, failedImports, createdItems, errors[…]).
3) ✅ Smoke tests: script `tools/smoke_imports.ps1` untuk validasi dryRun semua modul (Subjects: ✅, Teachers: ✅, Classrooms: ✅, Students: ✅).
4) ✅ Mode upsert (opsional): `?mode=upsert` untuk update jika key unik sudah ada (dengan guardrails terbatas).
5) ✅ .xlsx fixtures nyata: generate file contoh ke `fixtures/` untuk QA cepat.
6) ✅ Postman collection lengkap: semua endpoint import/export dengan dryRun + upsert examples.
7) ✅ QA tools siap: fixtures README updated, sample files created untuk testing cepat.

## Guardrails: Hindari Minor Project
- Tunda fitur non-inti (LMS detail, PKL detail, i18n lanjutan, backup/restore UI) sampai inti selesai.
- Hindari refactor besar kecuali memblokir build atau inti.
- Semua perubahan harus melewati: Build PASS + Postman smoke import(dryRun) PASS.