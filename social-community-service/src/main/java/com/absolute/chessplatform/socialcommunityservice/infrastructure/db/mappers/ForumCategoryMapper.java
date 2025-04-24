package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.forum.ForumCategory;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumCategoryEntity;

public class ForumCategoryMapper {
    public static ForumCategory toDomain(ForumCategoryEntity e) {
        return new ForumCategory(e.getId(), e.getName(), e.getDescription(), e.getCreatedAt());
    }
    public static ForumCategoryEntity toEntity(ForumCategory d) {
        ForumCategoryEntity e = new ForumCategoryEntity();
        e.setId(d.getId()); e.setName(d.getName());
        e.setDescription(d.getDescription()); e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}