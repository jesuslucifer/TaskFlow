package com.example.backend.controller;

import com.example.backend.service.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationHistoryService notificationHistoryService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationHistoryService.getNotificationsForUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationHistoryService.deleteNotificationHistory(id);

        return ResponseEntity.ok("Уведомление удалено");
    }
}
