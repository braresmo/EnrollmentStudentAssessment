package com.app.back.core.impl;

import com.app.back.core.IInstructor;
import com.app.back.model.Instructor;
import com.app.back.model.Role;
import com.app.back.repository.InstructorRepository;
import com.app.back.repository.RoleRepository;
import com.app.back.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class InstructorService implements IInstructor {

    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public Instructor save(Instructor instructor) throws InternalServerError, Exception {
        System.out.println("=== INSTRUCTOR SERVICE SAVE ===");
        System.out.println("Starting save process for instructor: " + instructor.getName());
        
        try {
            // Determine if this is an update operation
            boolean isUpdate = instructor.getUserId() != null;
            
            // Hash password if it's provided in the temporary password field
            if (instructor.getPassword() != null && !instructor.getPassword().isEmpty()) {
                System.out.println("Hashing password from temporary field...");
                instructor.setPasswordHash(passwordEncoder.encode(instructor.getPassword()));
                System.out.println("Password hashed successfully");
                // Clear the temporary password field
                instructor.setPassword(null);
            } else if (instructor.getPasswordHash() != null && !instructor.getPasswordHash().startsWith("$2a$")) {
                System.out.println("Hashing password from passwordHash field...");
                instructor.setPasswordHash(passwordEncoder.encode(instructor.getPasswordHash()));
                System.out.println("Password hashed successfully");
            } else if (!isUpdate) {
                // Only require password for new instructors
                System.err.println("No password provided for new instructor!");
                throw new Exception("Password is required for new instructors");
            } else if (isUpdate) {
                // For updates without password, preserve existing password
                System.out.println("Update without password - preserving existing password");
                Optional<Instructor> existingInstructor = instructorRepository.findById(instructor.getUserId());
                if (existingInstructor.isPresent()) {
                    instructor.setPasswordHash(existingInstructor.get().getPasswordHash());
                    System.out.println("Existing password preserved for update");
                } else {
                    throw new Exception("Instructor not found for update");
                }
            }
            
            // Handle roles: assign INSTRUCTOR role for new instructors or preserve existing roles for updates
            if (instructor.getRoles() == null || instructor.getRoles().isEmpty()) {
                if (!isUpdate) {
                    // New instructor - assign INSTRUCTOR role
                    System.out.println("Looking for INSTRUCTOR role for new instructor...");
                    Optional<Role> instructorRole = roleRepository.findByName("INSTRUCTOR");
                    if (instructorRole.isPresent()) {
                        System.out.println("INSTRUCTOR role found, assigning...");
                        Set<Role> roles = new HashSet<>();
                        roles.add(instructorRole.get());
                        instructor.setRoles(roles);
                    } else {
                        System.err.println("INSTRUCTOR role not found in database!");
                        throw new Exception("INSTRUCTOR role not found in database");
                    }
                } else {
                    // Update - preserve existing roles
                    System.out.println("Update without roles - preserving existing roles");
                    Optional<Instructor> existingInstructor = instructorRepository.findById(instructor.getUserId());
                    if (existingInstructor.isPresent()) {
                        instructor.setRoles(existingInstructor.get().getRoles());
                        System.out.println("Existing roles preserved for update");
                    }
                }
            }
            
            System.out.println("Checking for existing instructors...");
            Optional<Instructor> instructorByEmployeeNumber = findByEmployeeNumber(instructor.getEmployeeNumber());
            Optional<Instructor> instructorByEmail = findByEmail(instructor.getEmail());
            
            System.out.println("Employee number exists: " + instructorByEmployeeNumber.isPresent());
            System.out.println("Email exists: " + instructorByEmail.isPresent());

        // Case 1: Create a new instructor
        // The instructor ID is null and no instructor exists with this employee number or email.
        if (instructor.getUserId() == null && instructorByEmployeeNumber.isEmpty() && instructorByEmail.isEmpty()) {
            System.out.println("Creating new instructor...");
            Instructor saved = instructorRepository.save(instructor);
            System.out.println("Instructor created successfully with ID: " + saved.getUserId());
            return saved;
        }

        // Case 2: Update an existing instructor
        if (instructor.getUserId() != null) {
            System.out.println("Updating existing instructor with ID: " + instructor.getUserId());
            
            // Preserve existing courses and other relationships
            Optional<Instructor> existingInstructor = instructorRepository.findById(instructor.getUserId());
            if (existingInstructor.isPresent()) {
                Instructor existing = existingInstructor.get();
                System.out.println("Found existing instructor for update");
                System.out.println("Existing courses before preservation: " + (existing.getCourses() != null ? existing.getCourses().size() : "null"));
                
                // Force load courses if they are lazy-loaded
                if (existing.getCourses() != null) {
                    existing.getCourses().size(); // This forces Hibernate to load the collection
                    System.out.println("Courses collection loaded, size: " + existing.getCourses().size());
                }
                
                // Preserve courses
                instructor.setCourses(existing.getCourses());
                System.out.println("Courses preserved in instructor object: " + (instructor.getCourses() != null ? instructor.getCourses().size() : "null"));
                
                // Also preserve other fields that might not be included in the update
                if (instructor.getFailedLoginAttempts() == null) {
                    instructor.setFailedLoginAttempts(existing.getFailedLoginAttempts());
                }
                if (instructor.getAccountLockedUntil() == null) {
                    instructor.setAccountLockedUntil(existing.getAccountLockedUntil());
                }
                if (instructor.getLastLogin() == null) {
                    instructor.setLastLogin(existing.getLastLogin());
                }
            } else {
                System.err.println("WARNING: Existing instructor not found for update!");
            }
            
            // Check if employee number belongs to this instructor or doesn't exist
            boolean employeeNumberOk = instructorByEmployeeNumber.isEmpty() || 
                                      instructorByEmployeeNumber.get().getUserId().equals(instructor.getUserId());
            // Check if email belongs to this instructor or doesn't exist
            boolean emailOk = instructorByEmail.isEmpty() || 
                             instructorByEmail.get().getUserId().equals(instructor.getUserId());
            
            System.out.println("Employee number validation: " + employeeNumberOk);
            System.out.println("Email validation: " + emailOk);
            
            if (employeeNumberOk && emailOk) {
                System.out.println("About to save instructor with courses: " + (instructor.getCourses() != null ? instructor.getCourses().size() : "null"));
                
                // Use merge instead of save to better handle entity relationships
                // This should preserve the existing courses relationships
                Instructor saved = entityManager.merge(instructor);
                entityManager.flush(); // Force the persistence context to synchronize with the database
                
                System.out.println("Instructor updated successfully using merge");
                System.out.println("Saved instructor courses count: " + (saved.getCourses() != null ? saved.getCourses().size() : "null"));
                return saved;
            }
        }
        
        System.err.println("Validation failed - Employee number or email conflict, or ID mismatch");
        throw new Exception("Error saving instructor: Employee number or email may already exist or there is an ID mismatch.");
        
        } catch (Exception e) {
            System.err.println("Exception in InstructorService.save: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        try {
            // Check if instructor has courses assigned
            long courseCount = courseRepository.countByInstructorId(id);
            if (courseCount > 0) {
                throw new RuntimeException("Cannot delete instructor. This instructor has " + courseCount + " course(s) assigned. Please reassign or delete the courses first.");
            }
            
            // If no courses assigned, proceed with deletion
            instructorRepository.deleteById(id);
            System.out.println("Successfully deleted instructor with ID: " + id);
        } catch (RuntimeException e) {
            System.err.println("Error deleting instructor with ID " + id + ": " + e.getMessage());
            throw e; // Re-throw the runtime exception with the specific message
        } catch (Exception e) {
            System.err.println("Unexpected error deleting instructor with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to delete instructor: " + e.getMessage());
        }
    }

    @Override
    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    @Override
    public Optional<Instructor> findById(Integer id) {
        return instructorRepository.findById(id);
    }

    @Override
    public Optional<Instructor> findByEmail(String email) {
        return instructorRepository.findByEmail(email);
    }

    @Override
    public Optional<Instructor> findByEmployeeNumber(String employeeNumber) {
        return instructorRepository.findByEmployeeNumber(employeeNumber);
    }
}
