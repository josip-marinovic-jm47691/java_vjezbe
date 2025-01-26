package com.josip.vjezbe.controllers;

import com.josip.vjezbe.entities.Notification;
import com.josip.vjezbe.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Notification>> getNotificationsForMember(@PathVariable Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/send-test-notification")
    public ResponseEntity<String> sendTestNotification(@RequestParam Long memberId) {
        Notification notification = new Notification();
        notification.setMessage("Test notification message");
        notification.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/notifications/" + memberId, notification);
        return ResponseEntity.ok("Test notification sent.");
    }
}
