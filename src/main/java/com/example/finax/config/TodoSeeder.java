package com.example.finax.config;

import com.example.finax.model.Todo;
import com.example.finax.model.User;
import com.example.finax.repository.TodoRepository;
import com.example.finax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * TodoSeeder is a utility class that seeds the database with initial data for testing purposes.
 * It creates a default user and a set of sample todos if the database is empty.
 */
@Component
public class TodoSeeder implements CommandLineRunner {

        @Autowired
        private TodoRepository repository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;


        /**
         * Seeds the database with a default user and sample todos.
         * This method is executed automatically when the application starts.
         *
         * @param args Command-line arguments (not used)
         */
        @Override
        public void run(String... args) {

                User user;

                // Create user only if none exists
                if (userRepository.count() == 0) {
                        user = User.builder()
                                        .name("a")
                                        .email("a@gmail.com")
                                        .password(passwordEncoder.encode("123"))
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
                                                        .user(user)
                                                        .completed(false)
                                                        .build(),

                                        Todo.builder()
                                                        .title("Finish assignment")
                                                        .description("Complete Spring Boot Todo API assignment")
                                                        .user(user)
                                                        .completed(true)
                                                        .build(),

                                        Todo.builder()
                                                        .title("Call mom")
                                                        .description("Check in with family")
                                                        .user(user)
                                                        .completed(false)
                                                        .build(),

                                        Todo.builder()
                                                        .title("Call my friend's mommy")
                                                        .description("Check in with family")
                                                        .user(user)
                                                        .completed(false)
                                                        .build());

                        repository.saveAll(todos);
                        System.out.println("Seeded sample todos!");
                }
        }
}