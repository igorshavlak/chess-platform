package com.absolute.chessplatform.gamemanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {
    private UUID userId;
    private String mode;
    private int rating;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrawn;
    private int gamesResigned;
    private int gamesTimeout;
    private int gamesAbandoned;
    private int drawsByAgreement;
    private int drawsByStalemate;
    private int drawsByRepetition;
    private int tournamentsPlayed;
    private int tournamentsWon;
    private int winStreak;
    private int lossStreak;
    private int highestRating;
    private LocalDateTime peakRatingDate;
    private BigDecimal ratingVolatility;
    private int totalBrilliantMoves;
    private Object ratingHistory;
    private BigDecimal openingWinRate;
    private LocalDateTime lastGamePlayed;
}