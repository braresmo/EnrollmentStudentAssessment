package com.app.back.util;

import com.app.back.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SessionManager {

    public void createUserSession(HttpSession session, LoginResponse.UserInfo userInfo) {
        session.setAttribute("userId", userInfo.getUserId());
        session.setAttribute("userName", userInfo.getName());
        session.setAttribute("userEmail", userInfo.getEmail());
        session.setAttribute("userType", userInfo.getUserType());
        session.setAttribute("userRoles", userInfo.getRoles());
        session.setAttribute("tenantId", userInfo.getTenantId());
        session.setAttribute("isAuthenticated", true);
    }

    public boolean isUserAuthenticated(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        return isAuthenticated != null && isAuthenticated;
    }

    public Integer getCurrentUserId(HttpSession session) {
        return (Integer) session.getAttribute("userId");
    }

    public String getCurrentUserType(HttpSession session) {
        return (String) session.getAttribute("userType");
    }

    @SuppressWarnings("unchecked")
    public Set<String> getCurrentUserRoles(HttpSession session) {
        return (Set<String>) session.getAttribute("userRoles");
    }

    public void clearUserSession(HttpSession session) {
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.removeAttribute("userType");
        session.removeAttribute("userRoles");
        session.removeAttribute("tenantId");
        session.removeAttribute("isAuthenticated");
    }

    public LoginResponse.UserInfo getCurrentUserInfo(HttpSession session) {
        if (!isUserAuthenticated(session)) {
            return null;
        }

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId((Integer) session.getAttribute("userId"));
        userInfo.setName((String) session.getAttribute("userName"));
        userInfo.setEmail((String) session.getAttribute("userEmail"));
        userInfo.setUserType((String) session.getAttribute("userType"));
        userInfo.setRoles(getCurrentUserRoles(session));
        userInfo.setTenantId((Integer) session.getAttribute("tenantId"));
        userInfo.setActive(true);

        return userInfo;
    }
}