package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "project_notification_settings")
@RequiredArgsConstructor
public class ProjectNotificationSettings {

    @EmbeddedId
    private UserProjectId id;

    @Column(name = "notification_enabled")
    private boolean notificationEnabled = true;

    @Column(name = "notification_type")
    private String notificationType;

    public ProjectNotificationSettings(Long projectId, Long userId) {
        id = new UserProjectId(projectId, userId);
        notificationEnabled = true;
    }
}
