package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "user_notification_settings")
@RequiredArgsConstructor
public class UserNotificationSettings {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "global_notification_enabled")
    private boolean globalNotificationEnabled = true;

    public UserNotificationSettings(Long userId) {
        this.userId = userId;
    }
}
