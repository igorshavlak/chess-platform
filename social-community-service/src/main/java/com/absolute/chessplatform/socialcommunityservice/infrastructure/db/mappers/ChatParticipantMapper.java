package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatParticipant;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatParticipantEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatParticipantKey;

public class ChatParticipantMapper {
    public static ChatParticipant toDomain(ChatParticipantEntity e) {
        return new ChatParticipant(e.getId().getRoomId(), e.getId().getUserId(), e.getJoinedAt());
    }
    public static ChatParticipantEntity toEntity(ChatParticipant d) {
        ChatParticipantEntity e = new ChatParticipantEntity();
        ChatParticipantKey key = new ChatParticipantKey(d.getRoomId(), d.getUserId());
        e.setId(key); e.setJoinedAt(d.getJoinedAt()); return e;
    }
}
