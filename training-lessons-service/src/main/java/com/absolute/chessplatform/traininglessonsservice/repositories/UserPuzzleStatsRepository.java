package com.absolute.chessplatform.traininglessonsservice.repositories;

import com.absolute.chessplatform.traininglessonsservice.entities.UserPuzzleStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPuzzleStatsRepository extends JpaRepository<UserPuzzleStats, UUID> {}