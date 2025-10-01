package com.tcpl.billing.service;

import com.tcpl.billing.model.Notification;
import com.tcpl.billing.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    // Create notification
    public Notification createNotification(Notification notification) {
        return repository.save(notification);
    }

    // Get all notifications
    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

    // Get notification by ID
    public Optional<Notification> getNotificationById(Long id) {
        return repository.findById(id);
    }

    // Update notification read status
    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        return repository.save(notification);
    }

    // Delete notification
    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }

    // Get unread notifications
    public List<Notification> getUnreadNotifications() {
        return repository.findByReadStatus(false);
    }
}
