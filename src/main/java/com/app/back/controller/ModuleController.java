package com.app.back.controller;

import com.app.back.core.impl.ModuleService;
import com.app.back.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Module>> listar() {
        try {
            return ResponseEntity.ok(moduleService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Module> save(@RequestBody Module module) {
        try {
            return ResponseEntity.ok(moduleService.save(module));
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<Module>> delete(@PathVariable Integer id) {
        try {
            Optional<Module> module = moduleService.findById(id);
            if (module.isPresent()) {
                moduleService.delete(id);
                return ResponseEntity.ok(moduleService.findAll());
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Module> update(@RequestBody Module module) {
        try {
            return ResponseEntity.ok(moduleService.save(module));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/findByTitle/{title}")
    public ResponseEntity<List<Module>> findByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok(moduleService.findByTitle(title));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findByCourseId/{courseId}")
    public ResponseEntity<List<Module>> findByCourseId(@PathVariable Integer courseId) {
        try {
            List<Module> modules = moduleService.findByCourseId(courseId);
            return ResponseEntity.ok(modules);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getByCourse/{courseId}")
    public ResponseEntity<List<Module>> getByCourse(@PathVariable Integer courseId) {
        try {
            List<Module> modules = moduleService.findByCourseId(courseId);
            return ResponseEntity.ok(modules);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<Module> getById(@PathVariable Integer id) {
        try {
            Optional<Module> module = moduleService.findById(id);
            if (module.isPresent()) {
                return ResponseEntity.ok(module.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/updateWithCourseId/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Module> updateWithCourseId(@RequestBody Module module, @PathVariable Integer courseId) {
        try {
            return ResponseEntity.ok(moduleService.saveWithCourseId(module, courseId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
