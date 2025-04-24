package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.blog.BlogComment;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.BlogCommentEntity;

public class BlogCommentMapper {
    public static BlogComment toDomain(BlogCommentEntity e) {
        return new BlogComment(e.getId(), e.getBlogPostId(), e.getUserId(), e.getContent(), e.getCreatedAt());
    }
    public static BlogCommentEntity toEntity(BlogComment d) {
        BlogCommentEntity e = new BlogCommentEntity();
        e.setId(d.getId()); e.setBlogPostId(d.getBlogPostId()); e.setUserId(d.getUserId());
        e.setContent(d.getContent()); e.setCreatedAt(d.getCreatedAt()); return e;
    }
}