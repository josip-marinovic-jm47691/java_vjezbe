package com.josip.vjezbe.controllers;

import com.josip.vjezbe.entities.Notification;
import com.josip.vjezbe.repositories.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Notification>> getNotificationsForMember(@PathVariable Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        return ResponseEntity.ok(notifications);
    }
}