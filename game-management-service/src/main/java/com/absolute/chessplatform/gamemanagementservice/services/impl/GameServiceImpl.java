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

    private static final String GAME_PREFIX = "game:";

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();


    public UUID createGame(CreateGameRequest createGameRequest) {
        log.info("Creating new game");
        UUID gameId = UUID.randomUUID();
        UUID whitePlayerId = createGameRequest.getWhitePlayerId();
        UUID blackPlayerId = createGameRequest.getBlackPlayerId();
        ActiveGame game = new ActiveGame(whitePlayerId, blackPlayerId, new ArrayList<>(), new Date());
        redisTemplate.opsForValue().set(GAME_PREFIX + gameId, game);
        log.info("Active game {} created between {} and {}", gameId, whitePlayerId, blackPlayerId);
        String[] timeControl = createGameRequest.getTimeControl().split("\\+");
        long baseMillis = Long.parseLong(timeControl[0]) * 60_000;
        long incMillis  = Long.parseLong(timeControl[1]) * 1000;
        long extraInc   = createGameRequest.getAdditionalTime() * 1000L;
        long increment  = incMillis + extraInc;
        GameSession session = new GameSession(
                gameId,
                baseMillis,
                increment,
                createGameRequest.getGameMode(),
                createGameRequest.getTimeControl(),
                createGameRequest.isRating());

        long now = System.currentTimeMillis();
        session.setLastMoveTimestamp(now);

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
            log.info("Game over by checkmate");
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
        long whiteDeadline = session.getLastMoveTimestamp() + session.getWhiteRemaining();
        long blackDeadline = session.getLastMoveTimestamp() + session.getBlackRemaining();

        return new MoveResult(
                gameId,
                move,
                session.getWhiteRemaining(),
                session.getBlackRemaining(),
                session.getActivePlayerIsWhite(),
                whiteDeadline,
                blackDeadline
        );
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
//        gameRepository.save(game);
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
        return initActiveGame(game, session);
    }
    @Override
    public List<ActiveGameDTO> getGamesInfo(List<UUID> gameIds) {
        List<String> keys = gameIds.stream()
                .map(gameId -> GAME_PREFIX + gameId.toString())
                .collect(Collectors.toList());

        List<ActiveGame> games = redisTemplate.opsForValue().multiGet(keys);
        List<ActiveGameDTO> gameDTOs = new ArrayList<>();

        for (int i = 0; i < gameIds.size(); i++) {
            ActiveGame game = games.get(i);
            if (game == null) {
                throw new ResourceNotFoundException("Active game not found for game ID: " + gameIds.get(i));
            }
            GameSession session = sessions.get(gameIds.get(i));
            if (session == null) {
                throw new ResourceNotFoundException("Game session not found for game ID: " + gameIds.get(i));
            }
            ActiveGameDTO dto = initActiveGame(game, session);
            gameDTOs.add(dto);
        }
        return gameDTOs;
    }

    private ActiveGameDTO initActiveGame(ActiveGame game, GameSession session) {
        ActiveGameDTO dto = new ActiveGameDTO();
        dto.setGameId(session.getGameId());
        dto.setWhitePlayerId(game.whitePlayerId());
        dto.setBlackPlayerId(game.blackPlayerId());
        dto.setMoves(game.moves());
        dto.setStartTime(game.startTime());
        dto.setWhiteTimeMillis(session.getWhiteRemaining());
        dto.setBlackTimeMillis(session.getBlackRemaining());
        dto.setGameMode(session.getGameMode());
        dto.setTimeControl(session.getTimeControl());
        dto.setRating(session.isRating());
        dto.setIncrementMillis(session.getIncrementMillis());
        dto.setActivePlayerIsWhite(session.getActivePlayerIsWhite());
        dto.setLastMoveTimestamp(session.getLastMoveTimestamp());
        dto.setWhiteDeadline(session.getLastMoveTimestamp() + session.getWhiteRemaining());
        dto.setBlackDeadline(session.getLastMoveTimestamp() + session.getBlackRemaining());

        long now = System.currentTimeMillis();
        if (game.moves().isEmpty()) {
            // Якщо ходів немає, дедлайни базуються на початковому часі
            dto.setWhiteDeadline(now + session.getWhiteRemaining());
            dto.setBlackDeadline(now + session.getBlackRemaining());
        } else {
            // Для активного гравця дедлайн = lastMoveTimestamp + remaining
            // Для неактивного гравця дедлайн = now + remaining
            if (session.getActivePlayerIsWhite()) {
                dto.setWhiteDeadline(session.getLastMoveTimestamp() + session.getWhiteRemaining());
                dto.setBlackDeadline(now + session.getBlackRemaining());
            } else {
                dto.setWhiteDeadline(now + session.getWhiteRemaining());
                dto.setBlackDeadline(session.getLastMoveTimestamp() + session.getBlackRemaining());
            }
        }
        return dto;
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
