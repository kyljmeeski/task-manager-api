package com.amir.task_manager_api.event;

import com.amir.task_manager_api.model.Task;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskCreatedEvent extends ApplicationEvent {

    private final Task task;

    public TaskCreatedEvent(Object source, Task task) {
        super(source);
        this.task = task;
    }

}
