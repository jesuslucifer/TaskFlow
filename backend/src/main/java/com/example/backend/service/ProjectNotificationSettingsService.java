package com.example.backend.service;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.Project;
import com.example.backend.model.User;

public interface ProjectNotificationSettingsService {
    void createProjectNotificationSettings(Project project, User user, DeliveryMethod type);
    boolean notificationIsEnabled(Long projectId, Long userId, DeliveryMethod type);
    void disableEnableNotification(Long projectId, Long userId, DeliveryMethod type);
}
