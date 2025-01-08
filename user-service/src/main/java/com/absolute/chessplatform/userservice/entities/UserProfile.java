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
@Data
@Table(name = "user_profiles")
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    private UUID id;

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String locations;

    @Column(columnDefinition = "jsonb")
    private String socialLinks;
}
