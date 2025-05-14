package com.absolute.chessplatform.gamemanagementservice.services.impl;

import com.absolute.chessplatform.gamemanagementservice.clients.NotificationServiceClient;
import com.absolute.chessplatform.gamemanagementservice.clients.UserServiceClient;
import com.absolute.chessplatform.gamemanagementservice.dtos.*;
import com.absolute.chessplatform.gamemanagementservice.entities.*;
import com.absolute.chessplatform.gamemanagementservice.exception.ResourceNotFoundException;
import com.absolute.chessplatform.gamemanagementservice.services.GameService;
import com.absolute.chessplatform.gamemanagementservice.services.SimulLobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
        PlayerInfo playerInfo = loadPlayerInfo(playerId);
        lobby.getPlayersInfo().put(playerId, playerInfo);
        notificationServiceClient.sendSimulLobbyPlayer(PlayerInfoDTO.fromEntity(playerInfo),lobbyId);
    }

    @Override
    public List<ActiveGameDTO> getSimulGames(UUID simulSessionId) {
        SimulSession simulSession = simulSessions.get(simulSessionId);
        List<UUID> gameIds = simulSession.getGameIds();
        return gameService.getGamesInfo(gameIds);
    }


    @Override
    public void sendSimulLobbyPlayerMessage(UUID lobbyId, UUID playerId, String message) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null) {
            throw new ResourceNotFoundException("Lobby not found");
        }
        PlayerInfo playerInfo = lobby.getPlayersInfo().get(playerId);
        if(lobby.getMasterId().equals(playerId)){
            playerInfo = lobby.getMasterInfo();
        }
        PlayerMessage playerMessage = new PlayerMessage(message, playerInfo);
        lobby.getPlayersMessage().put(playerId, playerMessage);
        notificationServiceClient.sendSimulLobbyMessage(new PlayerMessageDTO(message,playerInfo),lobby.getSimulId());
    }
    @Override
    public void confirmSimulPlayer(UUID lobbyId,
                                   UUID playerToConfirm,
                                   Principal principal) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        // 2) Отримуємо сам Jwt і читаємо суб’єкт
        Jwt jwt = jwtAuth.getToken();
        String sub = jwt.getSubject();            // це те саме, що claim "sub"
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
        lobby.getJoinedPlayerIds().remove(playerToConfirm);
        notificationServiceClient.confirmSimulPlayer(lobbyId, PlayerInfoDTO.fromEntity(lobby.getPlayersInfo().get(playerToConfirm)));
    }

    @Override
    public void removePlayerFromConfirms(UUID lobbyId, UUID playerId, Principal principal){
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        // 2) Отримуємо сам Jwt і читаємо суб’єкт
        Jwt jwt = jwtAuth.getToken();
        String sub = jwt.getSubject();            // це те саме, що claim "sub"
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
    public SimulGamesDTO startSimulSession(UUID lobbyId) {
        SimulSession lobby = simulSessions.get(lobbyId);
        if (lobby == null || lobby.getStatus() != SimulStatus.LOBBY) {
            throw new IllegalStateException("Cannot start: invalid lobby or lobby was started");
        }
        UUID master = lobby.getMasterId();
        List<UUID> opponents = lobby.getOpponentIds().stream()
                .filter(id -> !id.equals(master))
                .toList();
        Map<UUID, UUID> gamesPlayerIds = new HashMap<>();


        List<UUID> createdGameIds = opponents.stream()
                .map(op -> {
                    UUID gameId = gameService.createGame(CreateGameRequest.builder()
                            .whitePlayerId(master)
                            .blackPlayerId(op)
                            .gameMode(lobby.getGameMode())
                            .timeControl(lobby.getTimeControl())
                            .isRating(lobby.isRating())
                            .additionalTime(lobby.getAdditionalMasterTime())
                            .build());

                    // Додавання пари гри та опонента в мапу
                    gamesPlayerIds.put(gameId, op);

                    return gameId;
                })
                .toList();


        lobby.setGameIds(createdGameIds);
        lobby.setStatus(SimulStatus.STARTED);
        gamesPlayerIds.forEach((gameId, playerId) -> {
            notificationServiceClient.startSimulLobby(playerId, gameId);
        });


        return new SimulGamesDTO(lobbyId, master, gamesPlayerIds);

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
        if (lobby == null) {
            throw new IllegalStateException("Cannot start: invalid lobby");
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
