package com.absolute.chessplatform.matchmakingservice.entities;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueEntry {
    private UUID userId;
    private int rating;
    private long timestamp;
}
