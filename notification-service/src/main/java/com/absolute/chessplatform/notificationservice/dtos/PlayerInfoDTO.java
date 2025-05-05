package com.absolute.chessplatform.notificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerInfoDTO {
    private UUID playerId;
    private String nickname;
    private int rating;
}
