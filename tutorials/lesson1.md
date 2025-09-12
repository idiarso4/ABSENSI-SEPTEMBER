# Pembelajaran 1: Pengenalan dan Setup Backend Java

## Pengenalan Proyek
Selamat datang di tutorial pembuatan aplikasi SIM_CLONE, sebuah sistem manajemen sekolah (SIM) yang lengkap dengan fitur Human Resource Management (HRM) dan penggajian. Aplikasi ini dirancang untuk mengelola berbagai aspek sekolah seperti data siswa, guru, mata pelajaran, nilai, jadwal pelajaran, dan pembayaran. Backend dibangun menggunakan Java Spring Boot, yang merupakan framework populer untuk aplikasi enterprise karena kemudahannya dalam mengintegrasikan database, security, dan API REST. Frontend menggunakan JavaScript sederhana dengan struktur HTML/CSS/JS untuk tampilan web, sementara aplikasi mobile dibuat dengan Flutter untuk cross-platform (Android dan iOS). 

Folder "HRM dan Penggajian" merupakan adaptasi dari sistem HRM existing, yang menggabungkan backend Java baru dengan kode Laravel lama di subfolder "kodeawal/". Ini memungkinkan integrasi fitur penggajian karyawan dengan sistem sekolah utama. Tujuan utama tutorial ini adalah mengajarkan siswa cara membangun aplikasi full-stack dari nol, mulai dari backend hingga mobile, sambil memahami best practices seperti version control dengan Git, containerization dengan Docker, dan testing.

Proyek ini cocok untuk siswa tingkat menengah yang sudah familiar dengan pemrograman dasar. Estimasi waktu: 20-30 jam untuk menyelesaikan semua lesson.

## Persyaratan Lingkungan Pengembangan
Sebelum memulai, pastikan lingkungan Anda siap. Berikut daftar tools yang diperlukan:

- **JDK 17 atau lebih tinggi**: Download dari situs resmi Oracle (https://www.oracle.com/java/technologies/downloads/) atau OpenJDK (https://openjdk.org/). JDK 17 dipilih karena support LTS (Long Term Support) dan kompatibilitas dengan Spring Boot 3.x. Verifikasi instalasi dengan menjalankan `java -version` di terminal. Jika belum terinstall, ikuti instruksi OS Anda (Windows: set JAVA_HOME environment variable).
  
- **Maven 3.8+**: Tool build untuk Java. Sudah disediakan portable di "HRM dan Penggajian/maven-portable/apache-maven-3.8.8/". Untuk instalasi global, download dari https://maven.apache.org/download.cgi. Verifikasi dengan `mvn -version`. Maven mengelola dependencies dan build JAR file.

- **IDE**: IntelliJ IDEA Community (gratis, direkomendasikan untuk Java) atau VS Code dengan extension "Extension Pack for Java". Di VS Code, install juga "Spring Boot Extension Pack" untuk support Spring. IDE membantu dengan auto-complete, debugging, dan refactoring.

- **Database**: MySQL 8.0 atau PostgreSQL 13+. Download MySQL dari https://dev.mysql.com/downloads/. Buat database kosong bernama "simsekolah". Gunakan tool seperti MySQL Workbench untuk manage. Jika menggunakan PostgreSQL, ubah driver di pom.xml.

- **Git**: Untuk version control. Install dari https://git-scm.com/. Inisialisasi repo dengan `git init` di root proyek. .gitignore sudah dibuat untuk ignore files seperti target/, .idea/.

Troubleshooting: Jika Maven tidak ditemukan, tambah path ke environment variables. Untuk database, pastikan port 3306 terbuka dan tidak konflik.

## Langkah 1: Membuat Proyek Spring Boot
1. Buka IDE (misalnya IntelliJ): File > New > Project > Spring Initializr.
2. Pilih Maven sebagai build tool, Java sebagai language, dan versi JDK 17.
3. Group: com.simsekolah, Artifact: simsekolah, Name: SimSekolah.
4. Pilih dependencies: 
   - Spring Web (untuk REST API)
   - Spring Data JPA (untuk ORM dengan Hibernate)
   - Spring Security (untuk authentication)
   - Flyway Migration (untuk database schema management)
   - MySQL Driver (atau PostgreSQL Driver)
5. Generate proyek dan import ke IDE. Struktur akan mirip backend/src/main/java/com/simsekolah/, dengan pom.xml untuk dependencies.

Jika menggunakan command line: `curl https://start.spring.io/starter.zip -d dependencies=web,data-jpa,security,flyway,mysql -d javaVersion=17 -d groupId=com.simsekolah -d artifactId=simsekolah | java -jar /tmp/spring-initializr.zip`.

Contoh file utama: [`SimSekolahApplication.java`](backend/src/main/java/com/simsekolah/SimSekolahApplication.java:1)
```java
package com.simsekolah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Scan components di package ini dan sub-package
public class SimSekolahApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimSekolahApplication.class, args);  // Start server di port default 8080
    }
}
```
Penjelasan: @SpringBootApplication mengaktifkan auto-configuration. Main method launch embedded Tomcat server.

Troubleshooting: Jika error "No qualifying bean", pastikan dependencies di pom.xml benar.

## Langkah 2: Konfigurasi Database
1. Edit [`application.properties`](backend/src/main/resources/application.properties:1) di src/main/resources/. File ini untuk konfigurasi Spring.
```properties
# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/simsekolah?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password  # Ganti dengan password MySQL Anda
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate  # Validate schema, gunakan 'update' untuk development
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Flyway untuk migrations
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

Penjelasan: URL JDBC menghubungkan ke database lokal. ddl-auto=validate memastikan schema cocok dengan entity tanpa auto-create. Flyway menjalankan SQL script versi secara otomatis saat startup, mencegah duplikasi migration.

2. Buat folder db/migration di src/main/resources/ untuk script SQL seperti V1__Initial_schema.sql. Contoh:
```sql
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE
);
```

Contoh konfigurasi database: [`DatabaseConfig.java`](backend/src/main/java/com/simsekolah/config/DatabaseConfig.java:1)
```java
@Configuration
public class DatabaseConfig {
    @Bean
    @Primary
    public DataSource dataSource() {
        // Custom data source jika diperlukan
        return DataSourceBuilder.create().build();
    }
}
```
Ini opsional untuk custom pooling.

Troubleshooting: Jika koneksi gagal, check firewall atau gunakan XAMPP untuk MySQL lokal. Untuk PostgreSQL, ubah URL dan driver.

## Langkah 3: Jalankan Backend
- Navigasi ke root backend/ di terminal.
- Jalankan `mvn clean install` untuk build dependencies.
- Kemudian `mvn spring-boot:run` untuk start server. Log akan tampil, cari "Started SimSekolahApplication in X seconds".
- Akses http://localhost:8080 untuk test (akan 404 jika belum ada controller, normal). Gunakan curl atau browser.

Penjelasan: Maven download dependencies dari repo central. Spring Boot embedded server memudahkan development tanpa server eksternal.

Troubleshooting: Jika port 8080 busy, tambah server.port=8081 di application.properties. Error "Could not resolve placeholder" berarti properties salah.

## Latihan
1. Install JDK, Maven, dan IDE. Verifikasi dengan commands.
2. Buat proyek Spring Boot sederhana dengan dependencies di atas dan jalankan. Lihat log startup.
3. Konfigurasi database lokal, buat DB "simsekolah", dan test koneksi dengan menambahkan @Repository dan @Service sederhana untuk query test.
4. Eksplor folder "HRM dan Penggajian" – jalankan backend HRM dengan Maven untuk lihat adaptasi.

Tips: Gunakan Git untuk commit perubahan setelah setup. Lihat README_SIM_SEKOLAH.md untuk overview proyek.

Lanjut ke Pembelajaran 2 untuk model dan database.