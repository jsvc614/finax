package com.example.finax.service;

import com.example.finax.dto.todo.TodoStats;
import com.example.finax.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodoService {


    Page<Todo> getAll(Boolean completed, Pageable pageable);

    Page<Todo> getAllActive(Boolean completed, Pageable pageable);

    Todo getById(Long id);

    Todo create(Todo todo);

    Todo update(Long id, Todo todo);

    void delete(Long id);

    void softDelete(Long id);

    Todo toggle(Long id);

    TodoStats stats();

    List<Todo> searchTodos(String keyword);
}