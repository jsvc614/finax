package com.example.finax.service;

import com.example.finax.model.Todo;
import com.example.finax.dto.todo.TodoStats;
import com.example.finax.model.User;
import com.example.finax.repository.TodoRepository;
import com.example.finax.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    @Override
    public Page<Todo> getAll(Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        if (completed == null) {
            return repository.findByUserId(currentUser.getId(), pageable);
        } else {
            return repository.findByUserIdAndCompleted(currentUser.getId(), completed, pageable);
        }
    }

    // get all service method with soft deletes
    @Override
    public Page<Todo> getAllActive(Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        if (completed == null) {
            return repository.findAllByUserIdAndDeletedFalse(currentUser.getId(), pageable);
        } else {
            return repository.findByUserIdAndCompletedAndDeletedFalse(currentUser.getId(), completed, pageable);
        }
    }

    @Override
    public Todo getById(Long id) {
        User currentUser = getCurrentUser();
        return repository.findByUserIdAndId(currentUser.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
    }

    @Override
    public Todo create(Todo todo) {
        User currentUser = getCurrentUser();
        todo.setUserId(currentUser.getId());
        return repository.save(todo);
    }

    @Override
    public Todo update(Long id, Todo updated) {
        Todo existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.isCompleted());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Todo existing = getById(id);
        repository.delete(existing);
    }

    @Override
    public void softDelete(Long id) {
        Todo existing = getById(id);
        existing.setDeleted(true);
        repository.save(existing);
    }

    @Override
    public Todo toggle(Long id) {
        Todo existing = getById(id);
        existing.setCompleted(!existing.isCompleted());
        return repository.save(existing);
    }

    @Override
    public TodoStats stats() {
        User currentUser = getCurrentUser();

        long total = repository.countByUserId(currentUser.getId());
        long completed = repository.countByUserIdAndCompleted(currentUser.getId(), true);
        long pending = total - completed;

        return new TodoStats(total, completed, pending);
    }

    @Override
    public List<Todo> searchTodos(String keyword) {
        User currentUser = getCurrentUser();
        return repository.searchTodos(currentUser.getId(), keyword);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}