package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatMessageEntity;

public class ChatMessageMapper {
    public static ChatMessage toDomain(ChatMessageEntity e) {
        return new ChatMessage(e.getId(), e.getRoomId(), e.getSenderId(), e.getContent(), e.getSentAt(), e.getStatus());
    }
    public static ChatMessageEntity toEntity(ChatMessage d) {
        ChatMessageEntity e = new ChatMessageEntity();
        e.setId(d.getId()); e.setRoomId(d.getRoomId()); e.setSenderId(d.getSenderId());
        e.setContent(d.getContent()); e.setSentAt(d.getSentAt()); e.setStatus(d.getStatus()); return e;
    }
}