package com.app.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.back.model.Enrollment;
import com.app.back.model.Student;
import com.app.back.model.Course;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
	
	List<Enrollment> findByStudent(Student student);
	
	List<Enrollment> findByCourse(Course course);
	
	List<Enrollment> findByStatus(String status);
	
	Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

}
