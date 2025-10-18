package com.app.back.core;

import com.app.back.model.Module;
import com.app.back.model.Course;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IModule {

    public Module save(Module module) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<Module> findAll();

    public Optional<Module> findById(Integer id);

    public List<Module> findByCourse(Course course);
    
    public List<Module> findByTitle(String title);
}
