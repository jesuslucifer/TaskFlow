package com.example.backend.repository;

import com.example.backend.model.ProjectNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectNotificationSettingsRepository extends JpaRepository<ProjectNotificationSettings, Long> {
    ProjectNotificationSettings findById_ProjectIdAndId_UserId(Long projectId, Long userid);
}
