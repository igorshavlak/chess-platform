package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.Data;


import java.util.UUID;

@Data
public class EnqueueRequest {

    private UUID userId;
    private String queueKey;
    private Integer rating;
    private boolean ranked;
}