package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.Data;

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Data
public class GameSession {
    private final UUID gameId;
    private long whiteRemaining;
    private long blackRemaining;
    private Boolean activePlayerIsWhite;
    private long lastMoveTimestamp;
    private final long incrementMillis;
    private ScheduledFuture<?> timeoutTask;

    public GameSession(UUID gameId,
                       long initialMillis,
                       long incrementMillis) {
        this.gameId = gameId;
        this.whiteRemaining = initialMillis;
        this.blackRemaining = initialMillis;
        this.incrementMillis = incrementMillis;
        this.activePlayerIsWhite = true;
        this.lastMoveTimestamp = System.currentTimeMillis();
    }
    public long getRemainingTime(boolean isWhite) {
        if (isWhite) {
            return whiteRemaining;
        }
        return blackRemaining;
    }
    public void setRemainingTime(boolean isWhite, long time) {
        if (isWhite) {
            whiteRemaining = time;
        }
        blackRemaining = time;
    }
}
