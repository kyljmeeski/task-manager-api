package com.amir.task_manager_api.repository;

import com.amir.task_manager_api.model.Task;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        task = Task.builder()
                .title("Test Task")
                .description("This is a test task")
                .status("NEW")
                .build();
    }

    @Test
    void testCreateTask() {
        Task savedTask = taskRepository.save(task);
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void testReadTask() {
        Task savedTask = taskRepository.save(task);
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");

        List<Task> allTasks = taskRepository.findAll();
        assertThat(allTasks).hasSize(1);
    }

    @Test
    void testUpdateTask() {
        Task savedTask = taskRepository.save(task);
        savedTask.setStatus("DONE");
        Task updatedTask = taskRepository.save(savedTask);

        assertThat(updatedTask.getStatus()).isEqualTo("DONE");
    }

    @Test
    void testDeleteTask() {
        Task savedTask = taskRepository.save(task);
        taskRepository.delete(savedTask);
        assertThat(taskRepository.findById(savedTask.getId())).isEmpty();
    }

}