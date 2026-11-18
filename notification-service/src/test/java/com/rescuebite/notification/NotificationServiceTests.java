package com.rescuebite.notification;

import com.rescuebite.notification.dto.CreateNotificationRequest;
import com.rescuebite.notification.model.Notification;
import com.rescuebite.notification.model.NotificationStatus;
import com.rescuebite.notification.model.NotificationType;
import com.rescuebite.notification.repository.NotificationRepository;
import com.rescuebite.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class NotificationServiceTests {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("Should create unread notification successfully")
    void testCreateNotification() {
        Notification saved = new Notification(
                "usr-jesa-001", "Order Confirmed", "Your order has been paid.", NotificationType.ORDER
        );
        saved.setId("notif-jesa-001");

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        CreateNotificationRequest req = new CreateNotificationRequest(
                "usr-jesa-001", "Order Confirmed", "Your order has been paid.", NotificationType.ORDER
        );

        var response = notificationService.createNotification(req);

        assertNotNull(response);
        assertEquals("Order Confirmed", response.getTitle());
        assertEquals(NotificationStatus.UNREAD, response.getStatus());
    }

    @Test
    @DisplayName("Should mark notification as read")
    void testMarkAsRead() {
        Notification notification = new Notification(
                "usr-jesa-001", "Pickup Ready", "Use code 849201", NotificationType.ORDER
        );
        notification.setId("notif-jesa-002");
        notification.setStatus(NotificationStatus.UNREAD);

        when(notificationRepository.findById("notif-jesa-002")).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        var response = notificationService.markAsRead("notif-jesa-002");

        assertEquals(NotificationStatus.READ, response.getStatus());
    }
}
