package com.absolute.chessplatform.socialcommunityservice.domain.social.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChatRoom {
    private final UUID id;
    private final String name;
    private final String type;
    private final UUID createdBy;
    private final Instant createdAt;
}
