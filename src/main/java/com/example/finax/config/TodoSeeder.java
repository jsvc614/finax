package com.example.finax.config;

import com.example.finax.model.Todo;
import com.example.finax.model.User;
import com.example.finax.repository.TodoRepository;
import com.example.finax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TodoSeeder implements CommandLineRunner {

    @Autowired
    private TodoRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        User user;

        // Create user only if none exists
        if (userRepository.count() == 0) {
            user = User.builder()
                    .name("a")
                    .email("a@gmail.com")
                    .password("123")
                    .build();

            user = userRepository.save(user); // IMPORTANT: capture saved entity
        } else {
            user = userRepository.findAll().get(0);
        }

        // Seed todos only if empty
        if (repository.count() == 0) {

            List<Todo> todos = List.of(
                    Todo.builder()
                            .title("Buy groceries")
                            .description("Milk, eggs, bread")
                            .userId(user.getId())
                            .completed(false)
                            .build(),

                    Todo.builder()
                            .title("Finish assignment")
                            .description("Complete Spring Boot Todo API assignment")
                            .userId(user.getId())
                            .completed(true)
                            .build(),

                    Todo.builder()
                            .title("Call mom")
                            .description("Check in with family")
                            .userId(user.getId())
                            .completed(false)
                            .build(),

                    Todo.builder()
                            .title("Call my friend's mommy")
                            .description("Check in with family")
                            .userId(user.getId())
                            .completed(false)
                            .build()
            );

            repository.saveAll(todos);
            System.out.println("Seeded sample todos!");
        }
    }}