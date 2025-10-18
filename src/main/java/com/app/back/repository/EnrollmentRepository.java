package com.app.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.back.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
	
	public Optional<Enrollment> findByCode(String code);

}
