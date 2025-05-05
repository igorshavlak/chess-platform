package com.absolute.chessplatform.socialcommunityservice.domain.forum;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ForumCategory {
    private final UUID id;
    private final String name;
    private final String description;
    private final Instant createdAt;

}
