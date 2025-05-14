package com.absolute.chessplatform.notificationservice.entities;

import com.absolute.chessplatform.notificationservice.dtos.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerMessage {
    private String message;
    private PlayerInfo playerInfo;
}
