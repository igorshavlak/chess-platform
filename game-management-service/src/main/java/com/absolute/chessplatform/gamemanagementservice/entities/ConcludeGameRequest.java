package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcludeGameRequest {
    private UUID gameId;
    private GameStatus gameStatus;
}
