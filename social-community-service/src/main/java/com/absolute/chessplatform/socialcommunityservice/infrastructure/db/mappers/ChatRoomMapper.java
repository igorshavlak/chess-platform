package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatRoomEntity;

public class ChatRoomMapper {
    public static ChatRoom toDomain(ChatRoomEntity e) {
        return new ChatRoom(e.getId(), e.getName(), e.getType(), e.getCreatedBy(), e.getCreatedAt());
    }
    public static ChatRoomEntity toEntity(ChatRoom d) {
        ChatRoomEntity e = new ChatRoomEntity();
        e.setId(d.getId()); e.setName(d.getName()); e.setType(d.getType()); e.setCreatedBy(d.getCreatedBy()); e.setCreatedAt(d.getCreatedAt()); return e;
    }
}