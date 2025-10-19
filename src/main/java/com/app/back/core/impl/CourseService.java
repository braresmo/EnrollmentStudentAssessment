package com.app.back.core.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.app.back.core.ICourse;
import com.app.back.model.Course;
import com.app.back.repository.CourseRepository;

@Service
public class CourseService implements ICourse{
	
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public Course save(Course course) throws InternalServerError, Exception {
		Optional<Course> courseByCodigo = findByCodigo(course.getCode());
		
		//save
		if(course.getIdCourse() == null && courseByCodigo.isEmpty()) {
			return courseRepository.save(course);
		}
		
		if(!courseByCodigo.isEmpty()) {
			
			//update
			if(course.getIdCourse().equals(courseByCodigo.get().getIdCourse()))
					 {
				return courseRepository.save(course);
			}
		}
		
		throw new Exception("Error save CourseRepository");
		
	}

	@Override
	public void delete(Integer id) throws InternalError {
		
		courseRepository.deleteById(id);
	}

	@Override
	public List<Course> findAll() {
		
		return courseRepository.findAll();
	}

	@Override
	public Optional<Course> findById(Integer id) {
		
		return courseRepository.findById(id);
	}

	@Override
	public Optional<Course> findByCodigo(String code) {
		
		return courseRepository.findByCode(code);
	}
	
	// Additional filtering methods
	public List<Course> findByStatus(String status) {
		return courseRepository.findByStatus(status);
	}
	
	public List<Course> findByInstructorId(Integer instructorId) {
		return courseRepository.findByInstructorId(instructorId);
	}
	
	public List<Course> findByCodeContaining(String code) {
		return courseRepository.findByCodeContainingIgnoreCase(code);
	}
	
	public List<Course> findByTitleContaining(String title) {
		return courseRepository.findByTitleContainingIgnoreCase(title);
	}
	
	public List<Course> findByPublishedDateRange(Date startDate, Date endDate) {
		return courseRepository.findByPublishedAtBetween(startDate, endDate);
	}
	
	public List<Course> findCoursesWithFilters(String status, Integer instructorId, String code, String title) {
		return courseRepository.findCoursesWithFilters(status, instructorId, code, title);
	}
}
