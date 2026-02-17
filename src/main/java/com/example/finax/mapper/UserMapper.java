package com.example.finax.mapper;

import com.example.finax.dto.auth.RegisterRequest;
import com.example.finax.dto.user.UserDto;
import com.example.finax.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto toUserDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    public User toUser(RegisterRequest registerRequest) {
        return  User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // BCrypt encryption
                .build();
    }
}
