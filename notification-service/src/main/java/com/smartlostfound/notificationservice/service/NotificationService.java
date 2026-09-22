package com.smartlostfound.notificationservice.service;

import com.smartlostfound.notificationservice.dto.NotificationRequest;
import com.smartlostfound.notificationservice.dto.NotificationResponse;
import com.smartlostfound.notificationservice.entity.Notification;
import com.smartlostfound.notificationservice.repository.NotificationRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    // ============================================================
    // CREATE NOTIFICATION
    // ============================================================

    public NotificationResponse createNotification(
            NotificationRequest request) {

        Notification notification = new Notification();

        notification.setUserId(request.getUserId());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved =
                notificationRepository.save(notification);

        return toResponse(saved);
    }

    // ============================================================
    // GET ALL NOTIFICATIONS FOR USER
    // ============================================================

    public List<NotificationResponse> getUserNotifications(
            Long userId) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // GET UNREAD NOTIFICATIONS
    // ============================================================

    public List<NotificationResponse> getUnreadNotifications(
            Long userId) {

        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(
                        userId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // MARK NOTIFICATION AS READ
    // ============================================================

    public NotificationResponse markAsRead(Long id) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        notification.setRead(true);

        Notification updated =
                notificationRepository.save(notification);

        return toResponse(updated);
    }

    // ============================================================
    // DELETE NOTIFICATION
    // ============================================================

    public void deleteNotification(Long id) {

        if (!notificationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Notification not found"
            );
        }

        notificationRepository.deleteById(id);
    }

    // ============================================================
    // ENTITY → RESPONSE DTO
    // ============================================================

    private NotificationResponse toResponse(
            Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}