package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SendMessageDTO {
    private UUID senderId;
    private String content;
}
