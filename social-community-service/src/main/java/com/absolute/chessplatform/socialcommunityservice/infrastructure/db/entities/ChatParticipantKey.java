package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Embeddable
public class ChatParticipantKey implements Serializable {
    @Column(name = "room_id")
    private  UUID roomId;
    @Column(name = "user_id")
    private UUID userId;

    public ChatParticipantKey() {}

    public ChatParticipantKey(UUID roomId, UUID userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatParticipantKey)) return false;
        ChatParticipantKey that = (ChatParticipantKey) o;
        return Objects.equals(roomId, that.roomId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, userId);
    }
}
