package com.absolute.chessplatform.gamemanagementservice.controllers;

import com.absolute.chessplatform.gamemanagementservice.dtos.ActiveGameDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.CreateSimulRequestDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.SimulGamesDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.SimulSession;
import com.absolute.chessplatform.gamemanagementservice.services.SimulLobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games/simul")
public class SimulLobbyController {

    private final SimulLobbyService simulLobbyService;

    @PostMapping("/createLobby")
    public ResponseEntity<UUID> createSimul(@RequestBody CreateSimulRequestDTO req){
        UUID simulId = simulLobbyService.createSimulLobby(
                req.getMasterId(),
                req.getMaxOpponents(),
                req.getTimeControl(),
                req.getGameMode(),
                req.isRating(),
                req.getStartTime(),
                req.getMinRating(),
                req.getAdditionalMasterTime()
        );
        return ResponseEntity.ok(simulId);
    }
    @PostMapping("/lobby/{lobbyId}/join")
    public ResponseEntity<Void> joinLobby(@PathVariable UUID lobbyId,
                                          @RequestParam UUID playerId) {
        simulLobbyService.joinSimulLobby(lobbyId, playerId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/lobby/{lobbyId}/start")
    public ResponseEntity<SimulGamesDTO> startSimul(@PathVariable UUID lobbyId) {
        SimulGamesDTO simulGamesDTO = simulLobbyService.startSimulSession(lobbyId);
        return ResponseEntity.ok(simulGamesDTO);
    }
    @PostMapping("/lobby/getLobbies")
    public ResponseEntity<List<SimulSessionDTO>> getSimulLobbies(){
        List<SimulSessionDTO> simulSessionDTOS = simulLobbyService.getSimulLobbies();
        return ResponseEntity.ok(simulSessionDTOS);
    }
    @PostMapping("/lobby/{lobbyId}/message")
    public ResponseEntity<String> sendSimulPlayerPlayerMessage(@PathVariable UUID lobbyId, @RequestParam("playerId") UUID playerId, @RequestParam("message") String message){
        simulLobbyService.sendSimulLobbyPlayerMessage(lobbyId, playerId, message);
        return ResponseEntity.ok("Message was send");
    }
    @GetMapping("/lobby/{lobbyId}")
    public ResponseEntity<SimulSessionDTO> getSimulLobby(@PathVariable UUID lobbyId){
        SimulSessionDTO simulSessionDTO =  simulLobbyService.getSimulLobby(lobbyId);
        return ResponseEntity.ok(simulSessionDTO);
    }
    @PostMapping("/lobby/confirmSimulPlayer/{lobbyId}")
    public ResponseEntity<String> confirmPlayer(@PathVariable UUID lobbyId, @RequestParam UUID playerId, Principal principal){
        simulLobbyService.confirmSimulPlayer(lobbyId,playerId,principal);
        return ResponseEntity.ok("player was confirmed");
    }
    @GetMapping("/getSimulGames/{lobbyId}")
    public ResponseEntity<List<ActiveGameDTO>> getSimulGames(@PathVariable UUID lobbyId){
        List<ActiveGameDTO> activeGameDTOS = simulLobbyService.getSimulGames(lobbyId);
        return ResponseEntity.ok(activeGameDTOS);
    }
//    @PostMapping("/lobby/removePlayerFromConfirms/{lobbyId}")
//    public ResponseEntity<String> removePlayerFromConfirms(@PathVariable UUID lobbyId, @RequestParam UUID playerId, Principal principal){
//        simulLobbyService.
//
//    }

}
