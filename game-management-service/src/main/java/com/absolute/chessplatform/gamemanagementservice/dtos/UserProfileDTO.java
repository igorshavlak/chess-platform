package com.absolute.chessplatform.gamemanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private UUID userId;
    private String username;
    private String name;
    private String surname;
    private String bio;
    private String locations;
    private String socialLinks;
}

