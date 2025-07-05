package com.example.backend.repository;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.UserNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSettings, Long> {
    UserNotificationSettings findByUserIdAndDeliveryMethod(Long userId, DeliveryMethod deliveryMethod);
}
