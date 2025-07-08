package com.example.backend.service.impl;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.User;
import com.example.backend.model.UserNotificationSettings;
import com.example.backend.repository.UserNotificationSettingsRepository;
import com.example.backend.service.UserNotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNotificationSettingsServiceImpl implements UserNotificationSettingsService {
    private final UserNotificationSettingsRepository userNotificationSettingsRepository;

    @Override
    public void createUserSettings(User user, DeliveryMethod deliveryMethod) {
        userNotificationSettingsRepository.save(new UserNotificationSettings(user.getId(), deliveryMethod));
    }

    @Override
    public boolean notificationIsEnabled(Long userId, DeliveryMethod deliveryMethod) {
        return userNotificationSettingsRepository
                .findByUserIdAndDeliveryMethod(userId, deliveryMethod)
                .isGlobalNotificationEnabled();
    }

    @Override
    public void disableEnableNotification(Long userId, DeliveryMethod deliveryMethod) {
        UserNotificationSettings userNotificationSettings = userNotificationSettingsRepository
                .findByUserIdAndDeliveryMethod(userId, deliveryMethod);

        userNotificationSettings.setGlobalNotificationEnabled(
                !userNotificationSettings.isGlobalNotificationEnabled());

        userNotificationSettingsRepository.save(userNotificationSettings);
    }
}
