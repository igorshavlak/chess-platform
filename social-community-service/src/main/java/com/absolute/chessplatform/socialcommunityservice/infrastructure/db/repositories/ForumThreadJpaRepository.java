package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForumThreadJpaRepository extends JpaRepository<ForumThreadEntity, UUID> {}
