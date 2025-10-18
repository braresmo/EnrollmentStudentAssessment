package com.app.back.controller;

import com.app.back.core.impl.EnrollmentService;
import com.app.back.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Enrollment>> listar() {
        try {
            return ResponseEntity.ok(enrollmentService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> save(@RequestBody Enrollment enrollment) {
        try {
            return ResponseEntity.ok(enrollmentService.save(enrollment));
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<Enrollment>> delete(@PathVariable Integer id) {
        try {
            Optional<Enrollment> enrollment = enrollmentService.findById(id);
            if (enrollment.isPresent()) {
                enrollmentService.delete(id);
                return ResponseEntity.ok(enrollmentService.findAll());
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enrollment> update(@RequestBody Enrollment enrollment) {
        try {
            return ResponseEntity.ok(enrollmentService.save(enrollment));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findByStatus/{status}")
    public ResponseEntity<List<Enrollment>> findByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(enrollmentService.findByStatus(status));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findByStudentId/{userId}")
    public ResponseEntity<List<Enrollment>> findByStudentId(@PathVariable Integer userId) {
        try {
            // Note: This would need the student service to get the student object
            // For now, we'll return a placeholder response
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findByCourseId/{courseId}")
    public ResponseEntity<List<Enrollment>> findByCourseId(@PathVariable Integer courseId) {
        try {
            // Note: This would need the course service to get the course object
            // For now, we'll return a placeholder response
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
