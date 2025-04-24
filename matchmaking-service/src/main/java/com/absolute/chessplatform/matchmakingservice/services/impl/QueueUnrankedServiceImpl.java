package com.absolute.chessplatform.matchmakingservice.services.impl;

import com.absolute.chessplatform.matchmakingservice.clients.GameManagementClient;
import com.absolute.chessplatform.matchmakingservice.clients.NotificationServiceClient;
import com.absolute.chessplatform.matchmakingservice.entities.*;
import com.absolute.chessplatform.matchmakingservice.repositories.QueueUnrankedRepository;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service("unrankedService")
@RequiredArgsConstructor
@Slf4j
public class QueueUnrankedServiceImpl implements MatchmakingService {
    private final QueueUnrankedRepository queueUnrankedRepository;
    private final GameManagementClient gameManagementClient;
    private final NotificationServiceClient notificationServiceClient;

    public void enqueuePlayer(String key, QueueEntry queueEntry) {
        queueUnrankedRepository.addToQueue(key, queueEntry);
    }

    public void dequeuePlayer(String key, UUID uuid) {
        queueUnrankedRepository.removeFromQueue(key, uuid);
    }

    public Optional<CreateGameRequest> findMatchInQueue(String key) {
        Optional<QueueEntry> firstOpt = queueUnrankedRepository.popFirst(key);
        if (firstOpt.isEmpty()) {
            return Optional.empty();
        }
        Optional<QueueEntry> secondOpt = queueUnrankedRepository.popFirst(key);
        if (secondOpt.isEmpty()) {
            queueUnrankedRepository.addToQueue(key, firstOpt.get());
            return Optional.empty();
        }

        boolean isFirstWhite = ThreadLocalRandom.current().nextBoolean();

        QueueEntry whitePlayer = isFirstWhite ? firstOpt.get() : secondOpt.get();
        QueueEntry blackPlayer = isFirstWhite ? secondOpt.get() : firstOpt.get();
        CreateGameRequest match = new CreateGameRequest(UUID.randomUUID(), whitePlayer.getUserId(), blackPlayer.getUserId());
        UUID gameId = gameManagementClient.createGame(match).getBody();
        notificationServiceClient.sendGameFoundNotification(new GameFoundNotificationRequest(gameId,match.getWhitePlayerId(),match.getBlackPlayerId(), extractGameMode(key), extractTimeControl(key),false));
        log.info("[{}] Unranked Match created: {} vs {}", key, whitePlayer.getUserId(), blackPlayer.getUserId());
        return Optional.of(match);
    }
    private GameMode extractGameMode(String key) {
        String gameMode =  key.split(":")[1].toUpperCase();
        return GameMode.valueOf(gameMode);
    }
    private String extractTimeControl(String key) {
        return key.split(":")[2].toUpperCase();
    }
}
