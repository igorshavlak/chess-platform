package com.absolute.chessplatform.socialcommunityservice.domain.social.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Friendship {
    private final UUID id;
    private final UUID userId;
    private final UUID friendId;
    private final Instant since;
}
