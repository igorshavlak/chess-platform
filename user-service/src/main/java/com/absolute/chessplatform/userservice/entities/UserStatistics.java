package com.absolute.chessplatform.userservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistics {

   @Id
   @Column(name = "user_id")
   private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "mode", nullable = false)
    private GameMode mode;

    // Рейтинги
    private int rating;

    // Загальна статистика ігор
    @Column(name = "games_played")
    private int gamesPlayed;

    @Column(name = "games_won")
    private int gamesWon;

    @Column(name = "games_lost")
    private int gamesLost;

    @Column(name = "games_drawn")
    private int gamesDrawn;

    // Деталізована статистика результатів
    @Column(name = "games_resigned")
    private int gamesResigned;

    @Column(name = "games_timeout")
    private int gamesTimeout;

    @Column(name = "games_abandoned")
    private int gamesAbandoned;

    @Column(name = "draws_by_agreement")
    private int drawsByAgreement;

    @Column(name = "draws_by_stalemate")
    private int drawsByStalemate;

    @Column(name = "draws_by_repetition")
    private int drawsByRepetition;

    // Турнірна статистика
    @Column(name = "tournaments_played")
    private int tournamentsPlayed;

    @Column(name = "tournaments_won")
    private int tournamentsWon;

    // Динаміка рейтингу та серії
    @Column(name = "win_streak")
    private int winStreak;

    @Column(name = "loss_streak")
    private int lossStreak;

    @Column(name = "highest_rating")
    private int highestRating;

    @Column(name = "peak_rating_date")
    private LocalDateTime peakRatingDate;

    @Column(name = "rating_volatility")
    private BigDecimal ratingVolatility;

    // Аналітичні дані
    @Column(name = "total_brilliant_moves")
    private int totalBrilliantMoves;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rating_history", columnDefinition = "jsonb")
    private List<RatingHistory> ratingHistory;

    @Column(name = "opening_win_rate")
    private BigDecimal openingWinRate;

    // Інформація про останню гру
    @Column(name = "last_game_played")
    private LocalDateTime lastGamePlayed;
}