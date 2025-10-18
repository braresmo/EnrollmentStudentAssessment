-- ========================================
-- INITIALIZATION DATA SCRIPT
-- ========================================

-- 1. ROLES (independent, no FK)
INSERT INTO role (name) VALUES ('STUDENT');
INSERT INTO role (name) VALUES ('INSTRUCTOR');
INSERT INTO role (name) VALUES ('ADMIN');

-- 2. ROLE PERMISSIONS
INSERT INTO role_permissions (role_id, permission) VALUES (1, 'VIEW_COURSES');
INSERT INTO role_permissions (role_id, permission) VALUES (1, 'ENROLL_COURSE');
INSERT INTO role_permissions (role_id, permission) VALUES (1, 'VIEW_PROGRESS');

INSERT INTO role_permissions (role_id, permission) VALUES (2, 'CREATE_COURSE');
INSERT INTO role_permissions (role_id, permission) VALUES (2, 'EDIT_COURSE');
INSERT INTO role_permissions (role_id, permission) VALUES (2, 'VIEW_STUDENTS');
INSERT INTO role_permissions (role_id, permission) VALUES (2, 'GRADE_STUDENTS');

INSERT INTO role_permissions (role_id, permission) VALUES (3, 'ADMIN_ALL');
INSERT INTO role_permissions (role_id, permission) VALUES (3, 'MANAGE_USERS');
INSERT INTO role_permissions (role_id, permission) VALUES (3, 'SYSTEM_CONFIG');

-- 3. BASE USERS (parent table in JOINED inheritance)
INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'John Smith', 'john.smith@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'Sarah Johnson', 'sarah.johnson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'Michael Brown', 'michael.brown@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'Emily Davis', 'emily.davis@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'David Wilson', 'david.wilson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO users (tenant_id, name, email, password_hash, is_active) VALUES 
(1, 'Lisa Anderson', 'lisa.anderson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

-- 4. INSTRUCTORS (JOINED inheritance - user_id is FK to user)
INSERT INTO instructor (user_id, employee_number, office_hours) VALUES 
(1, 'EMP001', 'Monday to Friday 9:00-11:00 AM');

INSERT INTO instructor (user_id, employee_number, office_hours) VALUES 
(2, 'EMP002', 'Tuesday and Thursday 2:00-4:00 PM');

INSERT INTO instructor (user_id, employee_number, office_hours) VALUES 
(3, 'EMP003', 'Monday, Wednesday and Friday 10:00 AM-12:00 PM');

-- 5. STUDENTS (JOINED inheritance - user_id is FK to user)
INSERT INTO student (user_id, student_number, cohort) VALUES 
(4, 'STU001', '2024-Fall');

INSERT INTO student (user_id, student_number, cohort) VALUES 
(5, 'STU002', '2024-Fall');

INSERT INTO student (user_id, student_number, cohort) VALUES 
(6, 'STU003', '2024-Spring');

-- 6. USER-ROLE RELATIONSHIPS
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2); -- John is INSTRUCTOR
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- Sarah is INSTRUCTOR  
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2); -- Michael is INSTRUCTOR
INSERT INTO user_roles (user_id, role_id) VALUES (4, 1); -- Emily is STUDENT
INSERT INTO user_roles (user_id, role_id) VALUES (5, 1); -- David is STUDENT
INSERT INTO user_roles (user_id, role_id) VALUES (6, 1); -- Lisa is STUDENT

-- 7. COURSES (require instructor_id)
INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(1, 'CS101', 'Introduction to Computer Science', 'ACTIVE', CURRENT_TIMESTAMP(), 1);

INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(1, 'ENG202', 'Advanced English Literature', 'ACTIVE', CURRENT_TIMESTAMP(), 2);

INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(2, 'MKT301', 'Digital Marketing Strategies', 'ACTIVE', CURRENT_TIMESTAMP(), 3);

INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(2, 'HIS450', 'World History Since 1900', 'ACTIVE', CURRENT_TIMESTAMP(), 1);

INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(1, 'PHY100', 'Fundamentals of Physics', 'ACTIVE', CURRENT_TIMESTAMP(), 2);

INSERT INTO course (id_tenant, code, title, status, published_at, instructor_id) VALUES 
(3, 'SEC200', 'Cybersecurity Essentials', 'ACTIVE', CURRENT_TIMESTAMP(), 3);

-- 8. MODULES (require course_id)
INSERT INTO module (title, sequence, id_course) VALUES ('Introduction to Programming', 1, 1);
INSERT INTO module (title, sequence, id_course) VALUES ('Data Structures', 2, 1);
INSERT INTO module (title, sequence, id_course) VALUES ('Basic Algorithms', 3, 1);

INSERT INTO module (title, sequence, id_course) VALUES ('Victorian Poetry', 1, 2);
INSERT INTO module (title, sequence, id_course) VALUES ('Modern Narrative', 2, 2);

INSERT INTO module (title, sequence, id_course) VALUES ('Social Media Marketing', 1, 3);
INSERT INTO module (title, sequence, id_course) VALUES ('SEO and SEM', 2, 3);

INSERT INTO module (title, sequence, id_course) VALUES ('World War I', 1, 4);
INSERT INTO module (title, sequence, id_course) VALUES ('World War II', 2, 4);

INSERT INTO module (title, sequence, id_course) VALUES ('Classical Mechanics', 1, 5);
INSERT INTO module (title, sequence, id_course) VALUES ('Thermodynamics', 2, 5);

INSERT INTO module (title, sequence, id_course) VALUES ('Security Fundamentals', 1, 6);

-- 9. CONTENT ITEMS (require module_id)
INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('VIDEO', 'What is Programming?', 'https://example.com/video1', 300, 1);
INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('DOCUMENT', 'History of Computing', 'https://example.com/doc1', NULL, 1);
INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('QUIZ', 'Introduction Quiz', 'https://example.com/quiz1', 600, 1);

INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('VIDEO', 'Arrays and Lists', 'https://example.com/video2', 450, 2);
INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('EXERCISE', 'Array Exercises', 'https://example.com/exercise1', 1200, 2);

INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('VIDEO', 'Introduction to Algorithms', 'https://example.com/video3', 420, 3);

INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('DOCUMENT', 'Victorian Poets', 'https://example.com/doc2', NULL, 4);

INSERT INTO content_item (type, title, uri, duration_sec, module_id) VALUES 
('VIDEO', 'Facebook Marketing Strategies', 'https://example.com/video4', 360, 6);

-- 10. ENROLLMENTS (require course_id and student_id)
INSERT INTO enrollment (status, enrolled_at, course_id, student_id) VALUES 
('ACTIVE', CURRENT_TIMESTAMP(), 1, 4); -- Emily in CS101

INSERT INTO enrollment (status, enrolled_at, course_id, student_id) VALUES 
('ACTIVE', CURRENT_TIMESTAMP(), 1, 5); -- David in CS101

INSERT INTO enrollment (status, enrolled_at, course_id, student_id) VALUES 
('ACTIVE', CURRENT_TIMESTAMP(), 2, 6); -- Lisa in ENG202

INSERT INTO enrollment (status, enrolled_at, course_id, student_id) VALUES 
('ACTIVE', CURRENT_TIMESTAMP(), 3, 4); -- Emily in MKT301

INSERT INTO enrollment (status, enrolled_at, course_id, student_id) VALUES 
('COMPLETED', CURRENT_TIMESTAMP(), 5, 5); -- David in PHY100

-- 11. CONTENT COMPLETIONS (simulating some students have completed content)
INSERT INTO content_completions_by_student (content_id, student_id) VALUES (1, 4); -- Emily completed video 1
INSERT INTO content_completions_by_student (content_id, student_id) VALUES (2, 4); -- Emily completed document 1
INSERT INTO content_completions_by_student (content_id, student_id) VALUES (1, 5); -- David completed video 1
