package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendshipJpaRepository extends JpaRepository<FriendshipEntity, UUID> {
    List<FriendshipEntity> findByUserId(UUID userId);
}
