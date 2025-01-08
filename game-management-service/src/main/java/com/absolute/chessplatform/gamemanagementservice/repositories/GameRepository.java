package com.absolute.chessplatform.gamemanagementservice.repositories;

import com.absolute.chessplatform.gamemanagementservice.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {


}
