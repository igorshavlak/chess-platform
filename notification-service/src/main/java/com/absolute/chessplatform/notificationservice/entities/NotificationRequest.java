package com.absolute.chessplatform.notificationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class NotificationRequest {
    private UUID userId;
    private String message;
}
