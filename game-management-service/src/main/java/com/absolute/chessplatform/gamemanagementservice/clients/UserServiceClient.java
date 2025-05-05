package com.absolute.chessplatform.gamemanagementservice.clients;

import com.absolute.chessplatform.gamemanagementservice.dtos.UserProfileDTO;
import com.absolute.chessplatform.gamemanagementservice.dtos.UserStatisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Component
@FeignClient(name = "user-service", url = "${user-service.url}/api/users")
public interface UserServiceClient {
    @GetMapping("/userProfile/{userId}")
    ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable UUID userId);

    @GetMapping("/statistics/classic/{userId}")
    ResponseEntity<UserStatisticsDTO> getClassicStats(@PathVariable UUID userId);

}
