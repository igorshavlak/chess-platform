package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameFoundNotificationRequest {
    private UUID gameId;
    private UUID whitePlayerId;
    private UUID blackPlayerId;
    private GameMode gameMode;
    private String timeControl;
    private boolean isRating;

}
