package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SendFriendRequestDTO {
    private UUID fromUserId;
    private UUID toUserId;
}
