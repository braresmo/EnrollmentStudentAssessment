package com.app.back.core.impl;

import com.app.back.core.IStudent;
import com.app.back.model.Role;
import com.app.back.model.Student;
import com.app.back.repository.RoleRepository;
import com.app.back.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService implements IStudent {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public Student save(Student student) throws InternalServerError, Exception {
        try {
            // Determine if this is an update operation
            boolean isUpdate = (student.getUserId() != null);
            
            // Hash password if it's provided in the temporary password field
            if (student.getPassword() != null && !student.getPassword().isEmpty()) {
                student.setPasswordHash(passwordEncoder.encode(student.getPassword()));
                // Clear the temporary password field
                student.setPassword(null);
            } else if (isUpdate) {
                // For updates, preserve existing password if no new password provided
                Optional<Student> existingStudent = studentRepository.findById(student.getUserId());
                if (existingStudent.isPresent()) {
                    student.setPasswordHash(existingStudent.get().getPasswordHash());
                } else {
                    throw new Exception("Student not found for update");
                }
            } else {
                // For new students, password is required
                throw new Exception("Password is required for new students");
            }
            
            // Handle roles: assign STUDENT role for new students or preserve existing roles for updates
            if (student.getRoles() == null || student.getRoles().isEmpty()) {
                if (!isUpdate) {
                    // New student - assign STUDENT role
                    System.out.println("Looking for STUDENT role for new student...");
                    Optional<Role> studentRole = roleRepository.findByName("STUDENT");
                    if (studentRole.isPresent()) {
                        System.out.println("STUDENT role found, assigning...");
                        Set<Role> roles = new HashSet<>();
                        roles.add(studentRole.get());
                        student.setRoles(roles);
                    } else {
                        System.err.println("STUDENT role not found in database!");
                        throw new Exception("STUDENT role not found in database");
                    }
                } else {
                    // Update - preserve existing roles
                    System.out.println("Update without roles - preserving existing roles");
                    Optional<Student> existingStudent = studentRepository.findById(student.getUserId());
                    if (existingStudent.isPresent()) {
                        student.setRoles(existingStudent.get().getRoles());
                        System.out.println("Existing roles preserved for update");
                    }
                }
            }
            
            System.out.println("Checking for existing students...");
            Optional<Student> studentByNumber = findByStudentNumber(student.getStudentNumber());
            Optional<Student> studentByEmail = findByEmail(student.getEmail());
            
            System.out.println("Student number exists: " + studentByNumber.isPresent());
            System.out.println("Email exists: " + studentByEmail.isPresent());

            // Case 1: Create a new student
            // The student ID is null and no student exists with this number or email.
            if (student.getUserId() == null && studentByNumber.isEmpty() && studentByEmail.isEmpty()) {
                System.out.println("Creating new student...");
                Student saved = studentRepository.save(student);
                System.out.println("Student created successfully with ID: " + saved.getUserId());
                return saved;
            }

            // Case 2: Update an existing student
            if (student.getUserId() != null) {
                System.out.println("Updating existing student with ID: " + student.getUserId());
                
                // Preserve existing enrollments and other relationships
                Optional<Student> existingStudent = studentRepository.findById(student.getUserId());
                if (existingStudent.isPresent()) {
                    Student existing = existingStudent.get();
                    System.out.println("Found existing student for update");
                    System.out.println("Existing enrollments before preservation: " + (existing.getEnrollments() != null ? existing.getEnrollments().size() : "null"));
                    
                    // Force load enrollments if they are lazy-loaded
                    if (existing.getEnrollments() != null) {
                        existing.getEnrollments().size(); // This forces Hibernate to load the collection
                        System.out.println("Enrollments collection loaded, size: " + existing.getEnrollments().size());
                    }
                    
                    // Preserve enrollments
                    student.setEnrollments(existing.getEnrollments());
                    System.out.println("Enrollments preserved in student object: " + (student.getEnrollments() != null ? student.getEnrollments().size() : "null"));
                    
                    // Also preserve other fields that might not be included in the update
                    if (student.getFailedLoginAttempts() == null) {
                        student.setFailedLoginAttempts(existing.getFailedLoginAttempts());
                    }
                    if (student.getAccountLockedUntil() == null) {
                        student.setAccountLockedUntil(existing.getAccountLockedUntil());
                    }
                    if (student.getLastLogin() == null) {
                        student.setLastLogin(existing.getLastLogin());
                    }
                } else {
                    System.err.println("WARNING: Existing student not found for update!");
                }
                
                // Check if student number belongs to this student or doesn't exist
                boolean numberOk = studentByNumber.isEmpty() || 
                                  studentByNumber.get().getUserId().equals(student.getUserId());
                // Check if email belongs to this student or doesn't exist
                boolean emailOk = studentByEmail.isEmpty() || 
                                 studentByEmail.get().getUserId().equals(student.getUserId());
                
                System.out.println("Student number validation: " + numberOk);
                System.out.println("Email validation: " + emailOk);
                
                if (numberOk && emailOk) {
                    System.out.println("About to save student with enrollments: " + (student.getEnrollments() != null ? student.getEnrollments().size() : "null"));
                    Student saved = studentRepository.save(student);
                    System.out.println("Student updated successfully");
                    System.out.println("Saved student enrollments count: " + (saved.getEnrollments() != null ? saved.getEnrollments().size() : "null"));
                    return saved;
                }
            }
            
            System.err.println("Validation failed - Student number or email conflict, or ID mismatch");
            throw new Exception("Error saving student: Student number or email may already exist or there is an ID mismatch.");
            
        } catch (Exception e) {
            System.err.println("Exception in StudentService.save: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public Optional<Student> findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    @Override
    public List<Student> findByCohort(String cohort) {
        return studentRepository.findByCohort(cohort);
    }
}
