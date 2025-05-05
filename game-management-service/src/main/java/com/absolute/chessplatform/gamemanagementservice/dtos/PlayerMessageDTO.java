package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMessageDTO {
    private String message;
    private PlayerInfo playerInfo;
}
