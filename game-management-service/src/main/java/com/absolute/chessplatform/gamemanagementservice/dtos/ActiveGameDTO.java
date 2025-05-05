package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.GameMode;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ActiveGameDTO implements Serializable{
   public UUID whitePlayerId;
   public UUID blackPlayerId;
   public List<String> moves;
   public Date startTime;
   public long whiteTimeMillis;
   public long blackTimeMillis;
   private GameMode gameMode;
   private String timeControl;
   private boolean isRating;

}

