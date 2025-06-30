package com.example.backend.service.impl;

import com.example.backend.model.NotificationHistory;
import com.example.backend.model.Project;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.DeadlineNotificationService;
import com.example.backend.service.NotificationHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadlineNotificationServiceImpl implements DeadlineNotificationService {
    private final ProjectRepository projectRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationHistoryService notificationHistoryService;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void checkDeadlines() {
        checkDeadlinesOfDays(3);
        checkDeadlinesOfDays(1);
        checkDeadlinesOfHours(1);
    }


    private void checkDeadlinesOfDays(int days) {
        LocalDate dateNow = LocalDate.now();
        String typeNotification = "deadline_days";

        LocalDate dateTomorrow = dateNow.plusDays(days);

        List<Project> projects = projectRepository.findAllByDateToIs(dateTomorrow);

        sendNotification(projects, typeNotification,  String.valueOf(days));
    }

    private void checkDeadlinesOfHours(int hours) {
        LocalDate dateNow = LocalDate.now();
        LocalTime now = LocalTime.now();
        String typeNotification = "deadline_hours";

        List<Project> projects = projectRepository.findAllByDateToAndTimeLeftBetween(dateNow, now, now.plusHours(hours));

        sendNotification(projects, typeNotification,  String.valueOf(hours));
    }

    private void sendNotification(List<Project> projects, String typeNotification, String periodNotification) {
        projects.forEach(project -> {
            if (!notificationHistoryRepository.existsByProjectIdAndTypeNotificationAndPeriodNotification(
                    project.getId(),
                    typeNotification,
                    periodNotification)) {
                String message = String.format("Скоро дедлайн у проекта %s", project.getName());
                sendNotificationToUser(project.getCreateUser().getUsername(), message);
                project.getExecutors().forEach(executor -> {
                    sendNotificationToUser(executor.getUser().getUsername(), message);
                });
                notificationHistoryService.save(NotificationHistory.builder()
                        .project(project)
                        .typeNotification(typeNotification)
                        .periodNotification(periodNotification)
                        .build());
            }
        });
    }

    private void sendNotificationToUser(String userName, String message) {
        try {
            messagingTemplate.convertAndSendToUser(userName,
                    "/queue/deadline-notification",
                    message);
            log.info("Уведомление отправлено пользователю {}: {}", userName, message);
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления пользователю {}", userName, e);
        }
    }
}
