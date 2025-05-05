package com.absolute.chessplatform.gamemanagementservice.services.impl;

import com.absolute.chessplatform.gamemanagementservice.clients.NotificationServiceClient;
import com.absolute.chessplatform.gamemanagementservice.clients.UserServiceClient;
import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.UserProfileDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.UserStatisticsDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.*;
import com.absolute.chessplatform.gamemanagementservice.exception.ResourceNotFoundException;
import com.absolute.chessplatform.gamemanagementservice.services.GameService;
import com.absolute.chessplatform.gamemanagementservice.services.SimulLobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class SimulLobbyServiceImpl implements SimulLobbyService {

    private final GameService gameService;
    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    private final Map<UUID, SimulSession> simulSessions = new ConcurrentHashMap<>();

    @Override
    public UUID createSimulLobby(UUID masterId,
                                 int maxOpponents,
                                 String timeControl,
                                 GameMode gameMode,
                                 boolean rating,
                                 Instant startTime,
                                 int minRating,
                                 int additionalMasterTime) {
        UUID lobbyId = UUID.randomUUID();
        boolean alreadyHas = simulSessions.values().stream()
                .anyMatch(s -> s.getMasterId().equals(masterId) && s.getStatus() == SimulStatus.LOBBY);
        if (alreadyHas) {
            throw new IllegalStateException("Master " + masterId + " already has an open simul lobby");
        }
        SimulSession lobby = new SimulSession();
        lobby.setSimulId(lobbyId);
        lobby.setMasterId(masterId);
        lobby.setOpponentIds(Collections.synchronizedList(new ArrayList<>()));
        lobby.setJoinedPlayerIds(Collections.synchronizedList(new ArrayList<>()));
        lobby.setGameIds(new CopyOnWriteArrayList<>());
        lobby.setTimeControl(timeControl);
        lobby.setGameMode(gameMode);
        lobby.setRating(rating);
        lobby.setStartTime(Instant.now());
        lobby.setStatus(SimulStatus.LOBBY);
        lobby.setAdditionalMasterTime(additionalMasterTime);
        lobby.setMinRating(minRating);
        lobby.setStartTime(startTime);
        lobby.setMasterInfo(loadPlayerInfo(masterId));
        lobby.setPlayersMessage(new ConcurrentHashMap<>());
        lobby.setPlayersInfo(new ConcurrentHashMap<>());
        simulSessions.put(lobbyId, lobby);
        notificationServiceClient.sendSimulLobby(SimulSessionDTO.fromEntity(lobby));
        return lobbyId;
    }

    @Override
    public void joinSimulLobby(UUID lobbyId, UUID playerId) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null || lobby.getStatus() != SimulStatus.LOBBY) {
            throw new ResourceNotFoundException("Lobby not found or already started");
        }
        if (lobby.getJoinedPlayerIds().contains(playerId)) {
            return;
        }
        if(lobby.getMasterId().equals(playerId)){
            return;
        }
        lobby.getJoinedPlayerIds().add(playerId);
        lobby.getPlayersInfo().put(playerId, loadPlayerInfo(playerId));

    }

    @Override
    public void sendSimulLobbyPlayerMessage(UUID lobbyId, UUID playerId, String message) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null) {
            throw new ResourceNotFoundException("Lobby not found");
        }
        if (lobby.getJoinedPlayerIds().contains(playerId)) {
            return;
        }
        PlayerInfo playerInfo = lobby.getPlayersInfo().get(playerId);
        PlayerMessage playerMessage = new PlayerMessage(message, playerInfo);
        lobby.getPlayersMessage().put(playerId, playerMessage);

    }

    @Override
    public void confirmSimulPlayer(UUID lobbyId,
                                   UUID playerToConfirm,
                                   Principal principal) {
        String sub = ((Jwt) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal()).getClaim("sub");
        UUID authUserId = UUID.fromString(sub);

        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null) {
            throw new ResourceNotFoundException("Lobby was not found");
        }
        if (!lobby.getMasterId().equals(authUserId)) {
            throw new AccessDeniedException("You are not the master of this session.");
        }
        if (lobby.getJoinedPlayerIds().stream().noneMatch(id -> id.equals(playerToConfirm))) {
            throw new IllegalArgumentException("Player is not waiting");
        }
        lobby.getOpponentIds().add(playerToConfirm);
    }

    @Override
    public void removePlayerFromConfirms(UUID lobbyId, UUID playerId, Principal principal){
        String sub = ((Jwt) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal()).getClaim("sub");
        UUID authUserId = UUID.fromString(sub);
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null) {
            throw new ResourceNotFoundException("Lobby was not found");
        }
        if (!lobby.getMasterId().equals(authUserId)) {
            throw new AccessDeniedException("You are not the master of this session.");
        }
        if (lobby.getOpponentIds().stream().noneMatch(id -> id.equals(playerId))) {
            throw new IllegalArgumentException("Player was not confirmed");
        }
        lobby.getOpponentIds().remove(playerId);
    }


    @Override
    public UUID startSimulSession(UUID lobbyId) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null || lobby.getStatus() != SimulStatus.LOBBY) {
            throw new IllegalStateException("Cannot start: invalid lobby or lobby was started");
        }
        UUID master = lobby.getMasterId();
        List<UUID> opponents = lobby.getJoinedPlayerIds().stream()
                .filter(id -> !id.equals(master))
                .toList();

        List<UUID> createdGameIds = opponents.stream()
                .map(op -> gameService.createGame(CreateGameRequest.builder()
                        .whitePlayerId(master)
                        .blackPlayerId(op)
                        .gameMode(lobby.getGameMode())
                        .timeControl(lobby.getTimeControl())
                        .isRating(lobby.isRating())
                        .build()))
                .toList();

        lobby.setGameIds(createdGameIds);
        lobby.setStatus(SimulStatus.STARTED);
        return lobbyId;

    }

    @Override
    public List<SimulSessionDTO> getSimulLobbies() {
        return simulSessions.values().stream()
                .filter(lobby -> lobby.getStatus() == SimulStatus.LOBBY)
                .map(SimulSessionDTO::fromEntity)
                .toList();
    }

    @Override
    public SimulSessionDTO getSimulLobby(UUID lobbyId) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null || lobby.getStatus() != SimulStatus.LOBBY) {
            throw new IllegalStateException("Cannot start: invalid lobby or lobby was started");
        }
        return SimulSessionDTO.fromEntity(lobby);
    }

    private PlayerInfo loadPlayerInfo(UUID userId) {
        ResponseEntity<UserProfileDTO> profileResp = userServiceClient.getUserProfile(userId);
        if (!profileResp.getStatusCode().is2xxSuccessful() || profileResp.getBody() == null) {
            throw new ResourceNotFoundException("User profile was not found for id " + userId);
        }

        ResponseEntity<UserStatisticsDTO> statsResp = userServiceClient.getClassicStats(userId);
        if (!statsResp.getStatusCode().is2xxSuccessful() || statsResp.getBody() == null) {
            throw new ResourceNotFoundException("User statistics was not found for id " + userId);
        }

        UserProfileDTO profile = profileResp.getBody();
        UserStatisticsDTO stats = statsResp.getBody();
        return new PlayerInfo(
                userId,
                profile.getUsername(),
                stats.getRating()
        );
    }

}
