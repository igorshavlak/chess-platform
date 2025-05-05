package com.absolute.chessplatform.traininglessonsservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "puzzles")
public class Puzzle {
    @Id
    @Column(name = "puzzleid", length = 10)
    private String puzzleId;
    @Column(name = "fen", columnDefinition = "TEXT", nullable = false)
    private String fen;
    @Column(name = "moves", columnDefinition = "TEXT", nullable = false)
    private String moves;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "ratingdeviation")
    private Integer ratingDeviation;
    @Column(name = "popularity")
    private Integer popularity;
    @Column(name = "nbplays")
    private Integer nbPlays;
    @Column(name = "themes", columnDefinition = "TEXT")
    private String themes;
    @Column(name = "gameurl")
    private String gameUrl;
    @Column(name = "openingtags", columnDefinition = "TEXT")
    private String openingTags;

}