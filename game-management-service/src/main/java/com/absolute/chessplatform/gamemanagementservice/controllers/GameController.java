package com.absolute.chessplatform.gamemanagementservice.controllers;

import com.absolute.chessplatform.gamemanagementservice.entities.CreateGameRequest;
import com.absolute.chessplatform.gamemanagementservice.entities.Move;
import com.absolute.chessplatform.gamemanagementservice.services.GameService;
import com.absolute.chessplatform.gamemanagementservice.services.GameServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")
    public void handleMove(Move moveMessage) {
        log.debug("Received move: {} in game: {}", moveMessage.getMove(), moveMessage.getGameId());
        Move move = gameService.makeMove(moveMessage.getGameId(), moveMessage.getMove());
        messagingTemplate.convertAndSend("/topic/game/" + move.getGameId(), move);
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest createGameRequest) {
        gameService.createGame(createGameRequest.firstPlayerId, createGameRequest.secondPlayerId);
        return ResponseEntity.ok("Game created");
    }
    @PostMapping("/getMoves")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> getMoves(UUID gameId) {
        List<String> moves = gameService.getMoves(gameId);
        return ResponseEntity.ok(moves);
    }
}
