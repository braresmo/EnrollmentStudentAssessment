package com.app.back.core.impl;

import com.app.back.core.IEnrollment;
import com.app.back.model.Enrollment;
import com.app.back.model.Student;
import com.app.back.model.Course;
import com.app.back.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService implements IEnrollment {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment save(Enrollment enrollment) throws InternalServerError, Exception {
        Optional<Enrollment> existingEnrollment = findByStudentAndCourse(enrollment.getStudent(), enrollment.getCourse());

        // Case 1: Create a new enrollment
        // The enrollment ID is null and no enrollment exists for this student-course combination.
        if (enrollment.getEnrollmentId() == null && existingEnrollment.isEmpty()) {
            return enrollmentRepository.save(enrollment);
        }

        // Case 2: Update an existing enrollment
        if (existingEnrollment.isPresent()) {
            // We can only update if the ID of the enrollment being saved matches the existing enrollment.
            if (enrollment.getEnrollmentId() != null && 
                enrollment.getEnrollmentId().equals(existingEnrollment.get().getEnrollmentId())) {
                return enrollmentRepository.save(enrollment);
            }
        }
        
        throw new Exception("Error saving enrollment: Student may already be enrolled in this course or there is an ID mismatch.");
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        enrollmentRepository.deleteById(id);
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> findById(Integer id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public List<Enrollment> findByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    @Override
    public List<Enrollment> findByCourse(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    @Override
    public List<Enrollment> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(Student student, Course course) {
        return enrollmentRepository.findByStudentAndCourse(student, course);
    }
}
