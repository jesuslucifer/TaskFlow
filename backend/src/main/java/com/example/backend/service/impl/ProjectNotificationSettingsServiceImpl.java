package com.example.backend.service.impl;

import com.example.backend.model.DeliveryMethod;
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
    public void createProjectNotificationSettings(Project project, User user, DeliveryMethod deliveryMethod) {
        projectNotificationSettingsRepository.save(new ProjectNotificationSettings(project.getId(), user.getId(), deliveryMethod));
    }

    @Override
    public boolean notificationIsEnabled(Long projectId, Long userId, DeliveryMethod deliveryMethod) {
        return projectNotificationSettingsRepository
                .findByUserProjectId_ProjectIdAndUserProjectId_UserIdAndDeliveryMethod(
                        projectId, userId, deliveryMethod)
                .isNotificationEnabled();
    }

    @Override
    public void disableEnableNotification(Long projectId, Long userId, DeliveryMethod deliveryMethod) {
        ProjectNotificationSettings projectNotificationSettings = projectNotificationSettingsRepository
                .findByUserProjectId_ProjectIdAndUserProjectId_UserIdAndDeliveryMethod(projectId, userId, deliveryMethod);

        projectNotificationSettings.setNotificationEnabled(
                !projectNotificationSettings.isNotificationEnabled());

        projectNotificationSettingsRepository.save(projectNotificationSettings);
    }

    @Override
    public void deleteProjectNotificationSettings(Long projectId, Long userId, DeliveryMethod deliveryMethod) {
        ProjectNotificationSettings projectNotificationSettings = projectNotificationSettingsRepository
                .findByUserProjectId_ProjectIdAndUserProjectId_UserIdAndDeliveryMethod(projectId, userId, deliveryMethod);

        projectNotificationSettingsRepository.delete(projectNotificationSettings);
    }
}
