package com.absolute.chessplatform.userservice.repositories;

import com.absolute.chessplatform.userservice.entities.GameMode;
import com.absolute.chessplatform.userservice.entities.UserStatistics;
import com.absolute.chessplatform.userservice.entities.UserStatisticsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, UserStatisticsId> {
    Optional<UserStatistics> findById_KeycloakIdAndId_Mode(UUID keycloakId, GameMode mode);
}