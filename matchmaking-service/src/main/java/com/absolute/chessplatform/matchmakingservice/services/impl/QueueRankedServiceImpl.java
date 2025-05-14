package com.absolute.chessplatform.matchmakingservice.services.impl;

import com.absolute.chessplatform.matchmakingservice.entities.CreateGameRequest;
import com.absolute.chessplatform.matchmakingservice.entities.Match;
import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
import com.absolute.chessplatform.matchmakingservice.repositories.QueueRankedRepository;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("rankedService")
@AllArgsConstructor
@Slf4j
public class QueueRankedServiceImpl implements MatchmakingService {

    private final QueueRankedRepository queueRankedRepository;
    //private final GameManagementClient gameManagementClient;

    private static final int INITIAL_TOLERANCE = 100;
    private static final int TOLERANCE_INCREMENT = 50;
    private static final int TOLERANCE_INTERVAL_SECONDS = 30;
    private static final int MAX_TOLERANCE = 500;

    public void enqueuePlayer(String key, QueueEntry queueEntry) {
        Optional<QueueEntry> existing = queueRankedRepository.getQueueEntry(key, queueEntry.getUserId());
        if (existing.isPresent()) {
            throw new IllegalStateException("User is already in the queue");
        }
        queueRankedRepository.addToQueue(key,queueEntry);
    }

    public void dequeuePlayer(String key, UUID userId) {
        queueRankedRepository.removeFromQueue(key, userId);
    }

    public Optional<CreateGameRequest> findMatchInQueue(String key) {

        Set<Object> allPlayers = queueRankedRepository.getAllPlayers(key);

        for (Object obj : allPlayers) {
            UUID playerOneId = UUID.fromString((String) obj);
            Optional<QueueEntry> playerOneOpt = queueRankedRepository.getQueueEntry(key,playerOneId);
            if (playerOneOpt.isEmpty()) {
                log.warn("Player {} not found in queue during match finding", playerOneId);
                continue;
            }

            QueueEntry playerOne = playerOneOpt.get();

            long currentTime = Instant.now().getEpochSecond();
            long timeInQueue = currentTime - playerOne.getTimestamp();
            int dynamicTolerance = calculateDynamicTolerance(timeInQueue);

            double minRating = playerOne.getRating() - dynamicTolerance;
            double maxRating = playerOne.getRating() + dynamicTolerance;

            Set<Object> potentialOpponents = queueRankedRepository.getPlayersInRange(key,minRating, maxRating);
            for (Object opponentObj : potentialOpponents) {

                UUID playerTwoId = UUID.fromString((String) opponentObj);
                if (playerTwoId.equals(playerOneId)) continue;

                Optional<QueueEntry> playerTwoOpt = queueRankedRepository.getQueueEntry(key,playerTwoId);
                if (playerTwoOpt.isEmpty()) {
                    log.warn("Player {} not found in queue during match finding", playerTwoId);
                    continue;
                }

                QueueEntry playerTwo = playerTwoOpt.get();

                long timeInQueueTwo = currentTime - playerTwo.getTimestamp();
                int dynamicToleranceTwo = calculateDynamicTolerance(timeInQueueTwo);

                if (playerTwo.getRating() >= (playerOne.getRating() - dynamicTolerance) &&
                        playerTwo.getRating() <= (playerOne.getRating() + dynamicTolerance)) {

                    //CreateGameRequest match = new CreateGameRequest(UUID.randomUUID(), playerOneId, playerTwoId);
                    CreateGameRequest match = null;
                    log.info("Match {} created between {} and {}", match.getGameId(), playerOneId, playerTwoId);


                    queueRankedRepository.removeFromQueue(key,playerOneId);
                    queueRankedRepository.removeFromQueue(key,playerTwoId);
                    return Optional.of(match);
                }
            }
        }

        log.debug("No match found during this iteration");
        return Optional.empty();
    }

    private int calculateDynamicTolerance(long timeInQueueSeconds) {
        int increments = (int) (timeInQueueSeconds / TOLERANCE_INTERVAL_SECONDS);
        int dynamicTolerance = INITIAL_TOLERANCE + (increments * TOLERANCE_INCREMENT);
        return Math.min(dynamicTolerance, MAX_TOLERANCE);
    }


}
