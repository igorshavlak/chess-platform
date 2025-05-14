package com.absolute.chessplatform.gamemanagementservice.clients;


import com.absolute.chessplatform.gamemanagementservice.dtos.PlayerInfoDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.PlayerMessageDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.SimulSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Component
@FeignClient(name = "notification-service", url = "${notification-service.url}/api/notifications")
public interface NotificationServiceClient {
    @PostMapping("/sendSimulLobby")
    ResponseEntity<String> sendSimulLobby(@RequestBody SimulSessionDTO simulSessionDTO);

    @PostMapping("/sendSimulLobbyMessage/{lobbyId}")
    ResponseEntity<String> sendSimulLobbyMessage(@RequestBody PlayerMessageDTO playerMessageDTO, @PathVariable UUID lobbyId);

    @PostMapping("/sendSimulLobbyPlayer/{lobbyId}")
    ResponseEntity<String> sendSimulLobbyPlayer(@RequestBody PlayerInfoDTO playerInfo, @PathVariable UUID lobbyId);

    //    @PostMapping("/removeSimulPlayerFromConfirms/{lobbyId}")
//    public ResponseEntity<String> removePlayerFromConfirms(@RequestParam UUID playerId, @PathVariable UUID lobbyId) {
//
//    }
    @PostMapping("/confirmSimulPlayer/{lobbyId}")
    ResponseEntity<String> confirmSimulPlayer(@PathVariable UUID lobbyId, @RequestBody PlayerInfoDTO playerInfoDTO);

    @PostMapping("/startSimulLobby/{playerId}")
    ResponseEntity<String> startSimulLobby(@PathVariable UUID playerId, @RequestBody UUID gameId);
}
