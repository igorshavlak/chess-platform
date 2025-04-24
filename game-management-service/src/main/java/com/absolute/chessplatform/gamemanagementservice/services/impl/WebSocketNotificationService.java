package com.absolute.chessplatform.gamemanagementservice.services.impl;

import com.absolute.chessplatform.gamemanagementservice.entities.ConcludeGameNotification;
import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;
import com.absolute.chessplatform.gamemanagementservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService implements NotificationService {

    private final SimpMessagingTemplate tpl;

    @Override
    public void sendGameConcludedNotification(ConcludeGameNotification notification) {
        tpl.convertAndSend("/topic/game/" + notification.getGameId() + "/conclude", notification);
    }
}
