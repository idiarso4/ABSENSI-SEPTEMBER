# Pembelajaran 8: Deployment dan Testing Akhir

## Pengenalan
Pembelajaran terakhir ini membahas deployment aplikasi SIM_CLONE ke environment production, termasuk containerization backend Java dengan Docker untuk portability dan scalability, serving frontend statis menggunakan Nginx atau Spring Boot static resources untuk performa, building dan signing APK/IPA untuk Flutter app agar siap distribusi, dan testing end-to-end untuk verifikasi full stack bekerja dengan benar, termasuk integrasi dengan folder "HRM dan Penggajian" yang merupakan adaptasi sistem HRM. Deployment adalah tahap krusial di mana code dari development diubah menjadi live system, mempertimbangkan security (secrets management), monitoring (logs), dan CI/CD (continuous integration/deployment) dasar menggunakan GitHub Actions atau Jenkins.

Konsep kunci: Docker containerize app agar run konsisten di local, cloud (AWS, Heroku), atau server. docker-compose orchestrate multi-container seperti backend + database. Testing mencakup unit (code level), integration (component interaction), dan E2E (user flow). Untuk HRM, test adaptasi Laravel legacy ke Java backend. Estimasi waktu: 5-7 jam. Pastikan lesson 7 selesai, Flutter app running, backend Docker-ready dari pom.xml.

Troubleshooting umum: Docker daemon not running? Start Docker Desktop. Image build gagal? Check Dockerfile syntax. APK not install? Enable developer options di device.

## Langkah 1: Deployment Backend Java dengan Docker
1. Pastikan pom.xml backend punya Spring Boot Maven plugin untuk generate fat JAR. Jalankan `mvn clean package -DskipTests` di backend/ untuk build target/simsekolah-0.0.1-SNAPSHOT.jar (nama dari artifactId dan version di pom.xml). Skip tests untuk speed.

2. Buat Dockerfile di root backend/ atau proyek root untuk image custom:
```dockerfile
# Stage 1: Build JAR
FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run JAR
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/simsekolah.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
```
Penjelasan: Multi-stage build: First stage build dengan Maven, second stage run JAR dengan slim image untuk size kecil (kurangi dari 1GB ke 300MB). COPY --from=build copy artifact. ENV untuk profile production. -Dspring.profiles.active=prod load application-prod.properties untuk config live (e.g., database remote).

3. Buat atau edit docker-compose.yml di root proyek untuk orchestrate (include db persistence):
```yaml
version: '3.8'

services:
  db:
    image: mysql:8.0
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD:-password}
      MYSQL_DATABASE: simsekolah
      MYSQL_USER: simuser
      MYSQL_PASSWORD: ${DB_PASSWORD:-pass}
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
      - ./backend/src/main/resources/db/migration:/flyway/sql:ro  # Mount migrations
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 30s
      timeout: 10s
      retries: 3

  backend:
    build: ./backend
    restart: always
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/simsekolah?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: simuser
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD:-pass}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SIMSEKOLAH_APP_JWT_SECRET: ${JWT_SECRET:-your-super-secret-key}
      LOGGING_LEVEL_COM_SIMSEKOLAH: DEBUG
    volumes:
      - ./logs:/app/logs  # Mount logs untuk monitoring

volumes:
  db_data:

networks:
  default:
    driver: bridge
```
Penjelasan: healthcheck pastikan db ready sebelum backend start. environment gunakan vars dari .env file (buat .env dengan DB_ROOT_PASSWORD=password). volumes persist db data, mount logs. depends_on condition wait healthy.

4. Buat .env file di root untuk secrets:
```
DB_ROOT_PASSWORD=password
DB_PASSWORD=pass
JWT_SECRET=your-very-secure-jwt-secret-at-least-256-bits
```
Jalankan `docker-compose up --build -d` untuk detached mode. Check status `docker-compose ps`, logs `docker-compose logs -f backend`. Test API http://localhost:8080/api/students.

Penjelasan: -d run background. --build rebuild image jika code berubah. Untuk production, gunakan Docker Swarm atau Kubernetes untuk scale.

Troubleshooting: Container exit? Check logs untuk error (e.g., db connection). Volume not persist? Check docker volume ls. Secret leak? Jangan commit .env ke Git.

## Langkah 2: Serving Frontend
Frontend statis (HTML/JS/CSS) bisa di-serve terintegrasi atau separate.

Option 1: Integrated with Spring Boot (simple untuk dev)
1. Copy seluruh frontend/ ke backend/src/main/resources/static/.
2. Di application.properties tambah:
```properties
spring.web.resources.static-locations=classpath:/static/,file:frontend/
spring.mvc.static-path-pattern=/static/**
```
3. Restart backend, akses http://localhost:8080/static/index.html. API calls gunakan relative /api/students.

Penjelasan: Spring Boot serve static files dari /static/** otomatis. Cocok jika backend dan frontend di satu server.

Option 2: Separate Nginx container (recommended untuk production, separate concern)
Tambah service di docker-compose.yml:
```yaml
  frontend:
    image: nginx:alpine
    restart: always
    ports:
      - "80:80"
    volumes:
      - ./frontend:/usr/share/nginx/html:ro
      - ./nginx.conf:/etc/nginx/nginx.conf:ro  # Custom config jika perlu proxy
    depends_on:
      - backend
```
Buat nginx.conf untuk proxy API:
```
events {}
http {
  server {
    listen 80;
    location / {
      root /usr/share/nginx/html;
      try_files $uri $uri/ =404;
    }
    location /api/ {
      proxy_pass http://backend:8080/api/;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
    }
  }
}
```
Jalankan docker-compose up. Akses http://localhost untuk frontend, /api/ proxy ke backend.

Penjelasan: Nginx fast untuk static, proxy_pass route API calls ke backend. ro untuk security.

Troubleshooting: 502 Bad Gateway? Backend not ready, tambah healthcheck. Static not load? Check volume mount.

## Langkah 3: Building dan Deployment Flutter App
1. Di mobile/, clean build: `flutter clean && flutter pub get`.

2. Untuk Android APK debug: `flutter build apk --debug`. Release: `flutter build apk --release --split-per-abi` untuk separate APK per architecture (arm, x86).

3. Signing untuk production: 
   - Generate key: `keytool -genkey -v -keystore ~/key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias key`.
   - Buat android/key.properties:
```
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=key
storeFile=/path/to/key.jks
```
   - Edit android/app/build.gradle: tambah signingConfigs.release dengan loadProperties.
   - Build signed: `flutter build apk --release`.

4. Output APK di build/app/outputs/flutter-apk/app-release.apk. Install di device: `adb install path/to/app.apk` atau drag-drop.

5. Untuk iOS: Butuh Mac. `flutter build ios --release`. Open ios/Runner.xcworkspace di Xcode, archive untuk App Store.

6. Deployment: Upload ke Google Play Console (buat account $25, submit APK dengan description). Gunakan Firebase App Distribution untuk beta test. Untuk web: `flutter build web`, serve build/web/.

Penjelasan: --split-per-abi kurangi size APK. Signing verifikasi developer. Firebase untuk crash reporting dan analytics.

Troubleshooting: Build error "Keystore file not found"? Check path in key.properties. Obfuscation? Tambah --obfuscate. Size besar? Analyze dengan `flutter build apk --analyze-size`.

## Langkah 4: Integrasi HRM dan Penggajian
Folder "HRM dan Penggajian" adalah adaptasi sistem HRM, dengan backend Java di /backend (struktur mirip SimSekolah, gunakan Maven portable di /maven-portable/ untuk build tanpa global Maven), frontend JS di /frontend/, dan kode Laravel legacy di /kodeawal/ (PHP framework untuk HRM lama, include vendor/ dependencies).

1. Jalankan backend HRM: cd "HRM dan Penggajian/backend". Set MAVEN_HOME=path/to/maven-portable/apache-maven-3.8.8, jalankan bin/mvnw.cmd spring-boot:run. Akses http://localhost:8081/api/hrm/employees (port berbeda untuk avoid conflict).

2. Adaptasi Laravel: Kodeawal/ adalah source PHP Laravel untuk fitur penggajian. Untuk integrasi, port logic ke Java (e.g., buat Employee model di HRM backend, endpoint /api/employees). Ignore folder seperti vendor/, storage/, bootstrap/cache/ via .gitignore. Jika perlu test legacy, install PHP/Composer di kodeawal/, run `composer install`, `php artisan serve`.

3. Integrasi dengan SIM: Di SimSekolah backend, tambah service call HRM API (e.g., Payment relasi ke Employee dari HRM). Contoh, di PaymentService:
```java
@Autowired
private RestTemplate restTemplate;

public Payment createPayment(Long studentId, Double amount) {
  // Call HRM for employee salary deduction if related
  String hrmResponse = restTemplate.postForObject("http://localhost:8081/api/hrm/employees/deduct", paymentData, String.class);
  // ...
}
```
Test dengan Postman: POST /api/payments, check HRM logs.

Penjelasan: Adaptasi berarti migrasi gradual dari PHP ke Java untuk consistency. Gunakan Feign atau RestTemplate untuk microservices call.

Troubleshooting: Maven portable error? Run with full path. Laravel composer fail? Update composer.json. API call fail? Check port dan CORS di HRM SecurityConfig.

## Langkah 5: Testing Akhir
1. Unit Test: Backend - Tambah @SpringBootTest di test class, e.g., StudentServiceTest:
```java
@SpringBootTest
class StudentServiceTest {
  @Autowired
  StudentService studentService;

  @Test
  void testSaveStudent() {
    Student student = new Student("John", "john@email.com", ...);
    Student saved = studentService.saveStudent(student);
    assertNotNull(saved.getId());
  }
}
```
Run `mvn test`. Frontend - Manual atau add Jest. Flutter - `flutter test`, buat test/widget_test.dart untuk UI test.

2. Integration Test: Buat Postman collection: Login (save token as variable), GET /api/students (use token), POST /api/payments. Run collection runner untuk automated.

3. End-to-End Test:
   - Start docker-compose up.
   - Login via frontend (localhost:80), add student, verify di db dengan mysql client: `docker exec -it <db_container> mysql -u simuser -p simsekolah -e "SELECT * FROM students;"`.
   - Install APK, login via mobile, load students, add payment, check sync di backend logs.
   - Test HRM: Run HRM backend, add employee via Postman /api/hrm/employees, link to SIM payment, test flow payroll.

4. Performance/Security Test: Load test dengan JMeter untuk API. Security scan dengan OWASP ZAP. Check logs Docker `docker-compose logs -f` untuk error.

Penjelasan: E2E simulasi user, integration test component. Gunakan Testcontainers untuk Docker test di JUnit.

Troubleshooting: Test fail? Seed data di migration V3__Seed_data.sql. Mobile not sync? Check IP dan network.

## Latihan
1. Buat Dockerfile multi-stage dan docker-compose.yml lengkap dengan healthcheck, run dan test backend + db connection.
2. Implement Nginx service di docker-compose, test frontend serve dan API proxy.
3. Build signed APK Flutter, install di device, test full flow login-add student.
4. Test integrasi HRM: Run HRM backend, port employee endpoint, call dari SIM PaymentService.
5. Buat Postman collection 10 requests untuk E2E, run dan export report.
6. Dokumentasi: Update README.md dengan deployment guide, screenshots, dan troubleshooting FAQ.

Tips: Gunakan .env untuk secrets, git ignore. Deploy ke cloud: Heroku untuk simple, AWS ECS untuk Docker. Monitor dengan Grafana. Selamat! Anda telah membangun, deploy, dan test aplikasi SIM_CLONE lengkap. Extend dengan fitur seperti push notifications menggunakan Firebase.