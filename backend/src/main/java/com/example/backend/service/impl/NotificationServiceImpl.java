package com.example.backend.service.impl;

import com.example.backend.model.*;
import com.example.backend.service.NotificationHistoryService;
import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationHistoryService notificationHistoryService;

    @Override
    public void sendNotificationToUser(String username, String message, String destination) {
        try {
            messagingTemplate.convertAndSendToUser(username,
                    destination,
                    message);
            log.info("[PUSH] : Уведомление отправлено пользователю {}: {}", username, message);
        } catch (Exception e) {
            log.error("[PUSH] : Ошибка при отправке уведомления пользователю {}", username, e);
        }
    }

    @Override
    public void saveNotification(Project project, NotificationType typeNotification, String periodNotification, User user, LocalDateTime dateTime, DeliveryMethod deliveryMethod) {
        notificationHistoryService.save(NotificationHistory.builder()
                .project(project)
                .typeNotification(typeNotification)
                .periodNotification(periodNotification)
                .user(user)
                .dateTime(dateTime)
                .deliveryMethod(deliveryMethod)
                .build());
    }
}
