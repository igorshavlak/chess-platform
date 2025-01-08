package com.absolute.chessplatform.matchmakingservice.entities;

import lombok.Data;


import java.util.UUID;

@Data
public class EnqueueRequest {

    private UUID userId;
    private int rating;
}