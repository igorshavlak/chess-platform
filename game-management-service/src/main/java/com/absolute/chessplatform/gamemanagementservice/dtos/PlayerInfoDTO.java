package com.absolute.chessplatform.gamemanagementservice.dtos;

import com.absolute.chessplatform.gamemanagementservice.entities.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerInfoDTO {
    private UUID playerId;
    private String nickname;
    private int rating;
    public static PlayerInfoDTO fromEntity(PlayerInfo player) {
        return new PlayerInfoDTO(
                player.getPlayerId(),
                player.getNickname(),
                player.getRating()
        );
    }
}
