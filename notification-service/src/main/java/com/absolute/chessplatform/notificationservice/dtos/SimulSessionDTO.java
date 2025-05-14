package com.absolute.chessplatform.notificationservice.dtos;

import com.absolute.chessplatform.notificationservice.entities.PlayerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class SimulSessionDTO {
    private UUID simulId;
    private UUID masterId;
    private List<UUID> opponentIds;
    private List<UUID> joinedPlayerIds;
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
