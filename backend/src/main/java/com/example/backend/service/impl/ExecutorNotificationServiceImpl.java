package com.example.backend.service.impl;

import com.example.backend.model.DeliveryMethod;
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
    private final EmailService emailService;
    private final String destination = "/queue/executor-notification";

    @Override
    public void sendNotificationToAddExecutor(User user, Project project) {
        String message = "Вас пригласили исполнителем в проект " + project.getName();
        if (userNotificationSettingsService.notificationIsEnabled(user.getId(), DeliveryMethod.PUSH)) {
            notificationService.sendNotificationToUser(
                    user.getUsername(),
                    message,
                    destination);
            notificationService.saveNotification(project,
                    NotificationType.ADD_EXECUTOR,
                    "1",
                    user,
                    LocalDateTime.now(),
                    DeliveryMethod.PUSH);
        }

        if (userNotificationSettingsService.notificationIsEnabled(user.getId(), DeliveryMethod.EMAIL)) {
            emailService.send(user.getEmail(), "Приглашение в проект", message);

            notificationService.saveNotification(project,
                    NotificationType.ADD_EXECUTOR,
                    "1",
                    user,
                    LocalDateTime.now(),
                    DeliveryMethod.EMAIL);
        }
    }

    @Override
    @Transactional
    public void sendNotificationToDeleteExecutor(User user, Project project) {
        String message = "Вас удалил из списка исполнителей в проекте " + project.getName();
        if (userNotificationSettingsService.notificationIsEnabled(user.getId(), DeliveryMethod.PUSH)
                && projectNotificationSettingsService.notificationIsEnabled(project.getId(), user.getId(), DeliveryMethod.PUSH)) {
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
                    LocalDateTime.now(),
                    DeliveryMethod.PUSH);
        }

        if (userNotificationSettingsService.notificationIsEnabled(user.getId(), DeliveryMethod.EMAIL)
                && projectNotificationSettingsService.notificationIsEnabled(project.getId(), user.getId(), DeliveryMethod.EMAIL)) {
            emailService.send(user.getEmail(), "Удаление из проекта", message);

            notificationService.saveNotification(project,
                    NotificationType.DELETE_EXECUTOR,
                    "1",
                    user,
                    LocalDateTime.now(),
                    DeliveryMethod.EMAIL);
        }
    }

    @Override
    public void sendNotificationToAcceptInviteExecutor(User creator, Project project, User executor) {
        if (userNotificationSettingsService.notificationIsEnabled(creator.getId(), DeliveryMethod.PUSH)
        && projectNotificationSettingsService.notificationIsEnabled(project.getId(), creator.getId(), DeliveryMethod.PUSH)) {
            String message = executor.getUsername() + " принял Ваше приглашение в проект " + project.getName();
            notificationService.sendNotificationToUser(
                    creator.getUsername(),
                    message,
                    destination);
            notificationService.saveNotification(project,
                    NotificationType.ACCEPT_EXECUTOR,
                    "1",
                    creator,
                    LocalDateTime.now(),
                    DeliveryMethod.PUSH);
        }
    }

    @Override
    public void sendNotificationToDeclineInviteExecutor(User creator, Project project, User executor) {
        if (userNotificationSettingsService.notificationIsEnabled(creator.getId(), DeliveryMethod.PUSH)
        && projectNotificationSettingsService.notificationIsEnabled(project.getId(), creator.getId(), DeliveryMethod.PUSH)) {
            String message = executor.getUsername() + " отклонил Ваше приглашение в проект " + project.getName();
            notificationService.sendNotificationToUser(
                    creator.getUsername(),
                    message,
                    destination);
            notificationService.saveNotification(project,
                    NotificationType.DECLINE_EXECUTOR,
                    "1",
                    creator,
                    LocalDateTime.now(),
                    DeliveryMethod.PUSH);
        }
    }

    @Override
    public void sendNotificationToChangeStatusProject(Project project) {
        String message = "Статус проекта " + project.getName() + " изменен на " + project.getStatus();
        project.getExecutors().forEach(executor -> {
            if (userNotificationSettingsService.notificationIsEnabled(executor.getId().getUserId(), DeliveryMethod.PUSH)
                    && projectNotificationSettingsService.notificationIsEnabled(project.getId(), executor.getId().getUserId(), DeliveryMethod.PUSH)) {
                notificationService.sendNotificationToUser(
                        executor.getUser().getUsername(),
                        message,
                        destination);
                notificationService.saveNotification(project,
                        NotificationType.CHANGE_STATUS,
                        "1",
                        executor.getUser(),
                        LocalDateTime.now(),
                        DeliveryMethod.PUSH);
            }

            if (userNotificationSettingsService.notificationIsEnabled(executor.getId().getUserId(), DeliveryMethod.EMAIL)
                    && projectNotificationSettingsService.notificationIsEnabled(project.getId(), executor.getId().getUserId(), DeliveryMethod.EMAIL)) {
                emailService.send(executor.getUser().getEmail(), "Изменение статуса проекта", message);

                notificationService.saveNotification(project,
                        NotificationType.DELETE_EXECUTOR,
                        "1",
                        executor.getUser(),
                        LocalDateTime.now(),
                        DeliveryMethod.EMAIL);
            }
        });
    }
}
