package com.app.back.core;

import com.app.back.model.Role;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IRole {

    public Role save(Role role) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<Role> findAll();

    public Optional<Role> findById(Integer id);

    public Optional<Role> findByName(String name);
}
