package com.example.finax.repository;

import com.example.finax.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByCompleted(boolean completed, Pageable pageable);
    long countByCompleted(boolean completed);
}