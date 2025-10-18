package com.app.back.core.impl;

import com.app.back.core.IInstructor;
import com.app.back.model.Instructor;
import com.app.back.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService implements IInstructor {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public Instructor save(Instructor instructor) throws InternalServerError, Exception {
        Optional<Instructor> instructorByEmployeeNumber = findByEmployeeNumber(instructor.getEmployeeNumber());
        Optional<Instructor> instructorByEmail = findByEmail(instructor.getEmail());

        // Case 1: Create a new instructor
        // The instructor ID is null and no instructor exists with this employee number or email.
        if (instructor.getUserId() == null && instructorByEmployeeNumber.isEmpty() && instructorByEmail.isEmpty()) {
            return instructorRepository.save(instructor);
        }

        // Case 2: Update an existing instructor
        if (instructor.getUserId() != null) {
            // Check if employee number belongs to this instructor or doesn't exist
            boolean employeeNumberOk = instructorByEmployeeNumber.isEmpty() || 
                                      instructorByEmployeeNumber.get().getUserId().equals(instructor.getUserId());
            // Check if email belongs to this instructor or doesn't exist
            boolean emailOk = instructorByEmail.isEmpty() || 
                             instructorByEmail.get().getUserId().equals(instructor.getUserId());
            
            if (employeeNumberOk && emailOk) {
                return instructorRepository.save(instructor);
            }
        }
        
        throw new Exception("Error saving instructor: Employee number or email may already exist or there is an ID mismatch.");
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        instructorRepository.deleteById(id);
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
