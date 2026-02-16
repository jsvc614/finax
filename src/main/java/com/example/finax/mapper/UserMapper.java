package com.example.finax.mapper;

import com.example.finax.dto.user.UserDto;
import com.example.finax.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }
}
