package com.app.back.controller;

import com.app.back.core.impl.StudentService;
import com.app.back.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Student>> listar() {
        try {
            return ResponseEntity.ok(studentService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> save(@RequestBody Student student) {
        try {
            return ResponseEntity.ok(studentService.save(student));
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<Student>> delete(@PathVariable Integer id) {
        try {
            Optional<Student> student = studentService.findById(id);
            if (student.isPresent()) {
                studentService.delete(id);
                return ResponseEntity.ok(studentService.findAll());
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
    public ResponseEntity<Student> update(@RequestBody Student student) {
        try {
            return ResponseEntity.ok(studentService.save(student));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findByStudentNumber/{studentNumber}")
    public ResponseEntity<Student> findByStudentNumber(@PathVariable String studentNumber) {
        try {
            Optional<Student> student = studentService.findByStudentNumber(studentNumber);
            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findByCohort/{cohort}")
    public ResponseEntity<List<Student>> findByCohort(@PathVariable String cohort) {
        try {
            return ResponseEntity.ok(studentService.findByCohort(cohort));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
