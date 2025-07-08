package com.example.backend.service;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.User;

public interface UserNotificationSettingsService {

    void createUserSettings(User user, DeliveryMethod deliveryMethod);

    boolean notificationIsEnabled(Long userId, DeliveryMethod deliveryMethod);

    void disableEnableNotification(Long userId, DeliveryMethod deliveryMethod);

}
