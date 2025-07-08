package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "user_notification_settings")
@RequiredArgsConstructor
public class UserNotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "global_notification_enabled")
    private boolean globalNotificationEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;

    public UserNotificationSettings(Long userId, DeliveryMethod deliveryMethod) {
        this.userId = userId;
        this.deliveryMethod = deliveryMethod;
    }
}
