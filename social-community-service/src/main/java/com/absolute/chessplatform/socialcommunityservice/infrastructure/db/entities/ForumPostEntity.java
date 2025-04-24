package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "forum_posts")
public class ForumPostEntity {
    @Id
    private UUID id;

    @Column(name = "thread_id", nullable = false)
    private UUID threadId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "parent_post_id")
    private UUID parentPostId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_edited")
    private boolean isEdited;

    @Column(name = "edited_by")
    private UUID editedBy;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "ip_address")
    private String ipAddress;
}
