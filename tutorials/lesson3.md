# Pembelajaran 3: Menambahkan Security dan Authentication di Backend

## Pengenalan
Pembelajaran ini membahas implementasi keamanan menggunakan JWT (JSON Web Token) untuk authentication dan authorization di backend Java Spring Boot. JWT adalah standar terbuka (RFC 7519) untuk representasi claim secara aman sebagai JSON object, yang berguna untuk API stateless seperti REST. Daripada session cookie, JWT memungkinkan client menyimpan token dan kirim di header setiap request, cocok untuk aplikasi web dan mobile.

Kita akan membuat model User (adaptasi dari HRM), UserPrincipal untuk Spring Security, JWT utilities untuk generate/validate token, filter untuk intercept request, service untuk load user, controller untuk login/register, dan konfigurasi security. Ini melindungi endpoint seperti /api/students agar hanya user authenticated yang bisa akses.

Konsep kunci: Authentication (siapa user ini?) menggunakan username/password, Authorization (apa yang bisa user lakukan?) menggunakan roles seperti ROLE_ADMIN, ROLE_TEACHER. Spring Security handle CSRF, CORS, dll. Estimasi waktu: 5-7 jam. Pastikan lesson 2 selesai, database punya user table.

Troubleshooting umum: Token expired? Implement refresh token. CORS error? Konfigurasi @CrossOrigin atau global CORS.

## Langkah 1: Setup Dependencies
Tambah dependencies di pom.xml backend (atau HRM backend jika adaptasi). Update <dependencies> section:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```
Penjelasan: spring-boot-starter-security include BCrypt password encoder, UserDetailsService. jjwt untuk JWT operations (api for compile, impl and jackson for runtime). Jalankan `mvn clean install` untuk download.

Tambah juga validation:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Troubleshooting: Jika version conflict, gunakan <dependencyManagement> atau update Spring Boot version di parent pom.

## Langkah 2: Buat User Model dan Principal
Gunakan atau adaptasi model User dari HRM: [`User.java`](HRM dan Penggajian/backend/src/main/java/com/hrm/model/User.java:1). Buat entity User di backend/src/main/java/com/simsekolah/model/User.java:
```java
package com.simsekolah.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;  // Encoded dengan BCrypt

    @Column(nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();  // e.g., "ROLE_USER", "ROLE_ADMIN"

    // Constructors
    public User() {}

    public User(String username, String email, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
```
Penjelasan: @ElementCollection untuk roles sederhana. Password disimpan encoded. Buat migration V2__Create_users_table.sql untuk tabel users dan user_roles.

Buat UserPrincipal di backend/src/main/java/com/simsekolah/security/UserPrincipal.java:1 untuk implement Spring's UserDetails:
```java
package com.simsekolah.security;

import com.simsekolah.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Implementasi method lain: isAccountNonExpired, isAccountNonLocked, etc. return true untuk sederhana
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public Long getId() { return id; }
}
```
Penjelasan: UserPrincipal wrap User untuk Spring Security. GrantedAuthority untuk roles.

Troubleshooting: Jika roles tidak load, check FetchType.EAGER.

## Langkah 3: JWT Utilities
Buat class utilitas di backend/src/main/java/com/simsekolah/security/JwtUtils.java:1 untuk handle JWT:
```java
package com.simsekolah.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${simsekolah.app.jwtSecret}")
    private String jwtSecret;  // Inject dari application.properties

    @Value("${simsekolah.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername(), userPrincipal.getId());
    }

    public String generateTokenFromUsername(String username, Long id) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("id", id)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (Exception e) {
            logger.error("JWT validation error: ", e);
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
```
Penjelasan: generateJwtToken buat token setelah login sukses. Validate check signature dan expiration. Secret key di properties untuk security. Expiration 24 jam tipikal.

Di application.properties tambah:
```properties
simsekolah.app.jwtSecret=your-very-secure-secret-key-min-32-chars
simsekolah.app.jwtExpirationMs=86400000  # 24 hours
```

Troubleshooting: Jika "Invalid signature", check secret key match. Gunakan HS512 untuk security lebih baik.

## Langkah 4: Authentication Filter
Filter ini extend OncePerRequestFilter untuk run sekali per request. Buat backend/src/main/java/com/simsekolah/security/JwtAuthenticationFilter.java:1:
```java
package com.simsekolah.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {0}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
```
Penjelasan: doFilterInternal extract token dari header Authorization: Bearer <token>. Jika valid, set authentication di SecurityContext untuk akses di controller. OncePerRequestFilter pastikan run sekali.

Troubleshooting: NullPointer jika @Autowired gagal, check component scan.

## Langkah 5: Service dan Controller
Buat service untuk load user di backend/src/main/java/com/simsekolah/service/UserDetailsServiceImpl.java:1:
```java
package com.simsekolah.service;

import com.simsekolah.model.User;
import com.simsekolah.repository.UserRepository;
import com.simsekolah.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return UserPrincipal.create(user);
    }
}
```
Penjelasan: loadUserByUsername dipanggil oleh Spring untuk authenticate. @Transactional untuk database transaction.

Buat UserRepository extends JpaRepository<User, Long> dengan findByUsername.

Buat DTO untuk request/response di dto/request/LoginRequest.java dan dto/response/JwtResponse.java, MessageResponse.java.

Buat controller di backend/src/main/java/com/simsekolah/controller/AuthController.java:1:
```java
package com.simsekolah.controller;

import com.simsekolah.dto.request.LoginRequest;
import com.simsekolah.dto.response.JwtResponse;
import com.simsekolah.dto.response.MessageResponse;
import com.simsekolah.security.JwtUtils;
import com.simsekolah.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId().toString(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody LoginRequest signUpRequest) {
        // Logic register: check username exist, encode password, save user
        // Return JwtResponse similar to signin
    }
}
```
Penjelasan: AuthenticationManager authenticate credentials. Jika sukses, generate JWT. @CrossOrigin untuk frontend calls dari browser. @Valid untuk validation DTO.

Buat LoginRequest dengan @NotBlank pada username/password.

Response DTO: [`JwtResponse.java`](backend/src/main/java/com/simsekolah/dto/response/JwtResponse.java:1)
```java
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = authorities;
    }

    // Getters
}
```

Troubleshooting: Jika 401, check password encoder di service. Gunakan BCryptPasswordEncoder @Bean di config.

## Langkah 6: Konfigurasi Security
Buat backend/src/main/java/com/simsekolah/config/SecurityConfig.java:1:
```java
package com.simsekolah.config;

import com.simsekolah.security.JwtAuthenticationEntryPoint;
import com.simsekolah.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }
}
```
Penjelasan: Disable CSRF untuk API. Session STATELESS karena JWT. permitAll untuk /auth, authenticated untuk lainnya. Add filter sebelum default filter. @EnableMethodSecurity untuk @PreAuthorize di controller method.

Buat JwtAuthenticationEntryPoint untuk handle unauthorized error.

Tambah di application.properties:
```properties
logging.level.org.springframework.security=DEBUG
```

Troubleshooting: Jika CORS error, tambah @CrossOrigin di controller atau global CorsConfigurationSource bean.

## Latihan
1. Tambah dependencies dan update pom.xml, test build.
2. Buat User entity, UserPrincipal, dan repository. Seed data user di migration.
3. Implement JwtUtils dengan full method, test generate token manual.
4. Buat JwtAuthenticationFilter dan test doFilter dengan mock request.
5. Implement UserDetailsServiceImpl dan AuthController, test login dengan Postman (POST /api/auth/signin dengan JSON { "username": "test", "password": "test" }, expect JWT response).
6. Konfigurasi SecurityConfig, test protected endpoint (GET /api/students dengan dan tanpa token, expect 401 tanpa token).

Tips: Gunakan jwt.io untuk decode dan verify token. Encode password dengan BCrypt online tool untuk test. Commit per component.

Lanjut ke Pembelajaran 4 untuk membangun frontend.