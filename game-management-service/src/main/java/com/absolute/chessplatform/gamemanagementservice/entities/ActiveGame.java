package com.absolute.chessplatform.gamemanagementservice.entities;

import jdk.jfr.Timespan;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public record ActiveGame(UUID whitePlayerId, UUID blackPlayerId, List<String> moves,
                         Date startTime) implements Serializable {
}
