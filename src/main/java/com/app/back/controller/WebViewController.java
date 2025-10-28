package com.app.back.controller;

import com.app.back.util.SessionManager;
import com.app.back.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class WebViewController {

    @Autowired
    private SessionManager sessionManager;

    private String getPrimaryRole(Set<String> roles) {
        System.out.println("DEBUG: Received roles: " + roles);
        if (roles == null || roles.isEmpty()) {
            System.out.println("DEBUG: No roles found, defaulting to STUDENT");
            return "STUDENT";
        }
        if (roles.contains("ADMIN")) {
            System.out.println("DEBUG: Found ADMIN role");
            return "ADMIN";
        }
        if (roles.contains("INSTRUCTOR")) {
            System.out.println("DEBUG: Found INSTRUCTOR role");
            return "INSTRUCTOR";
        }
        if (roles.contains("STUDENT")) {
            System.out.println("DEBUG: Found STUDENT role");
            return "STUDENT";
        }
        String firstRole = roles.iterator().next();
        System.out.println("DEBUG: Using first role: " + firstRole);
        return firstRole;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        if (sessionManager.isUserAuthenticated(session)) {
            LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
            model.addAttribute("loggedIn", true);
            model.addAttribute("userName", userInfo.getName());
            model.addAttribute("userRole", getPrimaryRole(userInfo.getRoles()));
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/courses")
    public String courses(HttpSession session, Model model) {
        if (!sessionManager.isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("userRole", getPrimaryRole(userInfo.getRoles()));
        return "courses";
    }

    @GetMapping("/students")
    public String students(HttpSession session, Model model) {
        if (!sessionManager.isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        String userRole = getPrimaryRole(userInfo.getRoles());
        if (!"ADMIN".equals(userRole) && !"INSTRUCTOR".equals(userRole)) {
            return "redirect:/"; // Students can't access this page
        }
        
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("userRole", userRole);
        return "students";
    }

    @GetMapping("/instructors")
    public String instructors(HttpSession session, Model model) {
        if (!sessionManager.isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        String userRole = getPrimaryRole(userInfo.getRoles());
        if (!"ADMIN".equals(userRole)) {
            return "redirect:/"; // Only admins can access this page
        }
        
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("userRole", userRole);
        return "instructors";
    }

    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        if (!sessionManager.isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        String userRole = getPrimaryRole(userInfo.getRoles());
        if (!"ADMIN".equals(userRole)) {
            return "redirect:/"; // Only admins can access this page
        }
        
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("userRole", userRole);
        return "users";
    }

    @GetMapping("/enrollments")
    public String enrollments(HttpSession session, Model model) {
        if (!sessionManager.isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        
        LoginResponse.UserInfo userInfo = sessionManager.getCurrentUserInfo(session);
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("userRole", getPrimaryRole(userInfo.getRoles()));
        return "enrollments";
    }
}