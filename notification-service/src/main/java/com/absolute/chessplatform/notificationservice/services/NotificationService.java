package com.absolute.chessplatform.notificationservice.services;

import com.absolute.chessplatform.notificationservice.dtos.PlayerInfoDTO;
import com.absolute.chessplatform.notificationservice.dtos.PlayerMessageDTO;
import com.absolute.chessplatform.notificationservice.dtos.SimulSessionDTO;
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
    public void sendCreatedSimulLobby(SimulSessionDTO simulSessionDTO) {
        messagingTemplate.convertAndSend("/topic/simul/simulLobby", simulSessionDTO);
    }
    public void sendSimulLobbyMessage(PlayerMessageDTO playerMessageDTO, UUID lobbyId) {
        messagingTemplate.convertAndSend("/topic/simul/lobby/" + lobbyId + "/message", playerMessageDTO);
    }
    public void sendJoinedSimulLobbyPlayer(PlayerInfoDTO playerInfoDTO, UUID lobbyId){
        messagingTemplate.convertAndSend("/topic/simul/lobby/" + lobbyId + "/newPlayer", playerInfoDTO);
    }

}
