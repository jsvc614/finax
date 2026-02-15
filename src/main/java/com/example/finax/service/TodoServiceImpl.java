package com.example.finax.service;

import com.example.finax.model.Todo;
import com.example.finax.dto.TodoStats;
import com.example.finax.repository.TodoRepository;
import com.example.finax.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    @Override
    public Page<Todo> getAll(Boolean completed, Pageable pageable) {
        if (completed == null) {
            return repository.findAll(pageable);
        } else {
            return repository.findByCompleted(completed, pageable);
        }
    }

    @Override
    public Todo getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
    }

    @Override
    public Todo create(Todo todo) {
        // createdAt and updatedAt are handled by @PrePersist in entity
        return repository.save(todo);
    }

    @Override
    public Todo update(Long id, Todo updated) {
        Todo existing = getById(id); // throws ResourceNotFoundException if not found
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
    public Todo toggle(Long id) {
        Todo existing = getById(id);
        existing.setCompleted(!existing.isCompleted());
        return repository.save(existing);
    }

    @Override
    public TodoStats stats() {
        long total = repository.count();
        long completed = repository.countByCompleted(true);
        long pending = total - completed;
        return new TodoStats(total, completed, pending);
    }
}