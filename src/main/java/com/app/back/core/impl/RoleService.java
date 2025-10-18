package com.app.back.core.impl;

import com.app.back.core.IRole;
import com.app.back.model.Role;
import com.app.back.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRole {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role save(Role role) throws InternalServerError, Exception {
        Optional<Role> roleByName = findByName(role.getName());

        // Case 1: Create a new role
        // The role ID is null and no role exists with this name.
        if (role.getRoleId() == null && roleByName.isEmpty()) {
            return roleRepository.save(role);
        }

        // Case 2: Update an existing role
        if (roleByName.isPresent()) {
            // We can only update if the ID of the role being saved matches the ID of the role found by name.
            if (role.getRoleId() != null && role.getRoleId().equals(roleByName.get().getRoleId())) {
                return roleRepository.save(role);
            }
        }
        
        throw new Exception("Error saving role: Role name may already exist or there is an ID mismatch.");
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
