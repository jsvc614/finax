package com.example.finax.service;

import com.example.finax.auth.JwtUtil;
import com.example.finax.dto.auth.LoginResponse;
import com.example.finax.dto.auth.RegisterRequest;
import com.example.finax.exception.InvalidCredentialsException;
import com.example.finax.exception.UserAlreadyExistsException;
import com.example.finax.dto.MessageResponse;
import com.example.finax.mapper.UserMapper;
import com.example.finax.model.User;
import com.example.finax.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;

    /**
     * Registers a new user account with email uniqueness validation.
     * Encrypts password using BCrypt before storing in database.
     * 
     * @param name     User's display name
     * @param email    User's email address (must be unique)
     * @param password Plain text password (will be encrypted)
     * @return Success message response
     * @throws RuntimeException if email already exists in system
     */
    public MessageResponse register(RegisterRequest registerRequest) {
        // Check for duplicate email addresses (unique constraint)
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Registration failed - please try again");
        }

        // Create new user entity with BCrypt encrypted password
        User user = userMapper.toUser(registerRequest);

        userRepository.save(user);
        return MessageResponse.builder().message("User registered successfully").build();
    }

    /**
     * Authenticates user login credentials and generates JWT token.
     * Validates email existence and password match using BCrypt.
     * 
     * @param email    User's email address
     * @param password Plain text password to verify
     * @return LoginResponse containing JWT token and user details
     * @throws RuntimeException if credentials are invalid
     */
    public LoginResponse login(String email, String password) {
        // Find user by email (throws exception if not found)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        // Verify password using BCrypt (compares plain text with hashed password)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Generate JWT token and return with user data (excluding sensitive fields)
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(email))
                .user(userMapper.toUserDto(user)) // DTO excludes password
                .build();
    }
}