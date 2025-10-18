package com.app.back.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.app.back.core.impl.CourseService;
import com.app.back.model.Course;



@RestController
@RequestMapping(path = "/course")
public class CourseController {


	@Autowired
	private CourseService courseService;
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/getAll")
	public ResponseEntity<List<Course>> listar() {
		try {

			return ResponseEntity.ok(courseService.findAll());

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@CrossOrigin(origins = "*")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Course> save(@RequestBody Course course) {
		try {
			
			return ResponseEntity.ok(courseService.save(course));
		} catch (InternalServerError e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins = "*")
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
	
	@CrossOrigin(origins = "*")
	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Course> update(@RequestBody Course course) {
		try {
			//course.setFechaModificacion(new Date());
			return ResponseEntity.ok(courseService.save(course));

		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
