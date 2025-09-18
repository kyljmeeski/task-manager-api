package com.amir.task_manager_api.event;

import com.amir.task_manager_api.model.Task;
import com.amir.task_manager_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class TaskEventListener {

    private final EmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    @EventListener
    @Async
    public void handleTaskCreated(TaskCreatedEvent event) {
        Task task = event.getTask();
        emailService.sendTaskCreatedNotification(task, adminEmail);
    }

}
