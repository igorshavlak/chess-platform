package com.absolute.chessplatform.matchmakingservice.repositories;

import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;

import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public interface QueueRankedRepository {
    void addToQueue(String key,QueueEntry queue);
    void removeFromQueue(String key, UUID userId);
    Optional<QueueEntry> getQueueEntry(String key, UUID userId);
    Set<Object> getPlayersInRange(String key, double minRating, double maxRating);
    Set<Object> getAllPlayers(String key);

}
