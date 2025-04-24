package com.absolute.chessplatform.notificationservice.controllers;


import com.absolute.chessplatform.notificationservice.entities.GameFoundNotificationRequest;
import com.absolute.chessplatform.notificationservice.entities.NotificationRequest;
import com.absolute.chessplatform.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request.getUserId(), request.getMessage());
        return ResponseEntity.ok("Notification sent");
    }
    @PostMapping("/send-game-found/")
    public ResponseEntity<String> sendGameFoundNotification(@RequestBody GameFoundNotificationRequest request) {
        notificationService.sendGameFoundNotification(request);
        return ResponseEntity.ok("Notification sent");

    }
}
