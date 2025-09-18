package com.amir.task_manager_api.service;

import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.event.TaskCreatedEvent;
import com.amir.task_manager_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher publisher;

    public Task createTask(Task task) {
        Task saved = taskRepository.save(task);
        publisher.publishEvent(new TaskCreatedEvent(this, saved));
        return saved;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
