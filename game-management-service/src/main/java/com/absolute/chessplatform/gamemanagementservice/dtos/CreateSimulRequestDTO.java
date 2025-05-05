package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.GameMode;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Data
public class CreateSimulRequestDTO {
    private UUID masterId;
    private String playerColor;
    private int maxOpponents;
    private String timeControl;
    private GameMode gameMode;
    private int additionalMasterTime;
    private int minRating;
    private boolean rating;
    private Instant startTime;
}
