package com.absolute.chessplatform.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserStatisticsId implements Serializable {
    @Column(name = "keycloak_id")
    private UUID keycloakId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private GameMode mode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStatisticsId)) return false;
        UserStatisticsId that = (UserStatisticsId) o;
        return Objects.equals(keycloakId, that.keycloakId) && mode == that.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keycloakId, mode);
    }
}