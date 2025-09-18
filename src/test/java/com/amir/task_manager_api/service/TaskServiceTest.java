package com.amir.task_manager_api.service;

import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.model.Task.TaskStatus;
import com.amir.task_manager_api.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        task = Task.builder()
                .title("Test Task")
                .description("Integration test task")
                .status(TaskStatus.NEW)
                .build();
    }

    @Test
    void testCreateTask() {
        Task savedTask = taskService.createTask(task);
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.NEW);
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
    void testTakeToWork() {
        Task savedTask = taskService.createTask(task);
        Task inProgressTask = taskService.takeToWork(savedTask.getId());
        assertThat(inProgressTask.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        assertThrows(IllegalStateException.class, () -> taskService.takeToWork(inProgressTask.getId()));
    }

    @Test
    void testResolve() {
        Task savedTask = taskService.createTask(task);
        taskService.takeToWork(savedTask.getId());

        Task completedTask = taskService.resolve(savedTask.getId());
        assertThat(completedTask.getStatus()).isEqualTo(TaskStatus.RESOLVED);

        assertThrows(IllegalStateException.class, () -> taskService.resolve(completedTask.getId()));
    }

    @Test
    void testCancel() {
        Task savedTask = taskService.createTask(task);
        taskService.takeToWork(savedTask.getId());

        Task canceledTask = taskService.cancel(savedTask.getId());
        assertThat(canceledTask.getStatus()).isEqualTo(TaskStatus.CANCELED);

        assertThrows(IllegalStateException.class, () -> taskService.cancel(canceledTask.getId()));

        Task newTask = Task.builder()
                .title("Another Task")
                .description("desc")
                .status(TaskStatus.NEW)
                .build();
        Task saved = taskService.createTask(newTask);
        taskService.takeToWork(saved.getId());
        Task canceledInProgress = taskService.cancel(saved.getId());
        assertThat(canceledInProgress.getStatus()).isEqualTo(TaskStatus.CANCELED);
    }

    @Test
    void testDeleteTask() {
        Task savedTask = taskService.createTask(task);
        taskService.deleteTask(savedTask.getId());
        Optional<Task> deletedTask = taskService.getTaskById(savedTask.getId());
        assertThat(deletedTask).isEmpty();
    }
}
