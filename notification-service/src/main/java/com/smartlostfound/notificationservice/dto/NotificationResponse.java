package com.smartlostfound.notificationservice.dto;

import com.smartlostfound.notificationservice.entity.Notification.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
}