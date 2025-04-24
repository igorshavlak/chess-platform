package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForumPostJpaRepository extends JpaRepository<ForumPostEntity, UUID> {}
