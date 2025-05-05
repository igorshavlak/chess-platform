package com.absolute.chessplatform.notificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
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
    private String masterNickname;
    private int masterRating;
}
