package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRequest {
    public UUID gameId;
    public UUID whitePlayerId;
    public UUID blackPlayerId;
}
