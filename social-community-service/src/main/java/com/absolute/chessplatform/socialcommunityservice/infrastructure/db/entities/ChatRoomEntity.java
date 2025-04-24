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
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    @Id
    private UUID id;

    private String name;

    @Column(nullable = false)
    private String type;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at")
    private Instant createdAt;
}
