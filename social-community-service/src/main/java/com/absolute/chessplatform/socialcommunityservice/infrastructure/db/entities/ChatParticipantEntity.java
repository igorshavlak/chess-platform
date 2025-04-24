package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "chat_participants")
public class ChatParticipantEntity {
    @EmbeddedId
    private ChatParticipantKey id;

    @Column(name = "joined_at")
    private Instant joinedAt;
}