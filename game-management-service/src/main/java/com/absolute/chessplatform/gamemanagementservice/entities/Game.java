package com.absolute.chessplatform.gamemanagementservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "player_black_id")
    private UUID playerBlackId;

    @Column(name = "player_white_id")
    private UUID playerWhiteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> moves;

    @Column(name = "game_type")
    private String gameType;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
