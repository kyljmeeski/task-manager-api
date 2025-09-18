package com.amir.task_manager_api.controller;

import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Integration test");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Integration test"));
    }

    @Test
    void getAllTasks_ShouldReturnTasksList() throws Exception {
        taskRepository.save(new Task(null, "Task 1", "Desc 1", null, null, null));
        taskRepository.save(new Task(null, "Task 2", "Desc 2", null, null, null));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        Task saved = taskRepository.save(new Task(null, "Single Task", "Desc", null, null, null));

        mockMvc.perform(get("/api/tasks/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Single Task"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    void getTaskById_NotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        Task saved = taskRepository.save(new Task(null, "Old Title", "Old Desc", null, null, null));
        Task updated = new Task();
        updated.setTitle("New Title");
        updated.setDescription("New Desc");

        mockMvc.perform(put("/api/tasks/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Desc"));
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        Task saved = taskRepository.save(new Task(null, "Task to Delete", "Desc", null, null, null));

        mockMvc.perform(delete("/api/tasks/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        Optional<Task> deleted = taskRepository.findById(saved.getId());
        assert(deleted.isEmpty());
    }
}
