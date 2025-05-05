package com.absolute.chessplatform.traininglessonsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;


@Data
@Entity
@Table(name = "user_puzzle_history")
public class UserPuzzleHistory {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "userid", nullable = false)
    private UUID userId;
    @Column(name = "puzzleid", length = 10, nullable = false)
    private String puzzleId;
    @Column(name = "success", nullable = false)
    private Boolean success;
    @Column(name = "tries", nullable = false)
    private Integer tries;
    @Column(name = "solvedat", nullable = false)
    private Instant solvedAt = Instant.now();
}