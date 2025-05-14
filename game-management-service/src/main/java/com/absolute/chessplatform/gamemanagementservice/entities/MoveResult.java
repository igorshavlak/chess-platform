package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveResult {
    private UUID gameId;
    private String move;
    private long whiteTimeMillis;
    private long blackTimeMillis;
    private boolean activePlayerIsWhite;
    private long whiteDeadline;
    private long blackDeadline;
}
