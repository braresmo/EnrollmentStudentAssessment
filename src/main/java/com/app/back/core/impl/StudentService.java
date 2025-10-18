package com.app.back.core.impl;

import com.app.back.core.IStudent;
import com.app.back.model.Student;
import com.app.back.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudent {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student save(Student student) throws InternalServerError, Exception {
        Optional<Student> studentByNumber = findByStudentNumber(student.getStudentNumber());
        Optional<Student> studentByEmail = findByEmail(student.getEmail());

        // Case 1: Create a new student
        // The student ID is null and no student exists with this number or email.
        if (student.getUserId() == null && studentByNumber.isEmpty() && studentByEmail.isEmpty()) {
            return studentRepository.save(student);
        }

        // Case 2: Update an existing student
        if (student.getUserId() != null) {
            // Check if student number belongs to this student or doesn't exist
            boolean numberOk = studentByNumber.isEmpty() || 
                              studentByNumber.get().getUserId().equals(student.getUserId());
            // Check if email belongs to this student or doesn't exist
            boolean emailOk = studentByEmail.isEmpty() || 
                             studentByEmail.get().getUserId().equals(student.getUserId());
            
            if (numberOk && emailOk) {
                return studentRepository.save(student);
            }
        }
        
        throw new Exception("Error saving student: Student number or email may already exist or there is an ID mismatch.");
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
