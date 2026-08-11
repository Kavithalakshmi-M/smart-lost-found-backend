package com.smartlostfound.notificationservice.dto;

import com.smartlostfound.notificationservice.entity.Notification.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotificationRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;
}