package com.app.back.core.impl;

import com.app.back.core.IUser;
import com.app.back.model.User;
import com.app.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUser {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) throws InternalServerError, Exception {
        Optional<User> userByEmail = findByEmail(user.getEmail());

        // Case 1: Create a new user
        // The user ID is null and no user exists with this email.
        if (user.getUserId() == null && userByEmail.isEmpty()) {
            return userRepository.save(user);
        }

        // Case 2: Update an existing user
        // A user with this email already exists.
        if (userByEmail.isPresent()) {
            // We can only update if the ID of the user being saved matches the ID of the user found by email.
            // This prevents changing a user's email to one that is already taken by another user.
            if (user.getUserId() != null && user.getUserId().equals(userByEmail.get().getUserId())) {
                return userRepository.save(user);
            }
        }
        
        // If none of the above conditions are met, it's an error.
        throw new Exception("Error saving user: Email may already exist or there is an ID mismatch.");
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
