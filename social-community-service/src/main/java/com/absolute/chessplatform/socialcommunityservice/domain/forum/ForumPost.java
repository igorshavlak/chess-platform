package com.absolute.chessplatform.socialcommunityservice.domain.forum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ForumPost {
    private final UUID id;
    private final UUID threadId;
    private final UUID userId;
    private final UUID parentPostId;
    private final String content;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final boolean isEdited;
    private final UUID editedBy;
    private final int likesCount;
    private final String attachmentUrl;
    private final String ipAddress;
}