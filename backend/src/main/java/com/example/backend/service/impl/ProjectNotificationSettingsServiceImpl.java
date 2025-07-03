package com.example.backend.service.impl;

import com.example.backend.model.Project;
import com.example.backend.model.ProjectNotificationSettings;
import com.example.backend.model.User;
import com.example.backend.repository.ProjectNotificationSettingsRepository;
import com.example.backend.service.ProjectNotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectNotificationSettingsServiceImpl implements ProjectNotificationSettingsService {
    private final ProjectNotificationSettingsRepository projectNotificationSettingsRepository;

    @Override
    public void createProjectNotificationSettings(Project project, User user) {
        projectNotificationSettingsRepository.save(new ProjectNotificationSettings(project.getId(), user.getId()));
    }

    @Override
    public boolean notificationIsEnabled(Long projectId, Long userId) {
        return projectNotificationSettingsRepository
                .findById_ProjectIdAndId_UserId(projectId, userId)
                .isNotificationEnabled();
    }

    @Override
    public void disableNotification(Long projectId, Long userId) {
        ProjectNotificationSettings projectNotificationSettings = projectNotificationSettingsRepository
                .findById_ProjectIdAndId_UserId(projectId, userId);

        projectNotificationSettings.setNotificationEnabled(false);

        projectNotificationSettingsRepository.save(projectNotificationSettings);
    }
}
