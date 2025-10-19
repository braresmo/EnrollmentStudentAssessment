package com.app.back.core.impl;

import com.app.back.core.IModule;
import com.app.back.model.Module;
import com.app.back.model.Course;
import com.app.back.repository.ModuleRepository;
import com.app.back.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService implements IModule {

    @Autowired
    private ModuleRepository moduleRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Module save(Module module) throws InternalServerError, Exception {
        // Ensure the course relationship is properly resolved
        if (module.getCourse() != null && module.getCourse().getIdCourse() != null && module.getCourse().getIdCourse() != 0) {
            Optional<Course> course = courseRepository.findById(module.getCourse().getIdCourse());
            if (course.isPresent()) {
                module.setCourse(course.get());
            } else {
                throw new Exception("Course not found with ID: " + module.getCourse().getIdCourse());
            }
        }

        // Case 1: Create a new module
        if (module.getModuleId() == 0) { // moduleId is int, 0 means new
            return moduleRepository.save(module);
        }

        // Case 2: Update existing module
        if (module.getModuleId() > 0) {
            Optional<Module> existingModule = findById(module.getModuleId());
            if (existingModule.isPresent()) {
                // For updates, preserve the course from existing module if not provided
                if (module.getCourse() == null) {
                    module.setCourse(existingModule.get().getCourse());
                }
                return moduleRepository.save(module);
            }
        }
        
        throw new Exception("Error saving module: Module ID not found for update.");
    }
    
    public Module saveWithCourseId(Module module, Integer courseId) throws InternalServerError, Exception {
        // Find and set the course
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            module.setCourse(course.get());
            return save(module);
        } else {
            throw new Exception("Course not found with ID: " + courseId);
        }
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        moduleRepository.deleteById(id);
    }

    @Override
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    @Override
    public Optional<Module> findById(Integer id) {
        return moduleRepository.findById(id);
    }

    @Override
    public List<Module> findByCourse(Course course) {
        return moduleRepository.findByCourse(course);
    }

    @Override
    public List<Module> findByTitle(String title) {
        return moduleRepository.findByTitle(title);
    }
    
    public List<Module> findByCourseId(Integer courseId) {
        return moduleRepository.findByCourseIdWithContentItems(courseId);
    }
}
