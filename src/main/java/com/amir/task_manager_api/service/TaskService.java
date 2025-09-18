package com.amir.task_manager_api.service;

import com.amir.task_manager_api.event.TaskCreatedEvent;
import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.amir.task_manager_api.model.Task.TaskStatus.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher publisher;

    @CacheEvict(value = "tasks", allEntries = true)
    public Task createTask(Task task) {
        Task saved = taskRepository.save(task);
        publisher.publishEvent(new TaskCreatedEvent(this, saved));
        return saved;
    }

    @Cacheable(value = "tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public Task takeToWork(long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getStatus() != NEW) {
            throw new IllegalStateException("Task have to be NEW to take to work");
        }
        task.setStatus(IN_PROGRESS);
        return taskRepository.save(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public Task resolve(long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getStatus() != IN_PROGRESS) {
            throw new IllegalStateException("Task have to be IN_PROGRESS to resolve");
        }
        task.setStatus(RESOLVED);
        return taskRepository.save(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public Task cancel(long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getStatus() != IN_PROGRESS) {
            throw new IllegalStateException("Task have to be IN_PROGRESS to cancel");
        }
        task.setStatus(CANCELED);
        return taskRepository.save(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.deleteById(id);
    }

}
