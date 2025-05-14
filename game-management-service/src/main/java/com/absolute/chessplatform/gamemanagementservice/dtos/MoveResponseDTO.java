package com.absolute.chessplatform.gamemanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoveResponseDTO {
    UUID gameId;
    String move;
    String senderId;
    long whiteTimeMillis;
    long blackTimeMillis;
    boolean isActivePlayerWhite;
    long serverTimestampMillis;
    private long whiteDeadline;
    private long blackDeadline;


}
