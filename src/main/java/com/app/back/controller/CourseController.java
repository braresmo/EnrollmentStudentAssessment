package com.app.back.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.app.back.core.impl.CourseService;
import com.app.back.core.impl.InstructorService;
import com.app.back.dto.CourseCreateDTO;
import com.app.back.dto.CourseUpdateDTO;
import com.app.back.model.Course;
import com.app.back.model.Instructor;
import com.app.back.util.SessionManager;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping(path = "/course")
public class CourseController {


	@Autowired
	private CourseService courseService;
	
	@Autowired
	private InstructorService instructorService;
	
	
	@GetMapping(path = "/getAll")
	public ResponseEntity<List<Course>> listar() {
		try {

			return ResponseEntity.ok(courseService.findAll());

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(path = "/getById/{id}")
	public ResponseEntity<Course> getById(@PathVariable Integer id) {
		try {
			Optional<Course> course = courseService.findById(id);
			if (course.isPresent()) {
				return ResponseEntity.ok(course.get());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Course> save(@RequestBody CourseCreateDTO courseDTO) {
		try {
			// Create Course entity from DTO
			Course course = new Course();
			course.setCode(courseDTO.getCode());
			course.setTitle(courseDTO.getTitle());
			course.setStatus(courseDTO.getStatus());
			course.setPublishedAt(courseDTO.getPublishedAt());
			course.setIdTenant(courseDTO.getTenantId());
			
			// Find and set the instructor
			Optional<Instructor> instructor = instructorService.findById(courseDTO.getInstructorId());
			if (instructor.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			course.setInstructor(instructor.get());
			
			return ResponseEntity.ok(courseService.save(course));
		} catch (InternalServerError e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<List<Course>> delete(@PathVariable Integer id) {
		try {
			Optional<Course> course = courseService.findById(id);
			if (!course.isEmpty()) {
				courseService.delete(id);
				return ResponseEntity.ok(courseService.findAll());
			} else {
				return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
			}

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Course> update(@RequestBody CourseUpdateDTO courseDTO) {
		try {
			// Find existing course
			Optional<Course> existingCourse = courseService.findById(courseDTO.getIdCourse());
			if (existingCourse.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			// Update course fields
			Course course = existingCourse.get();
			course.setCode(courseDTO.getCode());
			course.setTitle(courseDTO.getTitle());
			course.setStatus(courseDTO.getStatus());
			course.setPublishedAt(courseDTO.getPublishedAt());
			course.setIdTenant(courseDTO.getTenantId());
			
			// Update instructor if provided and different
			if (courseDTO.getInstructorId() != null) {
				Optional<Instructor> instructor = instructorService.findById(courseDTO.getInstructorId());
				if (instructor.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				course.setInstructor(instructor.get());
			}
			
			return ResponseEntity.ok(courseService.save(course));

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// Filtering endpoints
	
	@GetMapping(path = "/findByStatus/{status}")
	public ResponseEntity<List<Course>> findByStatus(@PathVariable String status) {
		try {
			List<Course> courses = courseService.findByStatus(status);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findByInstructorId/{instructorId}")
	public ResponseEntity<List<Course>> findByInstructorId(@PathVariable Integer instructorId) {
		try {
			List<Course> courses = courseService.findByInstructorId(instructorId);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findByCodeContaining")
	public ResponseEntity<List<Course>> findByCodeContaining(@RequestParam String code) {
		try {
			List<Course> courses = courseService.findByCodeContaining(code);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findByTitleContaining")
	public ResponseEntity<List<Course>> findByTitleContaining(@RequestParam String title) {
		try {
			List<Course> courses = courseService.findByTitleContaining(title);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findByDateRange")
	public ResponseEntity<List<Course>> findByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
			List<Course> courses = courseService.findByPublishedDateRange(start, end);
			return ResponseEntity.ok(courses);
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findWithFilters")
	public ResponseEntity<List<Course>> findCoursesWithFilters(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer instructorId,
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String title) {
		try {
			List<Course> courses = courseService.findCoursesWithFilters(status, instructorId, code, title);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/my-courses")
	public ResponseEntity<List<Course>> getMyCourses(HttpSession session) {
		try {
			SessionManager sessionManager = new SessionManager();
			Integer userId = sessionManager.getCurrentUserId(session);
			
			if (userId == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			// For instructors, filter by instructor ID which is the same as user ID
			List<Course> courses = courseService.findCoursesWithFilters(null, userId, null, null);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
