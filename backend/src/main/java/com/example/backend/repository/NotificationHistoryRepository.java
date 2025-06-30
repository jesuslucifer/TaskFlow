package com.example.backend.repository;

import com.example.backend.model.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    boolean existsByProjectIdAndTypeNotificationAndPeriodNotification(Long projectId, String typeNotification, String periodNotification);
    List<NotificationHistory> findAllByUserId(Long user_id);
}
