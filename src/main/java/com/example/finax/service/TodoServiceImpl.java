package com.example.finax.service;

import com.example.finax.dto.todo.TodoRequestDto;
import com.example.finax.dto.todo.TodoDto;
import com.example.finax.dto.todo.TodoStats;
import com.example.finax.exception.ResourceNotFoundException;
import com.example.finax.mapper.TodoMapper;
import com.example.finax.model.Todo;
import com.example.finax.model.User;
import com.example.finax.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoMapper todoMapper;

    @Override
    public Page<TodoDto> getAll(Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Todo> todos = (completed == null)
                ? todoRepository.findByUserId(currentUser.getId(), pageable)
                : todoRepository.findByUserIdAndCompleted(currentUser.getId(), completed, pageable);
        return todos.map(todoMapper::mapToDto);
    }

    @Override
    public Page<TodoDto> getAllActive(Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Todo> todos = (completed == null)
                ? todoRepository.findAllByUserIdAndDeletedFalse(currentUser.getId(), pageable)
                : todoRepository.findByUserIdAndCompletedAndDeletedFalse(currentUser.getId(), completed, pageable);
        return todos.map(todoMapper::mapToDto);
    }

    @Override
    public TodoDto getById(Long id) {
        User currentUser = getCurrentUser();
        Todo todo = todoRepository.findByUserIdAndId(currentUser.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
        return todoMapper.mapToDto(todo);
    }

    @Override
    public TodoDto create(TodoRequestDto createTodoDto) {
        User currentUser = getCurrentUser();
        Todo todo = todoMapper.mapToEntity(createTodoDto);
        todo.setUser(currentUser);
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.mapToDto(savedTodo);
    }

    @Override
    public TodoDto update(Long id, TodoRequestDto updateTodoDto) {
        Todo existingTodo = getByIdEntity(id);

        if (updateTodoDto.getTitle() != null) {
            existingTodo.setTitle(updateTodoDto.getTitle());
        }
        if (updateTodoDto.getDescription() != null) {
            existingTodo.setDescription(updateTodoDto.getDescription());
        }
        existingTodo.setCompleted(updateTodoDto.isCompleted());

        Todo updatedTodo = todoRepository.save(existingTodo);

        return todoMapper.mapToDto(updatedTodo);
    }

    @Override
    public void delete(Long id) {
        Todo existingTodo = getByIdEntity(id);
        todoRepository.delete(existingTodo);
    }

    @Override
    public void softDelete(Long id) {
        Todo existingTodo = getByIdEntity(id);
        existingTodo.setDeleted(true);
        todoRepository.save(existingTodo);
    }

    @Override
    public TodoDto toggle(Long id) {
        Todo existingTodo = getByIdEntity(id);
        existingTodo.setCompleted(!existingTodo.isCompleted());
        Todo toggledTodo = todoRepository.save(existingTodo);
        return todoMapper.mapToDto(toggledTodo);
    }

    @Override
    public TodoStats stats() {
        User currentUser = getCurrentUser();
        long total = todoRepository.countByUserId(currentUser.getId());
        long completed = todoRepository.countByUserIdAndCompleted(currentUser.getId(), true);
        long pending = total - completed;
        return new TodoStats(total, completed, pending);
    }

    @Override
    public List<TodoDto> searchTodos(String keyword) {
        User currentUser = getCurrentUser();
        List<Todo> todos = todoRepository.searchTodos(currentUser.getId(), keyword);
        return todos.stream().map(todoMapper::mapToDto).collect(Collectors.toList());
    }

    private Todo getByIdEntity(Long id) {
        User currentUser = getCurrentUser();
        return todoRepository.findByUserIdAndId(currentUser.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}