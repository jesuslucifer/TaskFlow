package com.example.backend.service.impl;

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
    public void createUserSettings(User user) {
        userNotificationSettingsRepository.save(new UserNotificationSettings(user.getId()));
    }

    @Override
    public boolean notificationIsEnabled(Long userId) {
        return userNotificationSettingsRepository
                .findByUserId(userId)
                .isGlobalNotificationEnabled();
    }

    @Override
    public void disableNotification(Long userId) {
        UserNotificationSettings userNotificationSettings = userNotificationSettingsRepository
                .findByUserId(userId);

        userNotificationSettings.setGlobalNotificationEnabled(false);

        userNotificationSettingsRepository.save(userNotificationSettings);
    }
}
