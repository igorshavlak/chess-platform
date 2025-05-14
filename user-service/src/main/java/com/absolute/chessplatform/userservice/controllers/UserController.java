package com.absolute.chessplatform.userservice.controllers;

import com.absolute.chessplatform.userservice.dtos.GameResultDTO;
import com.absolute.chessplatform.userservice.dtos.UserProfileDTO;
import com.absolute.chessplatform.userservice.dtos.UserStatisticsDTO;
import com.absolute.chessplatform.userservice.entities.UserStatistics;
import com.absolute.chessplatform.userservice.services.UserService;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/updateStatistic/{id}")
    public ResponseEntity<?> updateUserStatistic(@PathVariable UUID id, @RequestBody GameResultDTO gameResultDTO) {
        userService.updateUserStatistic(id, gameResultDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getUsersProfiles/")
    public ResponseEntity<List<UserProfileDTO>> getUserProfiles(@RequestParam("ids") List<UUID> ids) {
        List<UserProfileDTO> userProfilesDTO = userService.getUsersProfilesByIds(ids);
        return ResponseEntity.ok(userProfilesDTO);

    }
    @GetMapping("/statistics/classic/{userId}")
    public ResponseEntity<UserStatisticsDTO> getClassicStats(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getClassicStatistics(userId));
    }
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
    @GetMapping("/search")
    public ResponseEntity<List<UserProfileDTO>> searchUsers(
            @RequestParam String nickname,
            @RequestParam UUID requesterId
    ) {
        List<UserProfileDTO> results = userService.searchUsersByNickname(nickname, requesterId);
        return ResponseEntity.ok(results);
    }
}
