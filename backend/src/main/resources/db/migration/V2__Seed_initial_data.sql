-- Seed initial data for SIM Sekolah
-- Version 2.0.0

-- Insert permissions
INSERT INTO permissions (name, description, resource, action) VALUES
('USER_READ', 'Read user information', 'USER', 'READ'),
('USER_CREATE', 'Create new users', 'USER', 'CREATE'),
('USER_UPDATE', 'Update user information', 'USER', 'UPDATE'),
('USER_DELETE', 'Delete users', 'USER', 'DELETE'),
('STUDENT_READ', 'Read student information', 'STUDENT', 'READ'),
('STUDENT_CREATE', 'Create new students', 'STUDENT', 'CREATE'),
('STUDENT_UPDATE', 'Update student information', 'STUDENT', 'UPDATE'),
('STUDENT_DELETE', 'Delete students', 'STUDENT', 'DELETE'),
('TEACHER_READ', 'Read teacher information', 'TEACHER', 'READ'),
('TEACHER_CREATE', 'Create new teachers', 'TEACHER', 'CREATE'),
('TEACHER_UPDATE', 'Update teacher information', 'TEACHER', 'UPDATE'),
('TEACHER_DELETE', 'Delete teachers', 'TEACHER', 'DELETE'),
('ATTENDANCE_READ', 'Read attendance records', 'ATTENDANCE', 'READ'),
('ATTENDANCE_CREATE', 'Create attendance records', 'ATTENDANCE', 'CREATE'),
('ATTENDANCE_UPDATE', 'Update attendance records', 'ATTENDANCE', 'UPDATE'),
('GRADE_READ', 'Read grade information', 'GRADE', 'READ'),
('GRADE_CREATE', 'Create grade records', 'GRADE', 'CREATE'),
('GRADE_UPDATE', 'Update grade records', 'GRADE', 'UPDATE'),
('PAYMENT_READ', 'Read payment information', 'PAYMENT', 'READ'),
('PAYMENT_CREATE', 'Create payment records', 'PAYMENT', 'CREATE'),
('PAYMENT_UPDATE', 'Update payment records', 'PAYMENT', 'UPDATE'),
('REPORT_READ', 'Read reports', 'REPORT', 'READ'),
('SYSTEM_ADMIN', 'Full system administration', 'SYSTEM', 'ADMIN');

-- Insert roles
INSERT INTO roles (name, description, is_system_role) VALUES
('SUPER_ADMIN', 'Super Administrator with full access', TRUE),
('ADMIN', 'Administrator with management access', TRUE),
('TEACHER', 'Teacher role', TRUE),
('STUDENT', 'Student role', TRUE),
('PARENT', 'Parent role', TRUE);

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- SUPER_ADMIN gets all permissions
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20), (1, 21), (1, 22), (1, 23),
-- ADMIN gets most permissions except system admin
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16), (2, 17), (2, 18), (2, 19), (2, 20), (2, 21), (2, 22),
-- TEACHER gets teacher and student related permissions
(3, 5), (3, 6), (3, 7), (3, 13), (3, 14), (3, 15), (3, 16), (3, 17), (3, 18), (3, 22),
-- STUDENT gets basic read permissions
(4, 5), (4, 13), (4, 16), (4, 19),
-- PARENT gets basic read permissions for their children
(5, 5), (5, 13), (5, 16), (5, 19);

-- Insert admin user
INSERT INTO users (username, email, password, first_name, last_name, user_type, phone, is_active) VALUES
('admin', 'admin@simsekolah.com', '$2a$10$8K3VZ8Q3Q3Q3Q3Q3Q3Q3QOeF5w5w5w5w5w5w5w5w5w5w5w5w5w5w', 'Administrator', '', 'ADMIN', '+6281234567890', TRUE);

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2); -- ADMIN role

-- Note: Additional seed data for teachers, students, subjects, etc. can be added here
-- For now, we only seed the essential authentication data (users, roles, permissions)