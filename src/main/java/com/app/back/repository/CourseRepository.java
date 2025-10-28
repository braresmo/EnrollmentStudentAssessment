package com.app.back.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.back.model.Course;
import com.app.back.model.Instructor;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	public Optional<Course> findByCode(String code);
	
	// Filter by status
	List<Course> findByStatus(String status);
	
	// Filter by instructor
	List<Course> findByInstructor(Instructor instructor);
	
	// Filter by instructor ID (more convenient)
	@Query("SELECT c FROM Course c WHERE c.instructor.userId = :instructorId")
	List<Course> findByInstructorId(@Param("instructorId") Integer instructorId);
	
	// Count courses by instructor ID
	@Query("SELECT COUNT(c) FROM Course c WHERE c.instructor.userId = :instructorId")
	Long countByInstructorId(@Param("instructorId") Integer instructorId);
	
	// Filter by code containing (case insensitive)
	@Query("SELECT c FROM Course c WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :code, '%'))")
	List<Course> findByCodeContainingIgnoreCase(@Param("code") String code);
	
	// Filter by title containing (case insensitive)
	@Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
	List<Course> findByTitleContainingIgnoreCase(@Param("title") String title);
	
	// Filter by published date range
	@Query("SELECT c FROM Course c WHERE c.publishedAt BETWEEN :startDate AND :endDate")
	List<Course> findByPublishedAtBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	// Combined filter method with optional parameters
	@Query("SELECT c FROM Course c WHERE " +
	       "(:status IS NULL OR c.status = :status) AND " +
	       "(:instructorId IS NULL OR c.instructor.userId = :instructorId) AND " +
	       "(:code IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
	       "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))")
	List<Course> findCoursesWithFilters(@Param("status") String status,
	                                   @Param("instructorId") Integer instructorId,
	                                   @Param("code") String code,
	                                   @Param("title") String title);

}
