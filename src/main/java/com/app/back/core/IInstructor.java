package com.app.back.core;

import com.app.back.model.Instructor;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IInstructor {

    public Instructor save(Instructor instructor) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<Instructor> findAll();

    public Optional<Instructor> findById(Integer id);

    public Optional<Instructor> findByEmail(String email);
    
    public Optional<Instructor> findByEmployeeNumber(String employeeNumber);
}
