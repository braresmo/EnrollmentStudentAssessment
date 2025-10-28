package com.app.back.core.impl;

import com.app.back.core.IAuthenticationService;
import com.app.back.dto.LoginRequest;
import com.app.back.dto.LoginResponse;
import com.app.back.model.Role;
import com.app.back.model.Student;
import com.app.back.model.Instructor;
import com.app.back.model.User;
import com.app.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final int MAX_LOGIN_ATTEMPTS = 3;
    private final int LOCK_DURATION_MINUTES = 15;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            System.out.println("=== DEBUG LOGIN ATTEMPT ===");
            System.out.println("Email: " + loginRequest.getEmail());
            System.out.println("Password: " + loginRequest.getPassword());
            
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            
            if (!userOpt.isPresent()) {
                System.out.println("User not found for email: " + loginRequest.getEmail());
                return new LoginResponse(false, "Invalid email or password");
            }

            User user = userOpt.get();
            System.out.println("User found: " + user.getName() + " (ID: " + user.getUserId() + ")");
            System.out.println("Stored password hash: " + user.getPasswordHash());

            // Check if account is locked
            if (user.isAccountLocked()) {
                System.out.println("Account is locked");
                return new LoginResponse(false, "Account is temporarily locked. Please try again later.");
            }

            // Check if account is active
            if (!user.isActive()) {
                System.out.println("Account is not active");
                return new LoginResponse(false, "Account is deactivated. Please contact administrator.");
            }

            // Verify password using BCrypt
            boolean passwordMatch = verifyPassword(loginRequest.getPassword(), user.getPasswordHash());
            System.out.println("Password verification result: " + passwordMatch);
            
            if (!passwordMatch) {
                handleFailedLogin(user);
                return new LoginResponse(false, "Invalid email or password");
            }

            // Check tenant (if provided)
            if (loginRequest.getTenantId() != null && !user.getTenantId().equals(loginRequest.getTenantId())) {
                System.out.println("Tenant mismatch");
                return new LoginResponse(false, "Invalid tenant access");
            }

            // Successful login
            handleSuccessfulLogin(user);

            // Create response
            LoginResponse response = new LoginResponse(true, "Login successful");
            response.setUser(createUserInfo(user));
            
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "Login failed: " + e.getMessage());
        }
    }

    @Override
    public LoginResponse logout(Integer userId) {
        try {
            // You can implement logout logic here (e.g., invalidate tokens)
            return new LoginResponse(true, "Logout successful");
        } catch (Exception e) {
            return new LoginResponse(false, "Logout failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                return false;
            }

            User user = userOpt.get();

            // Verify current password
            if (!verifyPassword(currentPassword, user.getPasswordHash())) {
                return false;
            }

            // Update password
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean resetPassword(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (!userOpt.isPresent()) {
                return false;
            }

            User user = userOpt.get();
            
            // Generate temporary password (in production, send email)
            String tempPassword = "TempPass123!";
            user.setPasswordHash(passwordEncoder.encode(tempPassword));
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
            
            userRepository.save(user);
            
            // In production, send email with temporary password
            System.out.println("Temporary password for " + email + ": " + tempPassword);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void unlockAccount(Integer userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setFailedLoginAttempts(0);
                user.setAccountLockedUntil(null);
                userRepository.save(user);
            }
        } catch (Exception e) {
            // Log error
        }
    }

    // BCrypt password verification
    private boolean verifyPassword(String plainPassword, String storedPassword) {
        System.out.println("=== PASSWORD VERIFICATION ===");
        System.out.println("Plain password: '" + plainPassword + "'");
        System.out.println("Stored password: '" + storedPassword + "'");
        
        // For demo purposes, support both plain text and BCrypt passwords
        if (plainPassword.equals(storedPassword)) {
            System.out.println("Plain text match: TRUE");
            return true; // Plain text match for existing demo data
        }
        
        // Try BCrypt comparison for properly hashed passwords
        try {
            boolean matches = passwordEncoder.matches(plainPassword, storedPassword);
            System.out.println("BCrypt match: " + matches);
            return matches;
        } catch (Exception e) {
            System.out.println("BCrypt error: " + e.getMessage());
            return false; // Invalid hash format
        }
    }

    private void handleFailedLogin(User user) {
        // Handle null value for existing users
        Integer currentAttempts = user.getFailedLoginAttempts();
        int attempts = (currentAttempts != null ? currentAttempts : 0) + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
        }

        userRepository.save(user);
    }

    private void handleSuccessfulLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        userRepository.save(user);
    }

    private LoginResponse.UserInfo createUserInfo(User user) {
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        userInfo.setTenantId(user.getTenantId());
        userInfo.setActive(user.isActive());
        userInfo.setLastLogin(user.getLastLogin());
        
        // Set roles
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userInfo.setRoles(roleNames);
        
        // Determine user type
        if (user instanceof Student) {
            userInfo.setUserType("STUDENT");
        } else if (user instanceof Instructor) {
            userInfo.setUserType("INSTRUCTOR");
        } else {
            userInfo.setUserType("USER");
        }
        
        return userInfo;
    }
}