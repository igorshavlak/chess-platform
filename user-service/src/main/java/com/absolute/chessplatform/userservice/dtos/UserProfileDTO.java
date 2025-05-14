package com.absolute.chessplatform.userservice.dtos;

import com.absolute.chessplatform.userservice.entities.UserProfile;
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

    public static UserProfileDTO fromEntity(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        return new UserProfileDTO(
                userProfile.getUserId(),
                userProfile.getUsername(),
                userProfile.getName(),
                userProfile.getSurname(),
                userProfile.getBio(),
                userProfile.getLocations(),
                userProfile.getSocialLinks()
        );
    }
}

