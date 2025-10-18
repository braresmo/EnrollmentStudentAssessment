package com.app.back.core.impl;

import com.app.back.core.IModule;
import com.app.back.model.Module;
import com.app.back.model.Course;
import com.app.back.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService implements IModule {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public Module save(Module module) throws InternalServerError, Exception {
        // For modules, we typically allow multiple modules with the same title in different courses
        // So we just need to validate basic constraints

        // Case 1: Create a new module
        if (module.getModuleId() == 0) { // moduleId is int, 0 means new
            return moduleRepository.save(module);
        }

        // Case 2: Update existing module
        if (module.getModuleId() > 0) {
            Optional<Module> existingModule = findById(module.getModuleId());
            if (existingModule.isPresent()) {
                return moduleRepository.save(module);
            }
        }
        
        throw new Exception("Error saving module: Module ID not found for update.");
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
}
