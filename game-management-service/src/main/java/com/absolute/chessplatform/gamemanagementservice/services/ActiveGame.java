package com.absolute.chessplatform.gamemanagementservice.services;

import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ActiveGame implements Serializable {
    private final UUID whitePlayerId;
    private final UUID blackPlayerId;
    private final List<String> moves;
    private final LocalDateTime startTime;
}
