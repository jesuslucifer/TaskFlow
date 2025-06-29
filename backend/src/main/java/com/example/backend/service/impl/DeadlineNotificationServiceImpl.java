package com.example.backend.service.impl;

import com.example.backend.model.Project;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.DeadlineNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadlineNotificationServiceImpl implements DeadlineNotificationService {
    private final ProjectRepository projectRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void checkDeadlines() {
        //checkDeadlinesOfDays(3);
        checkDeadlinesOfDays(1);
    }


    private void checkDeadlinesOfDays(int days) {
        LocalDate dateNow = LocalDate.now();

        LocalDate dateTomorrow = dateNow.plusDays(days + 1);

        List<Project> projects = projectRepository.findAllByDateToBetween(dateNow, dateTomorrow);

        projects.forEach(project -> {
            String message = String.format("До дедлайна проекта %s осталось менее %d дня", project.getName(), days);
            messagingTemplate.convertAndSendToUser(project.getCreateUser().getUsername(),
                    "/queue/deadline-notification",
                    message);
            project.getExecutors().forEach(executor -> {
                messagingTemplate.convertAndSendToUser(executor.getUser().getUsername(),
                        "/queue/deadline-notification",
                        message);
            });
        });
    }
}
