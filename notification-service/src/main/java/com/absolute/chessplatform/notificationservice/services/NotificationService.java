package com.absolute.chessplatform.notificationservice.services;

import com.absolute.chessplatform.notificationservice.entities.GameFoundNotificationRequest;
import com.absolute.chessplatform.notificationservice.entities.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(UUID userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications" + userId, message);
    }
    public void sendGameFoundNotification(GameFoundNotificationRequest request) {
        messagingTemplate.convertAndSend("/topic/notifications/gameFound/" + request.getWhitePlayerId(), request);
        messagingTemplate.convertAndSend("/topic/notifications/gameFound/" + request.getBlackPlayerId(), request);
    }
}
