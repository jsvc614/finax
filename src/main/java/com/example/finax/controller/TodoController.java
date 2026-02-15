package com.example.finax.controller;

import com.example.finax.dto.SuccessResponse;
import com.example.finax.dto.TodoStats;
import com.example.finax.model.Todo;
import com.example.finax.service.TodoService;
import com.example.finax.util.PageHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService service;

    @GetMapping
    public SuccessResponse<Map<String, Object>> getAll(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Boolean completedFilter = null;
        if ("completed".equalsIgnoreCase(status)) completedFilter = true;
        else if ("pending".equalsIgnoreCase(status)) completedFilter = false;

        Page<Todo> todosPage = service.getAll(completedFilter, pageable);

        return SuccessResponse.of(PageHelper.toMap(todosPage), "Todos fetched successfully");
    }

    @GetMapping("/{id}")
    public SuccessResponse<Todo> getOne(@PathVariable Long id) {
        return SuccessResponse.of(service.getById(id), "Todo fetched successfully");
    }

    @PostMapping
    public SuccessResponse<Todo> create(@Valid @RequestBody Todo todo) {
        return SuccessResponse.of(service.create(todo), "Todo created successfully");
    }

    @PutMapping("/{id}")
    public SuccessResponse<Todo> update(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        return SuccessResponse.of(service.update(id, todo), "Todo updated successfully");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return SuccessResponse.of(null, "Todo deleted successfully");
    }

    @PatchMapping("/{id}/toggle")
    public SuccessResponse<Todo> toggle(@PathVariable Long id) {
        return SuccessResponse.of(service.toggle(id), "Todo toggled successfully");
    }

    @GetMapping("/stats")
    public SuccessResponse<TodoStats> stats() {
        return SuccessResponse.of(service.stats(), "Todo statistics fetched successfully");
    }
}
