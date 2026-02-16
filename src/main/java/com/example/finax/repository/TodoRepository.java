package com.example.finax.repository;

import com.example.finax.model.Todo;
import com.example.finax.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByUserId(Long userId, Pageable pageable);
    Page<Todo> findByUserIdAndCompleted(Long userId, boolean completed, Pageable pageable);

    Optional<Todo> findByUserIdAndId(Long userId, Long id);
    @Query("""
        SELECT t
        FROM Todo t
        WHERE t.userId = :userId
          AND t.deleted = false
          AND (
                LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
    """)
    List<Todo> searchTodos(Long userId, String keyword);
    Page<Todo> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Todo> findByUserIdAndCompletedAndDeletedFalse(Long userId, boolean completed, Pageable pageable);
    long countByUserIdAndCompleted(Long userId, boolean completed);
    long countByUserId(long userId);
}