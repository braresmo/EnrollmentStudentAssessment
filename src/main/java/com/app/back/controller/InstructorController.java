package com.app.back.controller;

import com.app.back.core.impl.InstructorService;
import com.app.back.model.Instructor;
import com.app.back.repository.CourseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Instructor>> listar() {
        try {
            return ResponseEntity.ok(instructorService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getAllWithStats")
    public ResponseEntity<List<Map<String, Object>>> listarWithStats() {
        try {
            List<Instructor> instructors = instructorService.findAll();
            
            List<Map<String, Object>> instructorsWithStats = instructors.stream()
                .map(instructor -> {
                    Map<String, Object> instructorData = new HashMap<>();
                    
                    // Basic instructor info
                    instructorData.put("userId", instructor.getUserId());
                    instructorData.put("name", instructor.getName());
                    instructorData.put("email", instructor.getEmail());
                    instructorData.put("employeeNumber", instructor.getEmployeeNumber());
                    instructorData.put("officeHours", instructor.getOfficeHours());
                    instructorData.put("active", instructor.isActive());
                    
                    // Course statistics
                    Long courseCount = courseRepository.countByInstructorId(instructor.getUserId());
                    instructorData.put("courseCount", courseCount != null ? courseCount.intValue() : 0);
                    
                    return instructorData;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(instructorsWithStats);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Instructor> save(@RequestBody String jsonBody) {
        try {
            System.out.println("=== RAW JSON RECEIVED ===");
            System.out.println(jsonBody);
            System.out.println("========================");
            
            // Parse JSON manually
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonBody);
            
            // Create instructor object
            Instructor instructor = new Instructor();
            
            // Set User fields
            instructor.setTenantId(jsonNode.get("tenantId").asInt());
            instructor.setName(jsonNode.get("name").asText());
            instructor.setEmail(jsonNode.get("email").asText());
            instructor.setActive(jsonNode.get("active").asBoolean());
            
            // Extract password manually and set it
            if (jsonNode.has("password")) {
                String password = jsonNode.get("password").asText();
                System.out.println("Password extracted: " + (password != null && !password.isEmpty() ? "[PROVIDED]" : "[NULL]"));
                instructor.setPassword(password);
            }
            
            // Set Instructor fields
            instructor.setEmployeeNumber(jsonNode.get("employeeNumber").asText());
            if (jsonNode.has("officeHours") && !jsonNode.get("officeHours").isNull()) {
                instructor.setOfficeHours(jsonNode.get("officeHours").asText());
            }
            
            System.out.println("=== MANUALLY PARSED INSTRUCTOR DATA ===");
            System.out.println("User ID: " + instructor.getUserId());
            System.out.println("Tenant ID: " + instructor.getTenantId());
            System.out.println("Name: " + instructor.getName());
            System.out.println("Email: " + instructor.getEmail());
            System.out.println("Password (temp field): " + (instructor.getPassword() != null ? "[PROVIDED]" : "[NULL]"));
            System.out.println("Password Hash: " + (instructor.getPasswordHash() != null ? "[PROVIDED]" : "[NULL]"));
            System.out.println("Is Active: " + instructor.isActive());
            System.out.println("Employee Number: " + instructor.getEmployeeNumber());
            System.out.println("Office Hours: " + instructor.getOfficeHours());
            System.out.println("Roles: " + instructor.getRoles());
            System.out.println("=====================================");
            
            Instructor savedInstructor = instructorService.save(instructor);
            System.out.println("Instructor saved successfully with ID: " + savedInstructor.getUserId());
            return ResponseEntity.ok(savedInstructor);
        } catch (InternalServerError e) {
            System.err.println("InternalServerError in save: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.err.println("Exception in save: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<Map<String, Object>>> delete(@PathVariable Integer id) {
        try {
            System.out.println("=== DELETE INSTRUCTOR REQUEST ===");
            System.out.println("Instructor ID to delete: " + id);
            
            Optional<Instructor> instructor = instructorService.findById(id);
            if (instructor.isPresent()) {
                System.out.println("Instructor found: " + instructor.get().getName());
                
                instructorService.delete(id);
                System.out.println("Instructor deleted successfully");
                
                // Return updated list with statistics (same format as getAllWithStats)
                List<Instructor> instructors = instructorService.findAll();
                
                List<Map<String, Object>> instructorsWithStats = instructors.stream()
                    .map(inst -> {
                        Map<String, Object> instructorData = new HashMap<>();
                        
                        // Basic instructor info
                        instructorData.put("userId", inst.getUserId());
                        instructorData.put("name", inst.getName());
                        instructorData.put("email", inst.getEmail());
                        instructorData.put("employeeNumber", inst.getEmployeeNumber());
                        instructorData.put("officeHours", inst.getOfficeHours());
                        instructorData.put("active", inst.isActive());
                        
                        // Course statistics
                        Long courseCount = courseRepository.countByInstructorId(inst.getUserId());
                        instructorData.put("courseCount", courseCount != null ? courseCount.intValue() : 0);
                        
                        return instructorData;
                    })
                    .collect(Collectors.toList());
                
                System.out.println("Returning updated list with " + instructorsWithStats.size() + " instructors");
                return ResponseEntity.ok(instructorsWithStats);
            } else {
                System.out.println("Instructor not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            System.err.println("Business logic error deleting instructor: " + e.getMessage());
            // Return error response with specific message for the frontend
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(errorResponse));
        } catch (Exception e) {
            System.err.println("Error deleting instructor: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Instructor> update(@RequestBody Instructor instructor) {
        try {
            return ResponseEntity.ok(instructorService.save(instructor));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/findByEmployeeNumber/{employeeNumber}")
    public ResponseEntity<Instructor> findByEmployeeNumber(@PathVariable String employeeNumber) {
        try {
            Optional<Instructor> instructor = instructorService.findByEmployeeNumber(employeeNumber);
            if (instructor.isPresent()) {
                return ResponseEntity.ok(instructor.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
