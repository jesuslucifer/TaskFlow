package com.example.backend.service.impl;

import com.example.backend.model.Project;
import com.example.backend.model.User;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.service.ExecutorNotificationService;
import com.example.backend.service.NotificationHistoryService;
import com.example.backend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExecutorNotificationServiceImpl implements ExecutorNotificationService {
    private final NotificationService notificationService;
    private final NotificationHistoryService notificationHistoryService;
    private final String destination = "/queue/executor-notification";

    @Override
    public void sendNotificationToAddExecutor(User user, Project project) {
        String message = "Вас назначили исполнителем в проекте " + project.getName();
        notificationService.sendNotificationToUser(
                user.getUsername(),
                message,
                destination);
        notificationService.saveNotification(project,
                "add_executor",
                "1",
                user,
                LocalDateTime.now());
    }

    @Override
    @Transactional
    public void sendNotificationToDeleteExecutor(User user, Project project) {
        String message = "Вас удалил из списка исполнителей в проекте " + project.getName();
        notificationService.sendNotificationToUser(
                user.getUsername(),
                message,
                destination
        );
        notificationHistoryService.deleteNotificationByProjectIdAndUserId(project.getId(), user.getId());
        notificationService.saveNotification(project,
                "delete_executor",
                "1",
                user,
                LocalDateTime.now());

    }
}
