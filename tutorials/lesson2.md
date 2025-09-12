# Pembelajaran 2: Implementasi Model dan Database di Java

## Pengenalan
Di pembelajaran ini, kita akan mendalami implementasi model entity untuk aplikasi SIM Sekolah menggunakan JPA (Java Persistence API) dengan Hibernate sebagai provider ORM (Object-Relational Mapping). JPA memungkinkan kita memetakan class Java ke tabel database secara otomatis, sehingga kita bisa bekerja dengan objek daripada SQL mentah. Model yang akan dibuat mencakup Siswa (Student), Guru (Teacher), Mata Pelajaran (Subject), Nilai (Grade), Jadwal (Schedule), dan Pembayaran (Payment). Setelah model, kita setup repository untuk CRUD operations, dan migrasi database menggunakan Flyway untuk mengelola schema changes secara versioned, menghindari konflik di tim development.

Konsep kunci: Entity adalah class yang merepresentasikan tabel database. Relasi antar entity menggunakan annotations seperti @OneToMany, @ManyToOne untuk menangani hubungan seperti satu guru mengajar banyak siswa. Flyway memastikan database selalu sinkron dengan code, mirip Git untuk schema.

Estimasi waktu: 4-6 jam. Pastikan backend dari lesson 1 sudah running.

## Langkah 1: Membuat Entity Model
Buat package com.simsekolah.model di src/main/java/com/simsekolah/model/. Setiap entity class harus di-annotate dengan @Entity (menandakan ini adalah persistence class), @Table(name = "nama_tabel") untuk specify nama tabel, dan field primary key dengan @Id dan @GeneratedValue untuk auto-increment.

Mulai dengan model dasar Siswa. Buat file Student.java:
Contoh model Siswa: [`Student.java`](backend/src/main/java/com/simsekolah/model/Student.java:1)
```java
package com.simsekolah.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 20)
    private String phone;

    // Constructor default
    public Student() {}

    // Constructor dengan parameter
    public Student(String name, String email, LocalDate birthDate, String phone) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
```
Penjelasan: @Column menentukan constraints seperti nullable dan length. Gunakan LocalDate untuk tanggal. Strategy IDENTITY untuk MySQL auto-increment.

Buat model serupa untuk yang lain:
- Teacher: [`Teacher.java`](backend/src/main/java/com/simsekolah/model/Teacher.java:1) – Tambah field specialization dan salary.
- Subject: [`Subject.java`](backend/src/main/java/com/simsekolah/model/Subject.java:1) – Field name dan code.
- Grade: [`Grade.java`](backend/src/main/java/com/simsekolah/model/Grade.java:1) – Relasi @ManyToOne ke Student dan Subject, field value (e.g., A, B).
- Schedule: [`Schedule.java`](backend/src/main/java/com/simsekolah/model/Schedule.java:1) – Relasi ke Teacher, Subject, Student, field date dan time.
- Payment: [`Payment.java`](backend/src/main/java/com/simsekolah/model/Payment.java:1) – Relasi ke Student, field amount dan status.

Untuk relasi, di Grade.java:
```java
@ManyToOne
@JoinColumn(name = "student_id")
private Student student;

@ManyToOne
@JoinColumn(name = "subject_id")
private Subject subject;
```
Penjelasan: @ManyToOne untuk foreign key. @JoinColumn specify kolom FK. Ini memungkinkan lazy loading untuk performa.

Troubleshooting: Jika error "No identifier specified", tambah @Id. Compile dengan mvn clean compile untuk check syntax.

## Langkah 2: Setup Repository
Repository adalah interface yang extend JpaRepository<T, ID>, menyediakan method CRUD otomatis seperti save(), findAll(), findById(). Buat package com.simsekolah.repository dan buat interface untuk setiap entity.

Contoh untuk Student:
```java
package com.simsekolah.repository;

import com.simsekolah.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository  // Optional, Spring scan otomatis
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom query method
    List<Student> findByNameContainingIgnoreCase(String name);  // Query by example
    List<Student> findByEmail(String email);
    // @Query annotation untuk complex query
    @Query("SELECT s FROM Student s WHERE s.birthDate > :date")
    List<Student> findStudentsBornAfter(@Param("date") LocalDate date);
}
```
Penjelasan: findByNameContainingIgnoreCase otomatis generate JPQL query. @Query untuk custom SQL-like query. Gunakan @Param untuk parameter.

Buat repository serupa untuk TeacherRepository, SubjectRepository, dll.

Troubleshooting: Jika method tidak ditemukan, pastikan entity memiliki field yang match nama method. Test dengan @Autowired di service.

## Langkah 3: Migrasi Database dengan Flyway
Flyway mengelola database schema seperti version control. Script bernama V1__Description.sql, dijalankan secara berurutan saat app start.

1. Buat folder src/main/resources/db/migration/.
2. Buat file V1__Create_initial_tables.sql:
```sql
-- Create students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    birth_date DATE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create teachers table
CREATE TABLE teachers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    specialization VARCHAR(100),
    salary DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create subjects table
CREATE TABLE subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create grades table
CREATE TABLE grades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    value CHAR(1) NOT NULL,  -- A, B, C, etc.
    student_id BIGINT,
    subject_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create schedules table
CREATE TABLE schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    time TIME NOT NULL,
    teacher_id BIGINT,
    subject_id BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PAID', 'PENDING', 'OVERDUE'),
    student_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    paid_at TIMESTAMP NULL
);
```

Penjelasan: Gunakan TIMESTAMP untuk audit. ENUM untuk status terbatas. Flyway run script ini otomatis, dan track versi di tabel flyway_schema_history.

2. Untuk update schema nanti, buat V2__Add_indexes.sql dengan ALTER TABLE ADD INDEX.

Troubleshooting: Jika migration gagal, check log untuk SQL error. Hapus schema dan restart app dengan flyway.clean-disabled=false di properties untuk reset.

## Langkah 4: Test Database
1. Buat simple service di com.simsekolah.service:
```java
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
```

2. Buat controller test di com.simsekolah.controller:
```java
@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
}
```

3. Jalankan app, test dengan Postman: POST /api/students dengan JSON body, GET untuk retrieve.

Penjelasan: @RestController return JSON otomatis. @RequestBody parse JSON ke object.

Troubleshooting: Jika 500 error, check console untuk stack trace, sering karena missing @Autowired atau relasi circular.

## Latihan
1. Buat semua entity model dengan field lengkap dan relasi (@OneToMany jika diperlukan, e.g., Student hasMany Grades).
2. Implement repository dengan 2-3 custom query method per entity.
3. Buat migration SQL V1__ untuk semua tabel, termasuk indexes pada foreign keys.
4. Test dengan Postman: Add student, get all, query by name. Verifikasi data di database tool seperti phpMyAdmin.
5. Eksplor relasi: Buat Grade dengan student_id dan subject_id, test save.

Tips: Gunakan Lombok (@Data, @NoArgsConstructor) untuk kurangi boilerplate getter/setter. Commit changes ke Git.

Lanjut ke Pembelajaran 3 untuk security dan authentication.