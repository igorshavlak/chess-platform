package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ConcludeGameNotification {
    private UUID gameId;
    private GameStatus gameStatus;
    private boolean isWhiteWinner;
}
