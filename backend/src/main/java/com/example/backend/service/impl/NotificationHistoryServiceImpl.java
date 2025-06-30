package com.example.backend.service.impl;

import com.example.backend.dto.response.NotificationHistoryDto;
import com.example.backend.model.NotificationHistory;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.service.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationHistoryServiceImpl implements NotificationHistoryService {
    private final NotificationHistoryRepository notificationHistoryRepository;

    @Override
    public NotificationHistory save(NotificationHistory notificationHistory) {
        return notificationHistoryRepository.save(notificationHistory);
    }

    @Override
    public List<NotificationHistoryDto> getNotificationsForUser(Long id) {
        List<NotificationHistory> notificationHistories = notificationHistoryRepository.findAllByUserId(id);
        List<NotificationHistoryDto> notificationHistoryDto = new ArrayList<>();

        notificationHistories.forEach(notificationHistory -> {
            String message = "Скоро дедлайн у проекта ";
            if (notificationHistory.getTypeNotification().equals("deadline_days")) {
                message += String.valueOf(notificationHistory.getProject().getName());
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        LocalDateTime.now()));
            }
            if (notificationHistory.getTypeNotification().equals("deadline_hours")) {
                message += String.valueOf(notificationHistory.getProject().getName());
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        LocalDateTime.now()));
            }
        });

        return notificationHistoryDto;
    }

    @Override
    public void deleteNotificationHistory(Long id) {
        notificationHistoryRepository.deleteById(id);
    }
}
