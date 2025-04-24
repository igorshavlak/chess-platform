package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.blog.BlogPost;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.BlogPostEntity;

public class BlogPostMapper {
    public static BlogPost toDomain(BlogPostEntity e) {
        return new BlogPost(e.getId(), e.getUserId(), e.getCategoryId(), e.getTitle(), e.getSlug(), e.getSummary(), e.getContent(), e.getStatus(), e.getViewsCount(), e.getLikesCount(), e.getPublishedAt(), e.getCreatedAt(), e.getUpdatedAt());
    }
    public static BlogPostEntity toEntity(BlogPost d) {
        BlogPostEntity e = new BlogPostEntity();
        e.setId(d.getId()); e.setUserId(d.getUserId()); e.setCategoryId(d.getCategoryId());
        e.setTitle(d.getTitle()); e.setSlug(d.getSlug()); e.setSummary(d.getSummary());
        e.setContent(d.getContent()); e.setStatus(d.getStatus());
        e.setViewsCount(d.getViewsCount()); e.setLikesCount(d.getLikesCount());
        e.setPublishedAt(d.getPublishedAt()); e.setCreatedAt(d.getCreatedAt()); e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}