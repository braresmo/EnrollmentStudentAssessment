package com.app.back.controller;

import com.app.back.core.impl.UserService;
import com.app.back.core.impl.StudentService;
import com.app.back.core.impl.InstructorService;
import com.app.back.core.impl.RoleService;
import com.app.back.dto.UserCreateDTO;
import com.app.back.dto.UserUpdateDTO;
import com.app.back.model.User;
import com.app.back.model.Student;
import com.app.back.model.Instructor;
import com.app.back.model.Role;
import com.app.back.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private SessionManager sessionManager;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<User>> listar() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> save(@RequestBody UserCreateDTO userDto) {
        try {
            // Determine user type based on roles
            boolean isInstructor = userDto.getRoles().stream()
                .anyMatch(role -> "INSTRUCTOR".equals(role.getName()));
            boolean isStudent = userDto.getRoles().stream()
                .anyMatch(role -> "STUDENT".equals(role.getName()));
            
            User user;
            
            if (isInstructor) {
                Instructor instructor = new Instructor();
                populateUserFields(instructor, userDto);
                // Generate employee number for instructor
                instructor.setEmployeeNumber("EMP" + System.currentTimeMillis());
                user = instructorService.save(instructor);
            } else if (isStudent) {
                Student student = new Student();
                populateUserFields(student, userDto);
                // Generate student number
                student.setStudentNumber("STU" + System.currentTimeMillis());
                user = studentService.save(student);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            return ResponseEntity.ok(user);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    private void populateUserFields(User user, UserCreateDTO userDto) throws Exception {
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Set temporary password field
        user.setTenantId(userDto.getTenantId());
        user.setActive("ACTIVE".equals(userDto.getStatus()));
        
        // Set roles
        Set<Role> roles = new HashSet<>();
        for (UserCreateDTO.RoleDTO roleDto : userDto.getRoles()) {
            Optional<Role> role = roleService.findByName(roleDto.getName());
            if (role.isPresent()) {
                roles.add(role.get());
            }
        }
        user.setRoles(roles);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(@RequestBody UserUpdateDTO userDto) {
        try {
            // Find the existing user
            Optional<User> existingUserOpt = userService.findById(userDto.getUserId());
            if (existingUserOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            User existingUser = existingUserOpt.get();
            
            // Determine user type and create appropriate instance
            User userToUpdate;
            if (existingUser instanceof Student) {
                Student student = (Student) existingUser;
                populateUserFieldsForUpdate(student, userDto);
                userToUpdate = studentService.save(student);
            } else if (existingUser instanceof Instructor) {
                Instructor instructor = (Instructor) existingUser;
                populateUserFieldsForUpdate(instructor, userDto);
                userToUpdate = instructorService.save(instructor);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            return ResponseEntity.ok(userToUpdate);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    private void populateUserFieldsForUpdate(User user, UserUpdateDTO userDto) throws Exception {
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        
        // Only set password if provided (not null and not empty)
        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            user.setPassword(userDto.getPassword()); // Set temporary password field for services to encrypt
        }
        // If password is null or empty, the existing passwordHash will be preserved by the services
        
        user.setTenantId(userDto.getTenantId());
        user.setActive("ACTIVE".equals(userDto.getStatus()));
        
        // Set roles
        Set<Role> roles = new HashSet<>();
        for (UserUpdateDTO.RoleDTO roleDto : userDto.getRoles()) {
            Optional<Role> role = roleService.findByName(roleDto.getName());
            if (role.isPresent()) {
                roles.add(role.get());
            }
        }
        user.setRoles(roles);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<User>> delete(@PathVariable Integer id, HttpServletRequest request) {
        try {
            // Check if user has ADMIN role
            Set<String> userRoles = sessionManager.getCurrentUserRoles(request.getSession());
            if (userRoles == null || !userRoles.contains("ADMIN")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                userService.delete(id);
                return ResponseEntity.ok(userService.findAll());
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        try {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findByEmail/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
