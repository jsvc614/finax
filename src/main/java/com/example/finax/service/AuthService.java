package com.example.finax.service;

import com.example.finax.dto.auth.LoginResponse;
import com.example.finax.dto.MessageResponse;
import com.example.finax.dto.auth.RegisterRequest;

public interface AuthService {

    MessageResponse register(RegisterRequest registerRequest);
    LoginResponse login(String email, String password);
}
