package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ForumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForumCategoryJpaRepository extends JpaRepository<ForumCategoryEntity, UUID> {}
