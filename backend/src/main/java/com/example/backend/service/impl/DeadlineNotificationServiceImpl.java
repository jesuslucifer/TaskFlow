package com.example.backend.service.impl;

import com.example.backend.model.Project;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.DeadlineNotificationService;
import com.example.backend.service.NotificationService;
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
                notificationService.sendNotificationToUser(
                        project.getCreateUser().getUsername(),
                        message,
                        destination);
                notificationService.saveNotification(project,
                        typeNotification,
                        periodNotification,
                        project.getCreateUser(),
                        LocalDateTime.now());
                project.getExecutors().forEach(executor -> {
                    notificationService.sendNotificationToUser(
                            executor.getUser().getUsername(),
                            message,
                            destination);
                    notificationService.saveNotification(project,
                            typeNotification,
                            periodNotification,
                            executor.getUser(),
                            LocalDateTime.now());
                });
            }
        });
    }
}
