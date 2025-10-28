package com.app.back.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class LoginResponse {
    
    private boolean success;
    private String message;
    private UserInfo user;
    private String token; // For future JWT implementation

    // Inner class for user information
    public static class UserInfo {
        private Integer userId;
        private String name;
        private String email;
        private Integer tenantId;
        private boolean isActive;
        private Set<String> roles;
        private String userType; // "STUDENT" or "INSTRUCTOR"
        private LocalDateTime lastLogin;

        // Constructors, getters, and setters
        public UserInfo() {}

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getTenantId() { return tenantId; }
        public void setTenantId(Integer tenantId) { this.tenantId = tenantId; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }

        public Set<String> getRoles() { return roles; }
        public void setRoles(Set<String> roles) { this.roles = roles; }

        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }

        public LocalDateTime getLastLogin() { return lastLogin; }
        public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    }

    // Constructors
    public LoginResponse() {}

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}