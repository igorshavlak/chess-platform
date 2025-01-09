package com.absolute.chessplatform.matchmakingservice.clients;

import com.absolute.chessplatform.matchmakingservice.entities.CreateGameRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "game-management-service", url = "${game.management.service.url}")
public interface GameManagementClient {
    @PostMapping("/api/games/create")
    ResponseEntity<String> createGame(CreateGameRequest createGameRequest);
}
