package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.forum.ForumPost;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumPostEntity;

public class ForumPostMapper {
    public static ForumPost toDomain(ForumPostEntity e) {
        return new ForumPost(e.getId(), e.getThreadId(), e.getUserId(), e.getParentPostId(), e.getContent(), e.getCreatedAt(), e.getUpdatedAt(), e.isEdited(), e.getEditedBy(), e.getLikesCount(), e.getAttachmentUrl(), e.getIpAddress());
    }
    public static ForumPostEntity toEntity(ForumPost d) {
        ForumPostEntity e = new ForumPostEntity();
        e.setId(d.getId()); e.setThreadId(d.getThreadId());
        e.setUserId(d.getUserId()); e.setParentPostId(d.getParentPostId());
        e.setContent(d.getContent()); e.setCreatedAt(d.getCreatedAt()); e.setUpdatedAt(d.getUpdatedAt());
        e.setEdited(d.isEdited()); e.setEditedBy(d.getEditedBy());
        e.setLikesCount(d.getLikesCount()); e.setAttachmentUrl(d.getAttachmentUrl());
        e.setIpAddress(d.getIpAddress()); return e;
    }
}