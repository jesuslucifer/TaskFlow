package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.NotificationHistoryService;
import com.example.backend.service.ProjectNotificationSettingsService;
import com.example.backend.service.UserNotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PutMapping("/global")
    public ResponseEntity<?> disableGlobalNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        userNotificationSettingsService.disableNotification(user.getId());

        return ResponseEntity.ok("Уведомления отключены");
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<?> disableProjectNotification(@PathVariable Long projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        projectNotificationSettingsService.disableNotification(projectId, user.getId());

        return ResponseEntity.ok("Уведомления отключены");
    }
}
