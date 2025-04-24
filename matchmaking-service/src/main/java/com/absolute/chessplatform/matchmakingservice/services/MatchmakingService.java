package com.absolute.chessplatform.matchmakingservice.services;


import com.absolute.chessplatform.matchmakingservice.entities.CreateGameRequest;
import com.absolute.chessplatform.matchmakingservice.entities.Match;
import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;

import java.util.Optional;
import java.util.UUID;

public interface MatchmakingService {
     void enqueuePlayer(String key, QueueEntry queueEntry);
     void dequeuePlayer(String key, UUID userId);
     Optional<CreateGameRequest> findMatchInQueue(String key);

}
