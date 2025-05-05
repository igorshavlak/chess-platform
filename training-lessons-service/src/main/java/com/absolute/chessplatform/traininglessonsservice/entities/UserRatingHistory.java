package com.absolute.chessplatform.traininglessonsservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_rating_history")
public class UserRatingHistory {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "userid", nullable = false)
    private UUID userId;
    @Column(name = "rating", nullable = false)
    private Integer rating;
    @Column(name = "changedat", nullable = false)
    private Instant changedAt = Instant.now();

}