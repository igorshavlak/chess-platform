package com.absolute.chessplatform.gamemanagementservice.services;

import com.absolute.chessplatform.gamemanagementservice.entities.ConcludeGameNotification;
import com.absolute.chessplatform.gamemanagementservice.entities.GameStatus;

import java.util.UUID;

public interface NotificationService {
    void sendGameConcludedNotification(ConcludeGameNotification notification);
}
