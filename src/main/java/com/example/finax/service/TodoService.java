package com.example.finax.service;

import com.example.finax.dto.todo.TodoRequestDto;
import com.example.finax.dto.todo.TodoDto;
import com.example.finax.dto.todo.TodoStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodoService {

    Page<TodoDto> getAll(Boolean completed, Pageable pageable);

    Page<TodoDto> getAllActive(Boolean completed, Pageable pageable);

    TodoDto getById(Long id);

    TodoDto create(TodoRequestDto createTodoDto);

    TodoDto update(Long id, TodoRequestDto updateTodoDto);

    void delete(Long id);

    void softDelete(Long id);

    TodoDto toggle(Long id);

    TodoStats stats();

    List<TodoDto> searchTodos(String keyword);
}