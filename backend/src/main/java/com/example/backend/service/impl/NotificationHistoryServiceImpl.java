package com.example.backend.service.impl;

import com.example.backend.dto.response.NotificationHistoryDto;
import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.NotificationHistory;
import com.example.backend.model.NotificationType;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.service.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        List<NotificationHistory> notificationHistories = notificationHistoryRepository.findAllByUserIdAndDeliveryMethodOrderByDateTimeDesc(id, DeliveryMethod.PUSH);
        List<NotificationHistoryDto> notificationHistoryDto = new ArrayList<>();

        notificationHistories.forEach(notificationHistory -> {
            if (notificationHistory.getTypeNotification().equals(NotificationType.DEADLINE_DAYS)) {
                String message = "Скоро дедлайн у проекта " + notificationHistory.getProject().getName();
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()));
            }
            if (notificationHistory.getTypeNotification().equals(NotificationType.DEADLINE_HOURS)) {
                String message = "Скоро дедлайн у проекта " + notificationHistory.getProject().getName();
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()));
            }
            if (notificationHistory.getTypeNotification().equals(NotificationType.ADD_EXECUTOR)) {
                String message = "Вас назначили исполнителем в проекте " + notificationHistory.getProject().getName();
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()
                ));
            }
            if (notificationHistory.getTypeNotification().equals(NotificationType.DECLINE_EXECUTOR)) {
                String message = " отклонил ваше приглашение в проект " + notificationHistory.getProject().getName();
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()
                ));
            }
            if (notificationHistory.getTypeNotification().equals(NotificationType.ACCEPT_EXECUTOR)) {
                String message = " принял ваше приглашение в проект " + notificationHistory.getProject().getName();
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()
                ));
            }
            if (notificationHistory.getTypeNotification().equals(NotificationType.CHANGE_STATUS)) {
                String message = "Статус проекта " + notificationHistory.getProject().getName() + " был изменен";
                notificationHistoryDto.add(new NotificationHistoryDto(notificationHistory.getId(),
                        message,
                        notificationHistory.getDateTime()
                ));
            }
        });

        return notificationHistoryDto;
    }

    @Override
    public void deleteNotificationHistory(Long id) {
        notificationHistoryRepository.deleteById(id);
    }

    @Override
    public void deleteNotificationByProjectIdAndUserId(Long projectId, Long userId) {
        notificationHistoryRepository.deleteByProjectIdAndUserId(projectId, userId);
    }
}
