package com.absolute.chessplatform.matchmakingservice.repositories;

import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;

import java.util.Optional;
import java.util.UUID;

public interface QueueUnrankedRepository {
    void addToQueue(String key, QueueEntry entry);
    Optional<QueueEntry> popFirst(String key);
    void removeFromQueue(String key, UUID uuid);

}
