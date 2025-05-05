package com.absolute.chessplatform.notificationservice.dtos;


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
