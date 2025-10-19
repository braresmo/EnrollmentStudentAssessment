package com.app.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.back.model.Module;
import com.app.back.model.Course;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
	
	List<Module> findByCourse(Course course);
	
	List<Module> findByTitle(String title);
	
	@Query("SELECT m FROM Module m LEFT JOIN FETCH m.contentItems WHERE m.course.idCourse = :courseId ORDER BY m.sequence")
	List<Module> findByCourseIdWithContentItems(@Param("courseId") Integer courseId);

}
