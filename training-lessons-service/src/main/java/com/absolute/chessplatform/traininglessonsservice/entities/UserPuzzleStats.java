package com.absolute.chessplatform.traininglessonsservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_puzzle_stats")
public class UserPuzzleStats {
    @Id
    @Column(name = "userid")
    private UUID userId;
    @Column(name = "rating", nullable = false)
    private Integer rating = 1500;
    @Column(name = "ratingdeviation", nullable = false)
    private Integer ratingDeviation = 350;
    @Column(name = "nbplays", nullable = false)
    private Integer nbPlays = 0;
    @Column(name = "nbsuccess", nullable = false)
    private Integer nbSuccess = 0;

    public UserPuzzleStats() {
    }

    public UserPuzzleStats(UUID userId) {
        this.userId = userId;
    }

}
