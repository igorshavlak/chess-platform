package com.absolute.chessplatform.socialcommunityservice.domain.social.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChatParticipant {
    private final UUID roomId;
    private final UUID userId;
    private final Instant joinedAt;
}
