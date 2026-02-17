package com.example.finax.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.finax.dto.todo.TodoDto;
import com.example.finax.dto.todo.TodoRequestDto;
import com.example.finax.model.Todo;

@Component
public class TodoMapper {
    public TodoDto mapToDto(Todo todo) {
        if (todo == null) {
            return null;
        }

        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }

    public Todo mapToEntity(TodoRequestDto todoDto) {
        if (todoDto == null) {
            return null;
        }

        return Todo.builder()
                .title(todoDto.getTitle())
                .description(todoDto.getDescription())
                .completed(todoDto.isCompleted())
                .build();
    }
}