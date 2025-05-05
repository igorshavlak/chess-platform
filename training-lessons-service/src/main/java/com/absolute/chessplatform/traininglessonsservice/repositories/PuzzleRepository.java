package com.absolute.chessplatform.traininglessonsservice.repositories;

import com.absolute.chessplatform.traininglessonsservice.entities.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleRepository extends JpaRepository<Puzzle, String> {}