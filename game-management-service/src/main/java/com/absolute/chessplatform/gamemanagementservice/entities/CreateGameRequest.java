package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGameRequest {
    private UUID gameId;
    private UUID whitePlayerId;
    private UUID blackPlayerId;
    private GameMode gameMode;
    private String timeControl;
    private boolean isRating;
    private int additionalTime;
}
