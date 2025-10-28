package com.app.back.controller;

import com.app.back.core.IAuthenticationService;
import com.app.back.dto.LoginRequest;
import com.app.back.dto.LoginResponse;
import com.app.back.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        LoginResponse response = authenticationService.login(loginRequest);
        
        if (response.isSuccess()) {
            // Store user info in session using SessionManager
            sessionManager.createUserSession(session, response.getUser());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        Integer userId = sessionManager.getCurrentUserId(session);
        
        if (userId != null) {
            LoginResponse response = authenticationService.logout(userId);
            sessionManager.clearUserSession(session);
            session.invalidate(); // Clear session
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok(new LoginResponse(true, "Already logged out"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<LoginResponse> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            HttpSession session) {
        
        Integer userId = sessionManager.getCurrentUserId(session);
        
        if (userId == null) {
            return ResponseEntity.ok(new LoginResponse(false, "User not logged in"));
        }
        
        boolean success = authenticationService.changePassword(userId, currentPassword, newPassword);
        
        if (success) {
            return ResponseEntity.ok(new LoginResponse(true, "Password changed successfully"));
        } else {
            return ResponseEntity.ok(new LoginResponse(false, "Failed to change password"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<LoginResponse> resetPassword(@RequestParam String email) {
        boolean success = authenticationService.resetPassword(email);
        
        if (success) {
            return ResponseEntity.ok(new LoginResponse(true, "Password reset instructions sent"));
        } else {
            return ResponseEntity.ok(new LoginResponse(false, "Email not found"));
        }
    }

    @GetMapping("/session")
    public ResponseEntity<LoginResponse> getSession(HttpSession session) {
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        
        if (userInfo != null) {
            LoginResponse response = new LoginResponse(true, "Session active");
            response.setUser(userInfo);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok(new LoginResponse(false, "No active session"));
    }

    @GetMapping("/user-info")
    public ResponseEntity<LoginResponse> getUserInfo(HttpSession session) {
        if (sessionManager.isUserAuthenticated(session)) {
            LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
            LoginResponse response = new LoginResponse(true, "User info retrieved");
            response.setUser(userInfo);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok(new LoginResponse(false, "No active session"));
    }

    @PostMapping("/unlock-account/{userId}")
    public ResponseEntity<LoginResponse> unlockAccount(@PathVariable Integer userId) {
        authenticationService.unlockAccount(userId);
        return ResponseEntity.ok(new LoginResponse(true, "Account unlocked successfully"));
    }
}
