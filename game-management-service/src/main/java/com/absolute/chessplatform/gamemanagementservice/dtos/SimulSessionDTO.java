package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.*;
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

    public static SimulSessionDTO fromEntity(SimulSession s) {
        return new SimulSessionDTO(
                s.getSimulId(),
                s.getMasterId(),
                s.getOpponentIds(),
                s.getJoinedPlayerIds(),
                s.getTimeControl(),
                s.getGameMode(),
                s.getAdditionalMasterTime(),
                s.isRating(),
                s.getStatus(),
                s.getMinRating(),
                s.getStartTime(),
                s.getMasterInfo(),
                s.getPlayersInfo(),
                s.getPlayersMessage()
        );
    }
}
