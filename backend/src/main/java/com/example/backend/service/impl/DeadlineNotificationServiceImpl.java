package com.example.backend.service.impl;

import com.example.backend.model.NotificationType;
import com.example.backend.model.Project;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.DeadlineNotificationService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.ProjectNotificationSettingsService;
import com.example.backend.service.UserNotificationSettingsService;
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
            String message = String.format("Скоро дедлайн у проекта %s", project.getName());
            project.getExecutors().forEach(executor -> {
                if (userNotificationSettingsService.notificationIsEnabled(executor.getId().getUserId())
                        && !notificationHistoryRepository.existsByProjectIdAndTypeNotificationAndPeriodNotificationAndUserId(
                        project.getId(),
                        typeNotification,
                        periodNotification,
                        executor.getId().getUserId())
                        && projectNotificationSettingsService
                        .notificationIsEnabled(project.getId(), executor.getId().getUserId()
                )) {
                    notificationService.sendNotificationToUser(
                            executor.getUser().getUsername(),
                            message,
                            destination);
                    notificationService.saveNotification(project,
                            typeNotification,
                            periodNotification,
                            executor.getUser(),
                            LocalDateTime.now());
                }
            });
        });
    }
}
