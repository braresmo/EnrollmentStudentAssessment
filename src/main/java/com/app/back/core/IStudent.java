package com.app.back.core;

import com.app.back.model.Student;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IStudent {

    public Student save(Student student) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<Student> findAll();

    public Optional<Student> findById(Integer id);

    public Optional<Student> findByEmail(String email);
    
    public Optional<Student> findByStudentNumber(String studentNumber);
    
    public List<Student> findByCohort(String cohort);
}
