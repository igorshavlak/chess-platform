package com.absolute.chessplatform.socialcommunityservice.domain.blog;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class BlogPost {
    private final UUID id;
    private final UUID userId;
    private final UUID categoryId;
    private final String title;
    private final String slug;
    private final String summary;
    private final String content;
    private final String status;
    private final int viewsCount;
    private final int likesCount;
    private final Instant publishedAt;
    private final Instant createdAt;
    private final Instant updatedAt;
}
