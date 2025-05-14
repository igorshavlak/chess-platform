package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.GameMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveGameDTO implements Serializable{
   private UUID gameId;
   private UUID whitePlayerId;
   private UUID blackPlayerId;
   private List<String> moves;
   private Date startTime;
   private long whiteTimeMillis;
   private long blackTimeMillis;
   private GameMode gameMode;
   private String timeControl;
   private boolean isRating;
   private Boolean activePlayerIsWhite;
   private long lastMoveTimestamp;
   private long incrementMillis;
   private long whiteDeadline;
   private long blackDeadline;

}

