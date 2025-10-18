package com.app.back.core;

import com.app.back.model.Enrollment;
import com.app.back.model.Student;
import com.app.back.model.Course;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IEnrollment {

    public Enrollment save(Enrollment enrollment) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<Enrollment> findAll();

    public Optional<Enrollment> findById(Integer id);

    public List<Enrollment> findByStudent(Student student);
    
    public List<Enrollment> findByCourse(Course course);
    
    public List<Enrollment> findByStatus(String status);
    
    public Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
