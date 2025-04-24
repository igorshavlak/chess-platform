package com.absolute.chessplatform.socialcommunityservice.domain.forum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ForumThread {
    private final UUID id;
    private final UUID categoryId;
    private final UUID userId;
    private final String title;
    private final Instant createdAt;
    private final Instant updatedAt;
}