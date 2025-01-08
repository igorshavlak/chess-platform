package com.absolute.chessplatform.matchmakingservice.controllers;

import com.absolute.chessplatform.matchmakingservice.entities.DequeueRequest;
import com.absolute.chessplatform.matchmakingservice.entities.EnqueueRequest;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matchmaking")
@RequiredArgsConstructor
public class MatchmakingController {
    private final MatchmakingService matchmakingService;

    @PostMapping("/enqueue")
    public ResponseEntity<String> enqueue( @RequestBody EnqueueRequest request) {
        matchmakingService.enqueuePlayer(request.getUserId(), request.getRating());
        return ResponseEntity.status(HttpStatus.OK).body("Enqueued successfully");
    }

    @PostMapping("/dequeue")
    public ResponseEntity<String> dequeue( @RequestBody DequeueRequest request) {
        matchmakingService.dequeuePlayer(request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body("Dequeued successfully");
    }
    @PostConstruct
    public void startMatchmakingProcessor(){
        Thread thread = new Thread(matchmakingService::processQueue);
        thread.setDaemon(true);
        thread.start();
    }
}
