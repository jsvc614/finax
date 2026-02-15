package com.example.finax.config;

import com.example.finax.model.Todo;
import com.example.finax.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoSeeder implements CommandLineRunner {

    private final TodoRepository repository;

    @Override
    public void run(String... args) {
        // Only seed if table is empty
        if (repository.count() == 0) {
            List<Todo> todos = List.of(
                    Todo.builder()
                            .title("Buy groceries")
                            .description("Milk, eggs, bread")
                            .completed(false)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    Todo.builder()
                            .title("Finish assignment")
                            .description("Complete Spring Boot Todo API assignment")
                            .completed(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),

                    Todo.builder()
                            .title("Call mom")
                            .description("Check in with family")
                            .completed(false)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            );

            repository.saveAll(todos);
            System.out.println("Seeded sample todos!");
        }
    }
}