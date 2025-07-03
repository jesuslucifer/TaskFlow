package com.example.backend.service;

import com.example.backend.model.Project;
import com.example.backend.model.User;

public interface ProjectNotificationSettingsService {
    void createProjectNotificationSettings(Project project, User user);
    boolean notificationIsEnabled(Long projectId, Long userId);
    void disableNotification(Long projectId, Long userId);
}
