package com.reunico.bpm.listener;

import com.reunico.bpm.service.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
public class NotifyManager implements TaskListener {

    private final NotificationService notificationService;

    public NotifyManager(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        notificationService.sendMessage(String.format("Please, get your task %s", delegateTask.getId() ));
    }
}
