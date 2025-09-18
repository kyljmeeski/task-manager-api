package com.amir.task_manager_api.controller;

import com.amir.task_manager_api.dto.NewTaskDTO;
import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import static com.amir.task_manager_api.model.Task.TaskStatus.NEW;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
                .status(NEW)
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

    @PatchMapping("/{id}/take")
    @Operation(summary = "Взять на работу")
    public ResponseEntity<Task> takeToWork(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.takeToWork(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(CONFLICT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Решить")
    public ResponseEntity<Task> resolve(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.resolve(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(CONFLICT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Отменить")
    public ResponseEntity<Task> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.cancel(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(CONFLICT).build();
        }catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
