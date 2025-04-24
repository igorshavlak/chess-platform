package com.absolute.chessplatform.matchmakingservice.controllers;

import com.absolute.chessplatform.matchmakingservice.entities.EnqueueRequest;
import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final MatchmakingService rankedService;
    private final MatchmakingService unrankedService;

    public MatchmakingController(
            @Qualifier("rankedService") MatchmakingService rankedService,
            @Qualifier("unrankedService") MatchmakingService unrankedService
    ) {
        this.rankedService = rankedService;
        this.unrankedService = unrankedService;
    }

    @PostMapping("/enqueue")
    public ResponseEntity<String> enqueue(@RequestBody EnqueueRequest enqueueRequest) {
        if (enqueueRequest.isRanked()) {
            if (enqueueRequest.getRating() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            QueueEntry queueEntry = QueueEntry.builder()
                    .rating(enqueueRequest.getRating())
                    .userId(enqueueRequest.getUserId())
                    .build();
            rankedService.enqueuePlayer(enqueueRequest.getQueueKey(), queueEntry);

        } else {
            QueueEntry queueEntry = QueueEntry.builder()
                    .userId(enqueueRequest.getUserId())
                    .build();
            unrankedService.enqueuePlayer(enqueueRequest.getQueueKey(), queueEntry);
        }
        return ResponseEntity.ok("User " + enqueueRequest.getUserId() + " joined unranked queue " + enqueueRequest.getQueueKey());
    }

    @PostMapping("/dequeue")
    public ResponseEntity<String> dequeue(@RequestParam String queueKey,
                                          @RequestParam UUID userId) {
        unrankedService.dequeuePlayer(queueKey, userId);
        return ResponseEntity.ok("User " + userId + " left unranked queue " + queueKey);
    }
}
