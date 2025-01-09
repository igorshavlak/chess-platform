package com.absolute.chessplatform.matchmakingservice.services.impl;

import com.absolute.chessplatform.matchmakingservice.clients.GameManagementClient;
import com.absolute.chessplatform.matchmakingservice.entities.CreateGameRequest;
import com.absolute.chessplatform.matchmakingservice.entities.Match;
import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
import com.absolute.chessplatform.matchmakingservice.repositories.QueueRepository;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MatchmakingServiceImpl implements MatchmakingService {

    private final QueueRepository queueRepository;
    private final GameManagementClient gameManagementClient;

    private static final int INITIAL_TOLERANCE = 100;
    private static final int TOLERANCE_INCREMENT = 50;
    private static final int TOLERANCE_INTERVAL_SECONDS = 30;
    private static final int MAX_TOLERANCE = 500;

    public void enqueuePlayer(UUID userId, int rating) {
        Optional<QueueEntry> existing = queueRepository.getQueueEntry(userId);
        if (existing.isPresent()) {
            throw new IllegalStateException("User is already in the queue");
        }
        long enqueueTime = Instant.now().getEpochSecond();
        QueueEntry entry = new QueueEntry(userId, rating, enqueueTime);
        queueRepository.addToQueue(entry);
    }

    public void dequeuePlayer(UUID userId) {
        queueRepository.removeFromQueue(userId);
    }

    public Optional<Match> findMatch() {

        Set<Object> allPlayers = queueRepository.getAllPlayers();

        for (Object obj : allPlayers) {
                UUID playerOneId = UUID.fromString((String) obj);
                Optional<QueueEntry> playerOneOpt = queueRepository.getQueueEntry(playerOneId);
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

                Set<Object> potentialOpponents = queueRepository.getPlayersInRange(minRating, maxRating);
                for (Object opponentObj : potentialOpponents) {

                        UUID playerTwoId = UUID.fromString((String) opponentObj);
                        if (playerTwoId.equals(playerOneId)) continue;

                        Optional<QueueEntry> playerTwoOpt = queueRepository.getQueueEntry(playerTwoId);
                        if (playerTwoOpt.isEmpty()) {
                            log.warn("Player {} not found in queue during match finding", playerTwoId);
                            continue;
                        }

                        QueueEntry playerTwo = playerTwoOpt.get();

                        long timeInQueueTwo = currentTime - playerTwo.getTimestamp();
                        int dynamicToleranceTwo = calculateDynamicTolerance(timeInQueueTwo);

                        if (playerTwo.getRating() >= (playerOne.getRating() - dynamicTolerance) &&
                                playerTwo.getRating() <= (playerOne.getRating() + dynamicTolerance)) {

                            Match match = new Match(UUID.randomUUID(), playerOneId, playerTwoId);
                            log.info("Match {} created between {} and {}", match.getId(), playerOneId, playerTwoId);


                            queueRepository.removeFromQueue(playerOneId);
                            queueRepository.removeFromQueue(playerTwoId);
                            CreateGameRequest request = new CreateGameRequest();
                            request.setFirstPlayerId(playerOneId);
                            request.setSecondPlayerId(playerTwoId);
                            gameManagementClient.createGame(request);
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

    public void processQueue() {
        log.info("Starting matchmaking processor thread");
        while (true) {
            try {
                Optional<Match> matchOpt = findMatch();
                if (matchOpt.isPresent()) {
                    // Матч створено, можна виконати додаткові дії
                    // Наприклад, повідомити Game Management Service
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Matchmaking processor thread interrupted", e);
                break;
            } catch (Exception e) {
                log.error("Error occurred during match finding process", e);
            }
        }
    }
}
