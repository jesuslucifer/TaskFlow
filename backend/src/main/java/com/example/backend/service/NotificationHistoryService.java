package com.example.backend.service;

import com.example.backend.dto.response.NotificationHistoryDto;
import com.example.backend.model.NotificationHistory;

import java.util.List;

public interface NotificationHistoryService {
    NotificationHistory save(NotificationHistory notificationHistory);
    List<NotificationHistoryDto> getNotificationsForUser(Long id);
    void deleteNotificationHistory(Long id);
    void deleteNotificationByProjectIdAndUserId(Long projectId, Long userId);
 }
