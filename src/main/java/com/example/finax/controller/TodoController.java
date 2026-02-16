package com.example.finax.controller;

import com.example.finax.dto.SuccessResponse;
import com.example.finax.dto.todo.TodoStats;
import com.example.finax.model.Todo;
import com.example.finax.service.TodoService;
import com.example.finax.util.CompletedFilterHelper;
import com.example.finax.util.PageHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo APIs", description = "APIs that require JWT authentication")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {
    @Autowired
    private TodoService service;

    @GetMapping
    public ResponseEntity<SuccessResponse<Map<String, Object>>> getAll(
            @RequestParam(required = false) String status,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Todo> todosPage = service.getAll(CompletedFilterHelper.getCompletedFilter(status), pageable);
        return ResponseEntity.ok(SuccessResponse.of(PageHelper.toMap(todosPage), "Todos fetched successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> getAllActive(
            @RequestParam(required = false) String status,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Todo> todosPage = service.getAllActive(CompletedFilterHelper.getCompletedFilter(status), pageable);
        return ResponseEntity.ok(SuccessResponse.of(PageHelper.toMap(todosPage), "Todos fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Todo>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.of(service.getById(id), "Todo fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Todo>> create(@Valid @RequestBody Todo todo) {
        Todo created = service.create(todo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(created, "Todo created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Todo>> update(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        return ResponseEntity.ok(SuccessResponse.of(service.update(id, todo), "Todo updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(SuccessResponse.of(null, "Todo deleted successfully"));
    }

    @DeleteMapping("/softDelete/{id}")
    public ResponseEntity<SuccessResponse<Void>> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.ok(SuccessResponse.of(null, "Todo deleted successfully"));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<SuccessResponse<Todo>> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.of(service.toggle(id), "Todo toggled successfully"));
    }

    @GetMapping("/stats")
    public ResponseEntity<SuccessResponse<TodoStats>> stats() {
        return ResponseEntity.ok(SuccessResponse.of(service.stats(), "Todo statistics fetched successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<Todo>>> searchTodos(@NotBlank @RequestParam String keyword){
        return ResponseEntity.ok(SuccessResponse.of(service.searchTodos(keyword), "Todos search results fetched successfully"));
    }
}