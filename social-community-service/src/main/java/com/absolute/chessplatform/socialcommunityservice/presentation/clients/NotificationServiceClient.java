package com.absolute.chessplatform.socialcommunityservice.presentation.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "notification-service", url = "${notification-service.url}")
public interface NotificationServiceClient {
//    @PostMapping("/api/notifications/send-game-found/")
//    ResponseEntity<String> sendGameFoundNotification(@RequestBody GameFoundNotificationRequest request);
}
