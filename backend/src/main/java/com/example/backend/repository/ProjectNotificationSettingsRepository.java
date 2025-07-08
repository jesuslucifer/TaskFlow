package com.example.backend.repository;

import com.example.backend.model.DeliveryMethod;
import com.example.backend.model.ProjectNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectNotificationSettingsRepository extends JpaRepository<ProjectNotificationSettings, Long> {
    ProjectNotificationSettings findByUserProjectId_ProjectIdAndUserProjectId_UserIdAndDeliveryMethod(
            Long projectId,
            Long userid,
            DeliveryMethod deliveryMethod);
}
