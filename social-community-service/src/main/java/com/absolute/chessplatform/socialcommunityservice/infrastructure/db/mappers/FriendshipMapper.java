package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendshipEntity;

public class FriendshipMapper {
    public static Friendship toDomain(FriendshipEntity e) {
        return new Friendship(e.getId(), e.getUserId(), e.getFriendId(), e.getSince());
    }
    public static FriendshipEntity toEntity(Friendship d) {
        FriendshipEntity e = new FriendshipEntity();
        e.setId(d.getId()); e.setUserId(d.getUserId()); e.setFriendId(d.getFriendId()); e.setSince(d.getSince()); return e;
    }
}