package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FriendshipDTO {
    private UUID id;
    private UUID friendId;
    private Instant since;
    private UserProfileDTO friendProfile;
}
