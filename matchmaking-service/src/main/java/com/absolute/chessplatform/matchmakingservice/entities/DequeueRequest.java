package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.Data;
import java.util.UUID;

@Data
public class DequeueRequest {
    private UUID userId;
}
