package com.example.backend.service.impl;

import com.example.backend.model.NotificationHistory;
import com.example.backend.repository.NotificationHistoryRepository;
import com.example.backend.service.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationHistoryServiceImpl implements NotificationHistoryService {
    private final NotificationHistoryRepository notificationHistoryRepository;

    @Override
    public NotificationHistory save(NotificationHistory notificationHistory) {
        return notificationHistoryRepository.save(notificationHistory);
    }
}
