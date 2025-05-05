package com.absolute.chessplatform.traininglessonsservice.repositories;

import com.absolute.chessplatform.traininglessonsservice.entities.UserRatingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRatingHistoryRepository extends JpaRepository<UserRatingHistory, UUID> {
    List<UserRatingHistory> findByUserIdOrderByChangedAt(UUID userId);
}