package com.example.backend.service;

import com.example.backend.model.User;

public interface UserNotificationSettingsService {

    void createUserSettings(User user);

    boolean notificationIsEnabled(Long userId);

    void disableEnableNotification(Long userId);

}
