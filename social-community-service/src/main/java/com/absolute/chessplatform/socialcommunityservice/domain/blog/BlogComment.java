package com.absolute.chessplatform.socialcommunityservice.domain.blog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BlogComment {
    private final UUID id;
    private final UUID blogPostId;
    private final UUID userId;
    private final String content;
    private final Instant createdAt;
}