package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "project_notification_settings")
@RequiredArgsConstructor
public class ProjectNotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserProjectId userProjectId;

    @Column(name = "notification_enabled")
    private boolean notificationEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    })
    private ProjectExecutor projectExecutor;

    public ProjectNotificationSettings(Long projectId, Long userId, DeliveryMethod deliveryMethod) {
        userProjectId = new UserProjectId(projectId, userId);
        notificationEnabled = true;
        this.deliveryMethod = deliveryMethod;
    }
}
