package com.absolute.chessplatform.gamemanagementservice.controllers;
import com.absolute.chessplatform.gamemanagementservice.entities.*;
import com.absolute.chessplatform.gamemanagementservice.services.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")
    public void handleMove(Move moveMessage, Principal principal) {
        if (!(principal instanceof UsernamePasswordAuthenticationToken authToken) ||
                !(authToken.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalArgumentException("Invalid authentication");
        }
                Object principalObj = authToken.getPrincipal();
                String clientId = jwt.getClaim("sub");
                log.debug("Received move: {} in game: {} from client: {}", moveMessage.getMove(), moveMessage.getGameId(), clientId);
                MoveResult result = gameService.makeMove(moveMessage.getGameId(), moveMessage.getMove(), UUID.fromString(clientId));
                MoveResponseDTO response = MoveResponseDTO.builder()
                        .gameId(result.getGameId())
                        .move(result.getMove())
                        .whiteTimeMillis(result.getWhiteTimeMillis())
                        .blackTimeMillis(result.getBlackTimeMillis())
                        .isActivePlayerWhite(result.isActivePlayerIsWhite())
                        .senderId(clientId)
                        .serverTimestampMillis(System.currentTimeMillis())
                        .build();
                messagingTemplate.convertAndSend("/topic/game/" + result.getGameId(), response);


    }
//    @MessageMapping("/concludeGame")
//    public void concludeGame(ConcludeGameRequest concludeGameRequest) {
//        gameService.concludeGame(concludeGameRequest.getGameId(),concludeGameRequest.getGameStatus());
//        messagingTemplate.convertAndSend("/topic/concludeGame/game/" + concludeGameRequest.getGameId(), concludeGameRequest);
//    }
    @PostMapping("/create")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createGame(@RequestBody CreateGameRequest createGameRequest) {
        UUID gameId = gameService.createGame(createGameRequest.getWhitePlayerId(), createGameRequest.getBlackPlayerId());
        return ResponseEntity.ok(gameId);
    }
    @GetMapping("/getGameInfo/{gameId}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<ActiveGame> getGameInfo(@PathVariable UUID gameId) {
        ActiveGame activeGame = gameService.getGameInfo(gameId);
        return ResponseEntity.ok(activeGame);
    }
}
