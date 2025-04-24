package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FriendRequestJpaRepository extends JpaRepository<FriendRequestEntity, UUID> {
    List<FriendRequestEntity> findByTargetId(UUID targetId);
}

