package com.simsekolah.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Security interceptor for assessment-related endpoints
 * Provides fine-grained access control for assessment operations
 */
@Component
public class AssessmentSecurityInterceptor implements HandlerInterceptor {

    private final SecurityService securityService;

    public AssessmentSecurityInterceptor(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Only handle assessment-related endpoints
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api/assessments")) {
            return true;
        }

        // Allow GET requests for teachers and admins
        if (request.getMethod().equals("GET") && securityService.canManageAssessments()) {
            return true;
        }

        // Allow POST, PUT, DELETE only for admins
        if ((request.getMethod().equals("POST") || request.getMethod().equals("PUT") || request.getMethod().equals("DELETE")) 
            && securityService.isAdmin()) {
            return true;
        }

        // Deny access
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"error\": \"Access denied\"}");
        return false;
    }
}