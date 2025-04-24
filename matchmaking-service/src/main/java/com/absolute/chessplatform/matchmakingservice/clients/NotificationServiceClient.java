package com.absolute.chessplatform.matchmakingservice.clients;

import com.absolute.chessplatform.matchmakingservice.entities.GameFoundNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "notification-service", url = "${notification-service.url}")
public interface NotificationServiceClient {
    @PostMapping("/api/notifications/send-game-found/")
    ResponseEntity<String> sendGameFoundNotification(@RequestBody GameFoundNotificationRequest request);
}
