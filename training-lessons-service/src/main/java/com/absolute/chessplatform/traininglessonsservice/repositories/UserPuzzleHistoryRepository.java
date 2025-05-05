package com.absolute.chessplatform.traininglessonsservice.repositories;

import com.absolute.chessplatform.traininglessonsservice.entities.UserPuzzleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserPuzzleHistoryRepository extends JpaRepository<UserPuzzleHistory, UUID> {
    List<UserPuzzleHistory> findByUserId(UUID userId);
}