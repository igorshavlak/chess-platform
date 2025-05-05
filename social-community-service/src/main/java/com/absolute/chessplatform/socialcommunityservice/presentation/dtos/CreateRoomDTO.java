package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateRoomDTO {
    private String name;
    private String type;
    private UUID creatorId;
}
