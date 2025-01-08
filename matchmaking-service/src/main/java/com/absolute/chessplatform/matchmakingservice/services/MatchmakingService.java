package com.absolute.chessplatform.matchmakingservice.services;


import com.absolute.chessplatform.matchmakingservice.entities.Match;

import java.util.Optional;
import java.util.UUID;

public interface MatchmakingService {
     void enqueuePlayer(UUID userId, int rating);
     void dequeuePlayer(UUID userId);
     Optional<Match> findMatch();
     void processQueue();
}
