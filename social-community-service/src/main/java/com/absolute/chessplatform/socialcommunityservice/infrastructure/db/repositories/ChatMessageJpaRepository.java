package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, UUID> {}