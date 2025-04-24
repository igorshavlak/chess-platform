package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories;

import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.BlogCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogCommentJpaRepository extends JpaRepository<BlogCommentEntity, UUID> {}