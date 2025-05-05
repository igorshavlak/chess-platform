package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FriendRequestDTO {
    private  UUID id;
    private  UUID targetId;
    private  String status;
    private  Instant createdAt;
    private  Instant respondedAt;
    private UserProfileDTO profileDTO;
}
