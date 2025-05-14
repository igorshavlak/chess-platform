package com.absolute.chessplatform.notificationservice.controllers;


import com.absolute.chessplatform.notificationservice.dtos.PlayerInfo;
import com.absolute.chessplatform.notificationservice.dtos.PlayerInfoDTO;
import com.absolute.chessplatform.notificationservice.dtos.PlayerMessageDTO;
import com.absolute.chessplatform.notificationservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.notificationservice.entities.GameFoundNotificationRequest;
import com.absolute.chessplatform.notificationservice.entities.NotificationRequest;
import com.absolute.chessplatform.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/sendSimulLobby")
    public ResponseEntity<String> sendSimulLobby(@RequestBody SimulSessionDTO simulSessionDTO) {
        notificationService.sendCreatedSimulLobby(simulSessionDTO);
        return ResponseEntity.ok("Simul lobby was send");
    }
    @PostMapping("/sendSimulLobbyMessage/{lobbyId}")
    public ResponseEntity<String> sendSimulLobbyMessage(@RequestBody PlayerMessageDTO playerMessageDTO, @PathVariable UUID lobbyId) {
        notificationService.sendSimulLobbyMessage(playerMessageDTO, lobbyId);
        return ResponseEntity.ok("Player message was send");
    }
    @PostMapping("/sendSimulLobbyPlayer/{lobbyId}")
    public ResponseEntity<String> sendSimulLobbyPlayer(@RequestBody PlayerInfoDTO playerInfo, @PathVariable UUID lobbyId){
        notificationService.sendJoinedSimulLobbyPlayer(playerInfo, lobbyId);
        return ResponseEntity.ok("Player was joined");
    }
    @PostMapping("/startSimulLobby/{playerId}")
    public ResponseEntity<String> startSimulLobby(@PathVariable UUID playerId, @RequestBody UUID gameId) {
        notificationService.sendStartSimulLobby(playerId, gameId);
        return ResponseEntity.ok("Message was sent ");
    }
//    @PostMapping("/removeSimulPlayerFromConfirms/{lobbyId}")
//    public ResponseEntity<String> removePlayerFromConfirms(@RequestParam UUID playerId, @PathVariable UUID lobbyId) {
//
//    }
    @PostMapping("/confirmSimulPlayer/{lobbyId}")
    public ResponseEntity<String> confirmSimulPlayer(@PathVariable UUID lobbyId, @RequestBody PlayerInfoDTO playerInfoDTO) {
        notificationService.sendConfirmedSimulLobbyPlayer(playerInfoDTO, lobbyId);
        return ResponseEntity.ok("Player was confirmed");
    }

}
