package com.absolute.chessplatform.gamemanagementservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record ActiveGame(UUID whitePlayerId, UUID blackPlayerId, List<String> moves,
                         LocalDateTime startTime) implements Serializable {
}
