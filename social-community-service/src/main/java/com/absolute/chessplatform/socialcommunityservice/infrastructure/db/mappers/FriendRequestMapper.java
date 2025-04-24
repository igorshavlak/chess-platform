package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendRequestEntity;

public class FriendRequestMapper {
    public static FriendRequest toDomain(FriendRequestEntity e) {
        return new FriendRequest(e.getId(), e.getRequesterId(), e.getTargetId(), e.getStatus(), e.getCreatedAt(), e.getRespondedAt());
    }
    public static FriendRequestEntity toEntity(FriendRequest d) {
        FriendRequestEntity e = new FriendRequestEntity();
        e.setId(d.getId()); e.setRequesterId(d.getRequesterId()); e.setTargetId(d.getTargetId());
        e.setStatus(d.getStatus()); e.setCreatedAt(d.getCreatedAt()); e.setRespondedAt(d.getRespondedAt()); return e;
    }
}