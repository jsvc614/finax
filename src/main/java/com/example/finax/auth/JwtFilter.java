package com.example.finax.auth;

import com.example.finax.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  JwtUtil jwtUtil;
    /**
     * Processes each HTTP request to extract and validate JWT tokens.
     * Sets up Spring Security authentication context if valid token is found.
     * 
     * @param request HTTP request containing potential Authorization header
     * @param response HTTP response (unused in this method)
     * @param filterChain Filter chain to continue processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract Authorization header from request
        String header = request.getHeader("Authorization");
        String token;
        String email = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

            email = jwtUtil.validateToken(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userRepository.findByEmail(email).ifPresent(user -> {
                // Create authentication token with user as principal
                // No credentials needed (null) as JWT validation already occurred
                // No authorities/roles set (null) - could be enhanced for role-based access
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                // Set authentication in Spring Security context for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            });
        }

        filterChain.doFilter(request, response);
    }
}