package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Match {
    private UUID id;
    private UUID player1;
    private UUID player2;
}
