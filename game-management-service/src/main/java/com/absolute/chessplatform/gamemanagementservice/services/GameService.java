package com.absolute.chessplatform.gamemanagementservice.services;


import com.absolute.chessplatform.gamemanagementservice.entities.ActiveGame;
import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;
import com.absolute.chessplatform.gamemanagementservice.entities.Move;
import com.absolute.chessplatform.gamemanagementservice.entities.MoveResult;

import java.util.List;
import java.util.UUID;

public interface GameService {
    UUID createGame(UUID whitePlayerId, UUID blackPlayerId);
    MoveResult makeMove(UUID gameId, String move,UUID clientId);
    void concludeGame(UUID gameId, GameStatus gameStatus,boolean isWhitePlayerWinner);
    ActiveGame getGameInfo(UUID gameId);
}
