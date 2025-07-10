package com.example.backend.service.impl;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.NotificationType;
import com.example.backend.model.Project;
import com.example.backend.model.ProjectExecutor;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadlineNotificationServiceImpl implements DeadlineNotificationService {
    private final ProjectRepository projectRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationService notificationService;
    private final UserNotificationSettingsService userNotificationSettingsService;
    private final ProjectNotificationSettingsService projectNotificationSettingsService;
    private final EmailService emailService;

    private final String destination = "/queue/deadline-notification";

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void checkDeadlines() {
        checkDeadlinesOfDays(3);
        checkDeadlinesOfDays(1);
        checkDeadlinesOfHours(1);
    }


    private void checkDeadlinesOfDays(int days) {
        LocalDate dateNow = LocalDate.now();
        LocalDate targetDate = dateNow.plusDays(days);

        List<Project> projects = projectRepository.findAllByDateToIs(targetDate);
        sendNotification(projects, NotificationType.DEADLINE_DAYS,  String.valueOf(days));
    }

    private void checkDeadlinesOfHours(int hours) {
        LocalDate dateNow = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Project> projects = projectRepository.findAllByDateToAndTimeLeftBetween(dateNow, now, now.plusHours(hours));
        sendNotification(projects, NotificationType.DEADLINE_HOURS,  String.valueOf(hours));
    }

    private void sendNotification(List<Project> projects, NotificationType typeNotification, String periodNotification) {
        projects.forEach(project -> {
            String message = generateMessage(project.getName(), typeNotification, periodNotification);
            project.getExecutors().forEach(executor -> {
                processNotification(project, executor, typeNotification, periodNotification, message, DeliveryMethod.PUSH);
                processNotification(project, executor, typeNotification, periodNotification, message, DeliveryMethod.EMAIL);
            });
        });
    }

    private void processNotification(Project project,
                                     ProjectExecutor executor,
                                     NotificationType typeNotification,
                                     String periodNotification,
                                     String message,
                                     DeliveryMethod deliveryMethod) {
        Long userId = executor.getUser().getId();
        Long projectId = project.getId();

        if (!userNotificationSettingsService.notificationIsEnabled(userId, deliveryMethod)) return;
        if (!projectNotificationSettingsService.notificationIsEnabled(projectId, userId, deliveryMethod)) return;
        if (notificationHistoryRepository.existsByProjectIdAndTypeNotificationAndPeriodNotificationAndUserIdAndDeliveryMethod(
                projectId, typeNotification, periodNotification, userId, deliveryMethod)) return;

        switch (deliveryMethod) {
            case PUSH: notificationService.sendNotificationToUser(executor.getUser().getUsername(), message, destination); break;
            case EMAIL: emailService.send(executor.getUser().getEmail(), "Дедлайн проекта", message); break;
        }

        notificationService.saveNotification(project,
                typeNotification, periodNotification, executor.getUser(), LocalDateTime.now(), deliveryMethod);
    }

    private String generateMessage(String projectName,  NotificationType notificationType, String periodNotification) {
        String message = String.format("До дедлайна проекта " + projectName + " осталось " + periodNotification + " ");


        if (Integer.parseInt(periodNotification) % 100 / 10 == 1) {
            return switch (notificationType) {
                case DEADLINE_DAYS -> message + "дней";
                case DEADLINE_HOURS -> message + "часов";
                default -> "";
            };
        }

        return switch (Integer.parseInt(periodNotification) % 10) {
            case 1 -> switch (notificationType) {
                case DEADLINE_DAYS -> message + "день";
                case DEADLINE_HOURS -> message + "час";
                default -> "";
            };
            case 2, 4, 3 -> switch (notificationType) {
                case DEADLINE_DAYS -> message + "дня";
                case DEADLINE_HOURS -> message + "часа";
                default -> "";
            };
            default -> switch (notificationType) {
                case DEADLINE_DAYS -> message + "дней";
                case DEADLINE_HOURS -> message + "часов";
                default -> "";
            };
        };
    }
}
