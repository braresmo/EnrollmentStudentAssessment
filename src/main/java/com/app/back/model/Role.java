package com.app.back.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private UUID roleId;

    @Column(unique = true, nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();

    // helpers
    public void addPermission(Permission p){ permissions.add(p); }
    public void removePermission(Permission p){ permissions.remove(p); }

    // getters/setters
    public UUID getRoleId() { return roleId; }
    public void setRoleId(UUID roleId) { this.roleId = roleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
}
