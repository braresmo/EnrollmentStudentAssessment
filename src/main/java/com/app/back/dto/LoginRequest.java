package com.app.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
    
    private Integer tenantId;

    // Constructors
    public LoginRequest() {}

    public LoginRequest(String email, String password, Integer tenantId) {
        this.email = email;
        this.password = password;
        this.tenantId = tenantId;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}