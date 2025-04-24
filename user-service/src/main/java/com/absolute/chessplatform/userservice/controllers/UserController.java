package com.absolute.chessplatform.userservice.controllers;

import com.absolute.chessplatform.userservice.dtos.GameResultDTO;
import com.absolute.chessplatform.userservice.dtos.UserStatisticsDTO;
import com.absolute.chessplatform.userservice.entities.UserStatistics;
import com.absolute.chessplatform.userservice.services.UserService;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final Keycloak keycloak;
    private final UserService userService;

    @PostMapping("/updateStatistic/{id}")
    public ResponseEntity<?> updateUserStatistic(@PathVariable UUID id, @RequestBody GameResultDTO gameResultDTO) {
        userService.updateUserStatistic(id, gameResultDTO);
        return ResponseEntity.ok().build();
    }

}
