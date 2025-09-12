package com.simsekolah.service;

import com.simsekolah.entity.*;
import com.simsekolah.enums.UserType;
import com.simsekolah.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeederService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void seedDatabase() {
        log.info("Starting database seeding...");

        // Create permissions
        createPermissions();

        // Create roles
        createRoles();

        // Create admin user
        createAdminUser();

        // Create sample teacher
        createSampleTeacher();

        log.info("Database seeding completed successfully!");
    }

    private void createPermissions() {
        List<String> permissionNames = Arrays.asList(
            "USER_READ", "USER_WRITE", "USER_DELETE",
            "STUDENT_READ", "STUDENT_WRITE", "STUDENT_DELETE",
            "TEACHER_READ", "TEACHER_WRITE", "TEACHER_DELETE",
            "SUBJECT_READ", "SUBJECT_WRITE", "SUBJECT_DELETE",
            "SCHEDULE_READ", "SCHEDULE_WRITE", "SCHEDULE_DELETE",
            "GRADE_READ", "GRADE_WRITE", "GRADE_DELETE",
            "ATTENDANCE_READ", "ATTENDANCE_WRITE", "ATTENDANCE_DELETE",
            "PAYMENT_READ", "PAYMENT_WRITE", "PAYMENT_DELETE",
            "REPORT_READ", "REPORT_WRITE"
        );

        for (String permissionName : permissionNames) {
            if (permissionRepository.findByName(permissionName).isEmpty()) {
                Permission permission = new Permission();
                permission.setName(permissionName);
                permission.setDescription(permissionName.replace("_", " ").toLowerCase());
                permissionRepository.save(permission);
                log.info("Created permission: {}", permissionName);
            }
        }
    }

    private void createRoles() {
        // Admin role with all permissions
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Administrator with full access");
            adminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
            roleRepository.save(adminRole);
            log.info("Created role: ROLE_ADMIN");
        }

        // Teacher role with limited permissions
        if (roleRepository.findByName("ROLE_TEACHER").isEmpty()) {
            Role teacherRole = new Role();
            teacherRole.setName("ROLE_TEACHER");
            teacherRole.setDescription("Teacher with teaching-related access");

            Set<Permission> teacherPermissions = new HashSet<>();
            Arrays.asList("STUDENT_READ", "SUBJECT_READ", "SCHEDULE_READ", "GRADE_READ", "GRADE_WRITE",
                         "ATTENDANCE_READ", "ATTENDANCE_WRITE", "REPORT_READ")
                .forEach(permName -> {
                    permissionRepository.findByName(permName).ifPresent(teacherPermissions::add);
                });

            teacherRole.setPermissions(teacherPermissions);
            roleRepository.save(teacherRole);
            log.info("Created role: ROLE_TEACHER");
        }

        // Parent role with read-only permissions
        if (roleRepository.findByName("ROLE_PARENT").isEmpty()) {
            Role parentRole = new Role();
            parentRole.setName("ROLE_PARENT");
            parentRole.setDescription("Parent with read-only access");

            Set<Permission> parentPermissions = new HashSet<>();
            Arrays.asList("STUDENT_READ", "GRADE_READ", "ATTENDANCE_READ", "PAYMENT_READ", "REPORT_READ")
                .forEach(permName -> {
                    permissionRepository.findByName(permName).ifPresent(parentPermissions::add);
                });

            parentRole.setPermissions(parentPermissions);
            roleRepository.save(parentRole);
            log.info("Created role: ROLE_PARENT");
        }
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@simsekolah.com");
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setUserType(UserType.ADMIN);
            adminUser.setIsActive(true);
            adminUser.setCreatedAt(LocalDateTime.now());
            adminUser.setUpdatedAt(LocalDateTime.now());

            // Assign admin role
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            adminUser.setRoles(Set.of(adminRole));

            userRepository.save(adminUser);
            log.info("Created admin user: admin/admin123");
        }
    }

    private void createSampleTeacher() {
        if (userRepository.findByUsername("teacher").isEmpty()) {
            User teacherUser = new User();
            teacherUser.setUsername("teacher");
            teacherUser.setEmail("teacher@simsekolah.com");
            teacherUser.setFirstName("Ahmad");
            teacherUser.setLastName("Santoso");
            teacherUser.setPassword(passwordEncoder.encode("teacher123"));
            teacherUser.setUserType(UserType.TEACHER);
            teacherUser.setNip("198001011234567890");
            teacherUser.setPhone("+6281234567891");
            teacherUser.setIsActive(true);
            teacherUser.setCreatedAt(LocalDateTime.now());
            teacherUser.setUpdatedAt(LocalDateTime.now());

            // Assign teacher role
            Role teacherRole = roleRepository.findByName("ROLE_TEACHER").orElseThrow();
            teacherUser.setRoles(Set.of(teacherRole));

            userRepository.save(teacherUser);
            log.info("Created teacher user: teacher/teacher123");
        }
    }
}