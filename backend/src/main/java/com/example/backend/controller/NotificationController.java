package com.example.backend.controller;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.Project;
import com.example.backend.model.User;
import com.example.backend.security.SecurityUtil;
import com.example.backend.service.NotificationHistoryService;
import com.example.backend.service.ProjectNotificationSettingsService;
import com.example.backend.service.UserNotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationHistoryService notificationHistoryService;
    private final UserNotificationSettingsService userNotificationSettingsService;
    private final ProjectNotificationSettingsService projectNotificationSettingsService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationHistoryService.getNotificationsForUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationHistoryService.deleteNotificationHistory(id);

        return ResponseEntity.ok("Уведомление удалено");
    }

    @PutMapping("/global_push")
    public ResponseEntity<?> disableEnableGlobalPushNotification() {
        return disableEnableGlobalNotification(DeliveryMethod.PUSH);
    }

    @PutMapping("/global_email")
    public ResponseEntity<?> disableEnableGlobalEmailNotification() {
        return disableEnableGlobalNotification(DeliveryMethod.EMAIL);
    }

    @PutMapping("/{projectId}/push")
    public ResponseEntity<?> disableEnableProjectPushNotification(@PathVariable Long projectId) {
        return disableEnableProjectNotification(projectId, DeliveryMethod.PUSH);
    }

    @PutMapping("/{projectId}/email")
    public ResponseEntity<?> disableEnableProjectEmailNotification(@PathVariable Long projectId) {
         return disableEnableProjectNotification(projectId, DeliveryMethod.EMAIL);
    }

    private ResponseEntity<String> disableEnableProjectNotification(Long projectId, DeliveryMethod deliveryMethod) {
        User user = SecurityUtil.getCurrentUser();
        projectNotificationSettingsService.disableEnableNotification(projectId, user.getId(), deliveryMethod);

        return projectNotificationSettingsService.notificationIsEnabled(projectId, user.getId(), deliveryMethod) ?
                ResponseEntity.ok("Уведомления включены") :
                ResponseEntity.ok("Уведомления отключены");
    }

    private ResponseEntity<String> disableEnableGlobalNotification(DeliveryMethod deliveryMethod) {
        User user = SecurityUtil.getCurrentUser();
        userNotificationSettingsService.disableEnableNotification(user.getId(), deliveryMethod);

        return userNotificationSettingsService.notificationIsEnabled(user.getId(), deliveryMethod) ?
                ResponseEntity.ok("Уведомления включены") :
                ResponseEntity.ok("Уведомления отключены");
    }
}
