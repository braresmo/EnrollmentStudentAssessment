package com.app.back.core;

import com.app.back.dto.LoginRequest;
import com.app.back.dto.LoginResponse;

public interface IAuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse logout(Integer userId);
    boolean changePassword(Integer userId, String currentPassword, String newPassword);
    boolean resetPassword(String email);
    void unlockAccount(Integer userId);
}