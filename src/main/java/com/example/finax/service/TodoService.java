package com.example.finax.service;

import com.example.finax.dto.TodoStats;
import com.example.finax.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {


    Page<Todo> getAll(Boolean completed, Pageable pageable);

    Todo getById(Long id);

    Todo create(Todo todo);

    Todo update(Long id, Todo todo);

    void delete(Long id);

    Todo toggle(Long id);

    TodoStats stats();
}