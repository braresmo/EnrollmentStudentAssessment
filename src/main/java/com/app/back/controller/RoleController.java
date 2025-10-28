package com.app.back.controller;

import com.app.back.core.impl.RoleService;
import com.app.back.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Role>> listar() {
        try {
            return ResponseEntity.ok(roleService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> save(@RequestBody Role role) {
        try {
            return ResponseEntity.ok(roleService.save(role));
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<Role>> delete(@PathVariable Integer id) {
        try {
            Optional<Role> role = roleService.findById(id);
            if (role.isPresent()) {
                roleService.delete(id);
                return ResponseEntity.ok(roleService.findAll());
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> update(@RequestBody Role role) {
        try {
            return ResponseEntity.ok(roleService.save(role));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name) {
        try {
            Optional<Role> role = roleService.findByName(name);
            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
