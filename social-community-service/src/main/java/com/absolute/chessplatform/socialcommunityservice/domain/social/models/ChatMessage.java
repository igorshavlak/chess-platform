package com.absolute.chessplatform.socialcommunityservice.domain.social.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public final class ChatMessage {
    private final UUID id;
    private final UUID roomId;
    private final UUID senderId;
    private final String content;
    private final Instant sentAt;
    private final String status;
}