package com.amir.task_manager_api.service;

import com.amir.task_manager_api.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTaskCreatedNotification(Task task, String recipient) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("Новая задача создана: " + task.getTitle());
        message.setText("Описание: " + task.getDescription() + "\nСтатус: " + task.getStatus());
        mailSender.send(message);
    }

}
