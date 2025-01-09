package com.absolute.chessplatform.gamemanagementservice.services;

import com.absolute.chessplatform.gamemanagementservice.entities.ActiveGame;
import com.absolute.chessplatform.gamemanagementservice.entities.Game;
import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;
import com.absolute.chessplatform.gamemanagementservice.entities.Move;
import com.absolute.chessplatform.gamemanagementservice.exception.ResourceNotFoundException;
import com.absolute.chessplatform.gamemanagementservice.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final RedisTemplate<String, ActiveGame> redisTemplate;

    private static final String GAME_PREFIX = "game:";


    public void createGame(UUID firstPlayerId, UUID secondPlayerId) {
        log.info("Creating new game");
        UUID gameId = UUID.randomUUID();
        ActiveGame game = new ActiveGame(firstPlayerId, secondPlayerId, new ArrayList<>(), LocalDateTime.now());
        ArrayList<String> moves = new ArrayList<>();
        redisTemplate.opsForValue().set(GAME_PREFIX + gameId, game);
        log.info("Active game {} created between {} and {}", gameId, firstPlayerId, secondPlayerId);
    }

    public Move makeMove(UUID gameId, String move) {
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
        if (move.charAt(move.length() - 1) == '#') {
            concludeGame(gameId, GameStatus.CHECKMATE);
        }
        return new Move(gameId, move);
    }

    public void concludeGame(UUID gameId, GameStatus gameStatus) {
        log.info("Concluding game {}", gameId);
        String key = GAME_PREFIX + gameId.toString();
        ActiveGame activeGame = redisTemplate.opsForValue().get(key);
        if (activeGame == null) {
            throw new ResourceNotFoundException("Active game not found");
        }
        Game game = new Game();
        game.setGameId(gameId);
        game.setPlayerBlackId(activeGame.blackPlayerId());
        game.setPlayerWhiteId(activeGame.whitePlayerId());
        game.setStatus(gameStatus);
        game.setCreatedAt(activeGame.startTime());
        gameRepository.save(game);
        redisTemplate.delete(key);
        log.info("Game {} concluded with status {}", gameId, gameStatus.toString());

    }
    public List<String> getMoves(UUID gameId) {
        String key = GAME_PREFIX + gameId.toString();
        ActiveGame game = redisTemplate.opsForValue().get(key);
        if (game == null) {
            throw new ResourceNotFoundException("Active game not found");
        }
        return game.moves();
    }


}
