package com.absolute.chessplatform.socialcommunityservice.domain.social.models;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class FriendRequest {
    private final UUID id;
    private final UUID requesterId;
    private final UUID targetId;
    private final String status;
    private final Instant createdAt;
    private final Instant respondedAt;
}