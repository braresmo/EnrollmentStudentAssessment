package com.app.back.core;

import com.app.back.model.User;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IUser {

    public User save(User user) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<User> findAll();

    public Optional<User> findById(Integer id);

    public Optional<User> findByEmail(String email);
}

