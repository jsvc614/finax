package com.example.finax.service;

import com.example.finax.dto.auth.LoginResponse;
import com.example.finax.dto.MessageResponse;

public interface AuthService {

    MessageResponse register(String name, String email, String password);
    LoginResponse login(String email, String password);
}
