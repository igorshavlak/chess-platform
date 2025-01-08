package com.absolute.chessplatform.userservice.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistics {
    @Id
    private UUID id;
    private int rating;
    @Column(name = "games_played")
    private int gamesPlayed;
    @Column(name = "games_won")
    private int gamesWon;

}
