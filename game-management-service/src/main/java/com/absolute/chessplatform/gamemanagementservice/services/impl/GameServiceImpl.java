package com.absolute.chessplatform.gamemanagementservice.services.impl;

import com.absolute.chessplatform.gamemanagementservice.dtos.ActiveGameDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.*;
import com.absolute.chessplatform.gamemanagementservice.exception.GameEndException;
import com.absolute.chessplatform.gamemanagementservice.exception.ResourceNotFoundException;
import com.absolute.chessplatform.gamemanagementservice.repositories.GameRepository;
import com.absolute.chessplatform.gamemanagementservice.services.GameService;
import com.absolute.chessplatform.gamemanagementservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final NotificationService notificationService;
    private final RedisTemplate<String, ActiveGame> redisTemplate;
    private final TaskScheduler taskScheduler;
    private static final long INITIAL_TIME_MILLIS = 10 * 60 * 1000;
    private static final long INCREMENT_MILLIS = 0;

    private static final String GAME_PREFIX = "game:";

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();
    private final Map<UUID, SimulSession> simulSessions = new ConcurrentHashMap<>();




    public UUID createGame(CreateGameRequest createGameRequest) {
        log.info("Creating new game");
        UUID gameId = UUID.randomUUID();
        UUID whitePlayerId = createGameRequest.getWhitePlayerId();
        UUID blackPlayerId = createGameRequest.getBlackPlayerId();
        ActiveGame game = new ActiveGame(whitePlayerId, blackPlayerId, new ArrayList<>(), new Date());
        redisTemplate.opsForValue().set(GAME_PREFIX + gameId, game);
        log.info("Active game {} created between {} and {}", gameId, whitePlayerId, blackPlayerId);
        String[] timeControl = createGameRequest.getTimeControl().split("\\+");
        GameSession session = new GameSession(
                gameId,
                Long.parseLong(timeControl[0]) * 60 * 1000,
                Long.parseLong(timeControl[1]) * 1000,
                createGameRequest.getGameMode(),
                createGameRequest.getTimeControl(),
                createGameRequest.isRating());
        sessions.put(gameId, session);

        log.info("Game {} created and timeout scheduled", gameId);
        return gameId;
    }

    public MoveResult makeMove(UUID gameId, String move, UUID clientId) {
        if (move == null || move.isEmpty()) {
            throw new IllegalArgumentException("Move cannot be null or empty");
        }
        String key = GAME_PREFIX + gameId.toString();
        ActiveGame activeGame = redisTemplate.opsForValue().get(key);
        if (activeGame == null) {
            throw new ResourceNotFoundException("Game not found");
        }
        activeGame.moves().add(move);
        redisTemplate.opsForValue().set(key, activeGame);

        GameSession session = sessions.get(gameId);
        if(session == null) {
            throw new ResourceNotFoundException("Game not found");
        }

        UUID expectedPlayer = session.getActivePlayerIsWhite() ? activeGame.whitePlayerId() : activeGame.blackPlayerId();
        if (!expectedPlayer.equals(clientId)) {
            throw new IllegalArgumentException("Not your turn");
        }

        if (move.charAt(move.length() - 1) == '#') {
            concludeGame(gameId, GameStatus.CHECKMATE, session.getActivePlayerIsWhite());
            sessions.remove(session.getGameId());
            throw new GameEndException("Game over by checkmate");
        }
        synchronized (session) {
            long now = System.currentTimeMillis();
            long elapsed = now - session.getLastMoveTimestamp();
            long remaining = session.getRemainingTime(session.getActivePlayerIsWhite()) - elapsed;
            session.setRemainingTime(session.getActivePlayerIsWhite(), remaining);

            long afterInc = remaining + session.getIncrementMillis();
            session.setRemainingTime(session.getActivePlayerIsWhite(), afterInc);

            session.setActivePlayerIsWhite(!session.getActivePlayerIsWhite());
            session.setLastMoveTimestamp(now);

            ScheduledFuture<?> prev = session.getTimeoutTask();
            if (prev != null && !prev.isDone()) {
                prev.cancel(false);
            }

            scheduleTimeout(session);
        }
        return new MoveResult(gameId, move, session.getWhiteRemaining(),session.getBlackRemaining(),session.getActivePlayerIsWhite());
    }

    public void concludeGame(UUID gameId, GameStatus gameStatus, boolean isWhitePlayerWinner) {

        log.info("Concluding game {}", gameId);
        String key = GAME_PREFIX + gameId.toString();
        ActiveGame activeGame = redisTemplate.opsForValue().get(key);
        if (activeGame == null) {
            throw new ResourceNotFoundException("Active game not found");
        }
        redisTemplate.delete(key);
        Game game = new Game();
        game.setGameId(gameId);
        game.setPlayerBlackId(activeGame.blackPlayerId());
        game.setPlayerWhiteId(activeGame.whitePlayerId());
        game.setStatus(gameStatus);
        game.setMoves(activeGame.moves());
        game.setGameType("-");
        game.setCreatedAt(activeGame.startTime());
        gameRepository.save(game);
        notificationService.sendGameConcludedNotification(new ConcludeGameNotification(gameId,gameStatus,isWhitePlayerWinner));
        log.info("Game {} concluded with status {}", gameId, gameStatus.toString());
    }

    public ActiveGameDTO getGameInfo(UUID gameId) {
        String key = GAME_PREFIX + gameId.toString();
        ActiveGame game = redisTemplate.opsForValue().get(key);
        GameSession session = sessions.get(gameId);
        if (game == null || session == null) {
            throw new ResourceNotFoundException("Active game not found");
        }
        return new ActiveGameDTO(
                game.whitePlayerId(),
                game.blackPlayerId(),
                game.moves(),
                game.startTime(),
                session.getWhiteRemaining(),
                session.getBlackRemaining(),
                session.getGameMode(),
                session.getTimeControl(),
                session.isRating()
        );
    }
    private void scheduleTimeout(GameSession gameSession) {
        boolean isActivePlayerIsWhite = gameSession.getActivePlayerIsWhite();
        long delay = gameSession.getRemainingTime(isActivePlayerIsWhite);
        Instant deadline = Instant.now().plusMillis(delay);
        ScheduledFuture<?> task = taskScheduler.schedule(
                () -> handleTimeout(gameSession.getGameId(),isActivePlayerIsWhite),
                deadline
        );
        gameSession.setTimeoutTask(task);
    }
    private void handleTimeout(UUID gameId, boolean isActivePlayerIsWhite) {
        log.info("Timeout in game {}", gameId);
        concludeGame(gameId, GameStatus.TIMEOUT, !isActivePlayerIsWhite);
    }
    public GameSession getGameSession(UUID gameId){
        return sessions.get(gameId);
    }

}
