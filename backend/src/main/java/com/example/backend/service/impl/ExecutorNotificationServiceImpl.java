package com.example.backend.service.impl;

import com.example.backend.model.NotificationType;
import com.example.backend.model.Project;
import com.example.backend.model.User;
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
        String message = "Вас пригласили исполнителем в проект " + project.getName();
        notificationService.sendNotificationToUser(
                user.getUsername(),
                message,
                destination);
        notificationService.saveNotification(project,
                NotificationType.ADD_EXECUTOR,
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
                NotificationType.DELETE_EXECUTOR,
                "1",
                user,
                LocalDateTime.now());
    }

    @Override
    public void sendNotificationToAcceptInviteExecutor(User creator, Project project, User executor) {
        String message = executor.getUsername() + " принял Ваше приглашение в проект " + project.getName();
        notificationService.sendNotificationToUser(
                creator.getUsername(),
                message,
                destination);
        notificationService.saveNotification(project,
                NotificationType.ACCEPT_EXECUTOR,
                "1",
                creator,
                LocalDateTime.now());
    }

    @Override
    public void sendNotificationToDeclineInviteExecutor(User creator, Project project, User executor) {
        String message = executor.getUsername() + " отклонил Ваше приглашение в проект " + project.getName();
        notificationService.sendNotificationToUser(
                creator.getUsername(),
                message,
                destination);
        notificationService.saveNotification(project,
                NotificationType.DECLINE_EXECUTOR,
                "1",
                creator,
                LocalDateTime.now());
    }
}
