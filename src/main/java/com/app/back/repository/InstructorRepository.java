package com.app.back.repository;

import com.app.back.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    Optional<Instructor> findByEmployeeNumber(String employeeNumber);
    
    Optional<Instructor> findByEmail(String email);
    
}
