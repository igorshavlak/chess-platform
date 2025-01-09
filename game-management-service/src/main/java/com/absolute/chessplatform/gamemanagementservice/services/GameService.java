package com.absolute.chessplatform.gamemanagementservice.services;


import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;
import com.absolute.chessplatform.gamemanagementservice.entities.Move;

import java.util.List;
import java.util.UUID;

public interface GameService {
    void createGame(UUID firstPlayerId, UUID secondPlayerId);
    Move makeMove(UUID gameId, String move);
    void concludeGame(UUID gameId, GameStatus gameStatus);
    List<String> getMoves(UUID gameId);
}
