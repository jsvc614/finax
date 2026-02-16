package com.example.finax.controller;

import com.example.finax.dto.*;
import com.example.finax.dto.auth.LoginRequest;
import com.example.finax.dto.auth.LoginResponse;
import com.example.finax.dto.auth.RegisterRequest;
import com.example.finax.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> register(@Valid @RequestBody RegisterRequest req) {
        // we can return token in case we want use to be logged in after registering
//        String token = authService.register(req.getName(), req.getEmail(), req.getPassword());
        MessageResponse messageResponse = authService.register(req.getName(), req.getEmail(), req.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of(messageResponse.getMessage(), "Register successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse token = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(SuccessResponse.of(token, "Login successful"));
    }
}