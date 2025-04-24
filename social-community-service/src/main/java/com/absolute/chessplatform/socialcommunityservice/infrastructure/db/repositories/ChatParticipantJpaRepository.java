package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatParticipantEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantJpaRepository extends JpaRepository<ChatParticipantEntity, ChatParticipantKey> {}
