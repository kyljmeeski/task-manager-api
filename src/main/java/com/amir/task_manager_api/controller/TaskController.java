package com.amir.task_manager_api.controller;

import com.amir.task_manager_api.dto.NewTaskDTO;
import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "CRUD операции для задач")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Создать задачу")
    public ResponseEntity<Task> createTask(@RequestBody NewTaskDTO newTask) {
        Task createdTask = taskService.createTask(Task.builder()
                .title(newTask.title())
                .description(newTask.description())
                .status("NEW")
                .build()
        );
        return ResponseEntity.created(URI.create("/api/tasks/" + createdTask.getId()))
                .body(createdTask);
    }

    @GetMapping
    @Operation(summary = "Получить все задачи")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.getTaskById(id)
                .map(existing -> {
                    task.setId(existing.getId());
                    Task updated = taskService.updateTask(task);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(existing -> {
                    taskService.deleteTask(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
