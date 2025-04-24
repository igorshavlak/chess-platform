package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.forum.ForumThread;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumThreadEntity;

public class ForumThreadMapper {
    public static ForumThread toDomain(ForumThreadEntity e) {
        return new ForumThread(e.getId(), e.getCategoryId(), e.getUserId(), e.getTitle(), e.getCreatedAt(), e.getUpdatedAt());
    }
    public static ForumThreadEntity toEntity(ForumThread d) {
        ForumThreadEntity e = new ForumThreadEntity();
        e.setId(d.getId()); e.setCategoryId(d.getCategoryId());
        e.setUserId(d.getUserId()); e.setTitle(d.getTitle());
        e.setCreatedAt(d.getCreatedAt()); e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}