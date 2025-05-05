package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulSession {
    private UUID simulId;
    private UUID masterId;
    private List<UUID> opponentIds;
    private List<UUID> joinedPlayerIds;
    private List<UUID> gameIds;
    private String timeControl;
    private GameMode gameMode;
    private int additionalMasterTime;
    private boolean rating;
    private SimulStatus status;
    private int minRating;
    private Instant startTime;
    private PlayerInfo masterInfo;
    private Map<UUID, PlayerInfo> playersInfo;
    private Map<UUID, PlayerMessage> playersMessage;

}
