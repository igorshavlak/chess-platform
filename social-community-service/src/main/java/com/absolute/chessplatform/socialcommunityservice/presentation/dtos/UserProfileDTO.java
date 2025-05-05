package com.absolute.chessplatform.socialcommunityservice.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private UUID id;
    private String username;
    private String name;
    private String surname;
    private String bio;
    private String locations;
    private String socialLinks;
}

