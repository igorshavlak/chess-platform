package com.absolute.chessplatform.socialcommunityservice.presentation.clients;

import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.UserProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Component
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {
        @GetMapping("/api/users/getUsersProfiles/")
        ResponseEntity<List<UserProfileDTO>> getUserProfiles(@RequestParam("ids") List<UUID> ids);
}
