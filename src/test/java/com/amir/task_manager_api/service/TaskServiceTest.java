package com.amir.task_manager_api.service;

import com.amir.task_manager_api.repository.TaskRepository;
import com.amir.task_manager_api.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        task = Task.builder()
                .title("Test Task")
                .description("Integration test task")
                .status("NEW")
                .build();
    }

    @Test
    void testCreateTask() {
        Task savedTask = taskService.createTask(task);
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void testGetAllTasks() {
        taskService.createTask(task);
        List<Task> tasks = taskService.getAllTasks();
        assertThat(tasks).hasSize(1);
    }

    @Test
    void testGetTaskById() {
        Task savedTask = taskService.createTask(task);
        Optional<Task> foundTask = taskService.getTaskById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void testUpdateTask() {
        Task savedTask = taskService.createTask(task);
        savedTask.setStatus("DONE");
        Task updatedTask = taskService.updateTask(savedTask);
        assertThat(updatedTask.getStatus()).isEqualTo("DONE");
    }

    @Test
    void testDeleteTask() {
        Task savedTask = taskService.createTask(task);
        taskService.deleteTask(savedTask.getId());
        Optional<Task> deletedTask = taskService.getTaskById(savedTask.getId());
        assertThat(deletedTask).isEmpty();
    }

}