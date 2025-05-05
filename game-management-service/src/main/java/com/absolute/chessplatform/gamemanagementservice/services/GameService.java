package com.absolute.chessplatform.gamemanagementservice.services;


import com.absolute.chessplatform.gamemanagementservice.dtos.ActiveGameDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.*;

import java.util.List;
import java.util.UUID;

public interface GameService {
    UUID createGame(CreateGameRequest createGameRequest);
    MoveResult makeMove(UUID gameId, String move,UUID clientId);
    void concludeGame(UUID gameId, GameStatus gameStatus,boolean isWhitePlayerWinner);
    ActiveGameDTO getGameInfo(UUID gameId);
    GameSession getGameSession(UUID gameId);
}
