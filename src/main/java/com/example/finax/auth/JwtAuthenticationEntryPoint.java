package com.example.finax.auth;

import com.example.finax.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.List;

/**
 * JwtAuthenticationEntryPoint is responsible for handling unauthorized access
 * attempts.
 * It is triggered whenever an unauthenticated user tries to access a secured
 * resource.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles unauthorized access attempts by returning a 401 Unauthorized response
     * with a structured JSON error message.
     *
     * @param request       The HTTP request
     * @param response      The HTTP response
     * @param authException The exception that triggered this entry point
     * @throws IOException      If an input or output error occurs
     * @throws ServletException If a servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse.of(
                "Unauthorized",
                Map.of("message", List.of("Authentication required")));

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}