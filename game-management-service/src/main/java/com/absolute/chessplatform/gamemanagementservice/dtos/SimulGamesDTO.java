package com.absolute.chessplatform.gamemanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulGamesDTO {
    private UUID lobbyId;
    private UUID masterId;
    private Map<UUID,UUID> gamesPlayerIds;
}
