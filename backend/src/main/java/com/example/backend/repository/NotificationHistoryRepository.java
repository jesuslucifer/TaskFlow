package com.example.backend.repository;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.NotificationHistory;
import com.example.backend.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    boolean existsByProjectIdAndTypeNotificationAndPeriodNotificationAndUserIdAndDeliveryMethod(
            Long project_id, NotificationType typeNotification, String periodNotification, Long user_id, DeliveryMethod deliveryMethod);
    List<NotificationHistory> findAllByUserIdAndDeliveryMethodOrderByDateTimeDesc(Long user_id, DeliveryMethod deliveryMethod);
    void deleteByProjectIdAndUserId(Long project_id, Long user_id);
}
