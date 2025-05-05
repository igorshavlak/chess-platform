package com.absolute.chessplatform.gamemanagementservice.clients;


import com.absolute.chessplatform.gamemanagementservice.dtos.SimulSessionDTO;
import com.absolute.chessplatform.gamemanagementservice.entities.SimulSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "notification-service", url = "${notification-service.url}/api/notifications")
public interface NotificationServiceClient {
    @PostMapping("/sendSimulLobby")
    ResponseEntity<String> sendSimulLobby(@RequestBody SimulSessionDTO simulSessionDTO);
}
