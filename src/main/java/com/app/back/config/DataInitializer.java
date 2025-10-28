package com.app.back.config;

import com.app.back.model.Role;
import com.app.back.model.User;
import com.app.back.model.Student;
import com.app.back.model.Instructor;
import com.app.back.repository.RoleRepository;
import com.app.back.repository.UserRepository;
import com.app.back.repository.StudentRepository;
import com.app.back.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INITIALIZING TEST DATA ===");
        
        // Always generate a fresh password hash for testing
        String encodedPassword = passwordEncoder.encode("password123");
        System.out.println("Fresh encoded password for 'password123': " + encodedPassword);
        
        // Test the encoded password
        boolean testMatch = passwordEncoder.matches("password123", encodedPassword);
        System.out.println("Test BCrypt match for fresh hash: " + testMatch);
        
        // Check if data already exists
        long userCount = userRepository.count();
        System.out.println("Found " + userCount + " users in database");
        
        if (userCount > 0) {
            System.out.println("Existing users:");
            userRepository.findAll().forEach(user -> {
                System.out.println("- " + user.getName() + " (" + user.getEmail() + ") - Password: " + user.getPasswordHash());
            });
            
            // Update all users with the correct password hash
            System.out.println("Updating all users with correct password hash...");
            userRepository.findAll().forEach(user -> {
                user.setPasswordHash(encodedPassword);
                userRepository.save(user);
                System.out.println("Updated password for: " + user.getEmail());
            });
            
            System.out.println("Password update complete!");
            return;
        }

        // Create roles
        Role studentRole = createRoleIfNotExists("STUDENT");
        Role instructorRole = createRoleIfNotExists("INSTRUCTOR");
        createRoleIfNotExists("ADMIN"); // Create admin role for future use

        // Create instructor users
        createInstructorUser("John Smith", "john.smith@email.com", encodedPassword, "EMP001", "Monday to Friday 9:00-11:00 AM", instructorRole);
        createInstructorUser("Sarah Johnson", "sarah.johnson@email.com", encodedPassword, "EMP002", "Tuesday and Thursday 2:00-4:00 PM", instructorRole);

        // Create student users
        createStudentUser("Emily Davis", "emily.davis@email.com", encodedPassword, "STU003", "Cohort 2023", studentRole);
        createStudentUser("Michael Brown", "michael.brown@email.com", encodedPassword, "STU004", "Cohort 2023", studentRole);
        createStudentUser("David Wilson", "david.wilson@email.com", encodedPassword, "STU005", "Cohort 2024", studentRole);
        createStudentUser("Lisa Anderson", "lisa.anderson@email.com", encodedPassword, "STU006", "Cohort 2024", studentRole);

        System.out.println("=== TEST DATA INITIALIZATION COMPLETE ===");
        System.out.println("Users created: " + userRepository.count());
    }

    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    private void createInstructorUser(String name, String email, String passwordHash, String employeeNumber, String officeHours, Role role) {
        // Create base user
        User user = new User() {}; // Anonymous implementation since User is abstract
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setTenantId(1);
        user.setActive(true);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setRoles(Set.of(role));
        
        User savedUser = userRepository.save(user);

        // Create instructor profile
        Instructor instructor = new Instructor();
        instructor.setUserId(savedUser.getUserId());
        instructor.setEmployeeNumber(employeeNumber);
        instructor.setOfficeHours(officeHours);
        // Copy user fields
        instructor.setName(name);
        instructor.setEmail(email);
        instructor.setPasswordHash(passwordHash);
        instructor.setTenantId(1);
        instructor.setActive(true);
        instructor.setFailedLoginAttempts(0);
        instructor.setAccountLockedUntil(null);
        instructor.setRoles(Set.of(role));

        instructorRepository.save(instructor);
        
        System.out.println("Created instructor: " + name + " (" + email + ")");
    }

    private void createStudentUser(String name, String email, String passwordHash, String studentNumber, String cohort, Role role) {
        // Create base user
        User user = new User() {}; // Anonymous implementation since User is abstract
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setTenantId(1);
        user.setActive(true);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setRoles(Set.of(role));
        
        User savedUser = userRepository.save(user);

        // Create student profile
        Student student = new Student();
        student.setUserId(savedUser.getUserId());
        student.setStudentNumber(studentNumber);
        student.setCohort(cohort);
        // Copy user fields
        student.setName(name);
        student.setEmail(email);
        student.setPasswordHash(passwordHash);
        student.setTenantId(1);
        student.setActive(true);
        student.setFailedLoginAttempts(0);
        student.setAccountLockedUntil(null);
        student.setRoles(Set.of(role));

        studentRepository.save(student);
        
        System.out.println("Created student: " + name + " (" + email + ")");
    }
}