package com.app.back.core;

import java.util.List;
import java.util.Optional;

import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.app.back.model.Course;

public interface ICourse {
	
	public Course save (Course course) throws InternalServerError, Exception;
	
	public void delete(Integer id) throws InternalServerError;
	
	public List<Course> findAll();
	
	public Optional<Course> findById(Integer id);
	
	public Optional<Course> findByCodigo(String codigo);
}
