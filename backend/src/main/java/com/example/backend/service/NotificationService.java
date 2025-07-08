package com.example.backend.service;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.NotificationType;
import com.example.backend.model.Project;
import com.example.backend.model.User;

import java.time.LocalDateTime;

public interface NotificationService {
    void sendNotificationToUser(String username, String message, String destination);
    void saveNotification(
            Project project, NotificationType typeNotification, String periodNotification, User user,
            LocalDateTime dateTime, DeliveryMethod deliveryMethod);
}
