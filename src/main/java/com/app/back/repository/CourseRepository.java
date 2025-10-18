package com.app.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.back.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	public Optional<Course> findByCode(String code);

}
