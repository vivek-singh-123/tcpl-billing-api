package com.tcpl.billing.controller;

import com.tcpl.billing.model.Notification;
import com.tcpl.billing.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Create notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification saved = service.createNotification(notification);
        return ResponseEntity.ok(saved);
    }

    // Get all notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    // Get single notification
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        return service.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Mark notification as read
    @PostMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification updated = service.markAsRead(id);
        return ResponseEntity.ok(updated);
    }

    // Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        service.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // Get unread notifications
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        return ResponseEntity.ok(service.getUnreadNotifications());
    }
}
