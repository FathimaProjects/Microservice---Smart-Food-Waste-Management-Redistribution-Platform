package com.rescuebite.notification.service;

import com.rescuebite.notification.dto.CreateNotificationRequest;
import com.rescuebite.notification.dto.NotificationResponse;
import com.rescuebite.notification.exception.ResourceNotFoundException;
import com.rescuebite.notification.model.Notification;
import com.rescuebite.notification.model.NotificationStatus;
import com.rescuebite.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = new Notification(
                request.getUserId(),
                request.getTitle(),
                request.getMessage(),
                request.getType()
        );

        Notification saved = notificationRepository.save(notification);
        return new NotificationResponse(saved);
    }

    public NotificationResponse getNotificationById(String id) {
        Notification notification = findNotificationById(id);
        return new NotificationResponse(notification);
    }

    public List<NotificationResponse> getNotificationsByUser(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotificationsByUser(String userId) {
        return notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, NotificationStatus.UNREAD).stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.UNREAD);
    }

    public NotificationResponse markAsRead(String id) {
        Notification notification = findNotificationById(id);
        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(LocalDateTime.now());
        Notification updated = notificationRepository.save(notification);
        return new NotificationResponse(updated);
    }

    public void markAllAsRead(String userId) {
        List<Notification> unreadList = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, NotificationStatus.UNREAD);
        LocalDateTime now = LocalDateTime.now();
        unreadList.forEach(n -> {
            n.setStatus(NotificationStatus.READ);
            n.setReadAt(now);
        });
        notificationRepository.saveAll(unreadList);
    }

    public void deleteNotification(String id) {
        Notification notification = findNotificationById(id);
        notificationRepository.delete(notification);
    }

    private Notification findNotificationById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
    }
}

