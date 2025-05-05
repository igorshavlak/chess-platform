package com.absolute.chessplatform.gamemanagementservice.services;

import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.GameMode;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SimulLobbyService {

    UUID createSimulLobby(UUID masterId,
                               int maxOpponents,
                               String timeControl,
                               GameMode gameMode,
                               boolean rating,
                               Instant startTime,
                               int minRating,
                               int additionalMasterTime);
    void joinSimulLobby(UUID lobbyId, UUID playerId);
    UUID startSimulSession(UUID lobbyId);
    void confirmSimulPlayer(UUID lobbyId, UUID playerToConfirm, Principal principal);
    List<SimulSessionDTO> getSimulLobbies();
    SimulSessionDTO getSimulLobby(UUID lobbyId);
    void sendSimulLobbyPlayerMessage(UUID lobbyId, UUID playerId, String message);
    void removePlayerFromConfirms(UUID lobbyId, UUID playerId, Principal principal);
}
