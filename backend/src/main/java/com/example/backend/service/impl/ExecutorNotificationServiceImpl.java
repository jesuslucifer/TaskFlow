package com.example.backend.service.impl;

import com.example.backend.model.NotificationType;
import com.example.backend.model.Project;
import com.example.backend.model.User;
import com.example.backend.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExecutorNotificationServiceImpl implements ExecutorNotificationService {
    private final NotificationService notificationService;
    private final NotificationHistoryService notificationHistoryService;
    private final UserNotificationSettingsService userNotificationSettingsService;
    private final ProjectNotificationSettingsService projectNotificationSettingsService;
    private final String destination = "/queue/executor-notification";

    @Override
    public void sendNotificationToAddExecutor(User user, Project project) {
        if (userNotificationSettingsService.notificationIsEnabled(user.getId())) {
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
    }

    @Override
    @Transactional
    public void sendNotificationToDeleteExecutor(User user, Project project) {
        if (userNotificationSettingsService.notificationIsEnabled(user.getId())
        && projectNotificationSettingsService.notificationIsEnabled(project.getId(), user.getId())) {
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
    }

    @Override
    public void sendNotificationToAcceptInviteExecutor(User creator, Project project, User executor) {
        if (userNotificationSettingsService.notificationIsEnabled(creator.getId())
        && projectNotificationSettingsService.notificationIsEnabled(project.getId(), creator.getId())) {
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
    }

    @Override
    public void sendNotificationToDeclineInviteExecutor(User creator, Project project, User executor) {
        if (userNotificationSettingsService.notificationIsEnabled(creator.getId())
        && projectNotificationSettingsService.notificationIsEnabled(project.getId(), creator.getId())) {
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

    @Override
    public void sendNotificationToChangeStatusProject(Project project) {
        String message = "Статус проекта " + project.getName() + " изменен на " + project.getStatus();
        project.getExecutors().forEach(executor -> {
            if (userNotificationSettingsService.notificationIsEnabled(executor.getId().getUserId())
                    && projectNotificationSettingsService.notificationIsEnabled(project.getId(), executor.getId().getUserId())) {
                notificationService.sendNotificationToUser(
                        executor.getUser().getUsername(),
                        message,
                        destination);
                notificationService.saveNotification(project,
                        NotificationType.CHANGE_STATUS,
                        "1",
                        executor.getUser(),
                        LocalDateTime.now());
            }
        });
    }
}
