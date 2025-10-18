package com.app.back.repository;

import com.app.back.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByStudentNumber(String studentNumber);
    
    List<Student> findByCohort(String cohort);
    
    Optional<Student> findByEmail(String email);
    
}
