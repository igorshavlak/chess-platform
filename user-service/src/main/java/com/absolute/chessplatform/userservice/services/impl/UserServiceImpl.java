package com.absolute.chessplatform.userservice.services.impl;

import com.absolute.chessplatform.userservice.dtos.*;
import com.absolute.chessplatform.userservice.entities.CreateUserRequest;
import com.absolute.chessplatform.userservice.entities.UserProfile;
import com.absolute.chessplatform.userservice.entities.UserSettings;
import com.absolute.chessplatform.userservice.entities.UserStatistics;
import com.absolute.chessplatform.userservice.exceptions.ResourceNotFoundException;
import com.absolute.chessplatform.userservice.repositories.UserProfileRepository;
import com.absolute.chessplatform.userservice.repositories.UserSettingsRepository;
import com.absolute.chessplatform.userservice.repositories.UserStatisticsRepository;
import com.absolute.chessplatform.userservice.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Keycloak keycloak;
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserSettingsRepository userSettingsRepository;
    @Value("${keycloak.realm}")
    private String realm;

//    @Transactional
//    public String createUser(@Validated  CreateUserRequest request, BindingResult bindingResult) {
//
//        UserRepresentation user = new UserRepresentation();
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEnabled(true);
//
//        UsersResource usersResource = keycloak.realm(realm).users();
//        String userId;
//        try (Response response = usersResource.create(user)) {
//            if (response.getStatus() != 201) {
//                throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo().getReasonPhrase());
//            }
//
//            userId = extractUserId(response.getLocation().getPath());
//        }
//
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            usersResource.get(userId).resetPassword(createPasswordCredential(request.getPassword()));
//        }
//
//        UUID userUUID = UUID.fromString(userId);
//
//        UserStatistics statistics = new UserStatistics(UUID.randomUUID(),  0, 0, 0);
//        userStatisticsRepository.save(statistics);
//
//        if (request.getProfile() != null) {
//            UserProfileDTO profileDTO = request.getProfile();
//            UserProfile profile = new UserProfile(
//                    UUID.randomUUID(),
//                    userUUID,
//                    profileDTO.getBio(),
//                    profileDTO.getLocations(),
//                    profileDTO.getSocialLinks()
//            );
//            userProfileRepository.save(profile);
//        }
//
//        if (request.getSettings() != null) {
//            UserSettingsDTO settingsDTO = request.getSettings();
//            UserSettings settings = new UserSettings(
//                    UUID.randomUUID(),
//                    userUUID,
//                    settingsDTO.getPreferences(),
//                    settingsDTO.isNotifications()
//            );
//            userSettingsRepository.save(settings);
//        }
//
//        return userId;
//    }
    private String extractUserId(String path) {
        String[] segments = path.split("/");
        return segments[segments.length - 1];
    }
    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

//
//    @Override
//    public UserResponseDTO getUserById(String id) {
//        UserRepresentation user = keycloak.realm(realm).users().get(id).toRepresentation();
//        UserStatistics statistics = userStatisticsRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User statistics not found for ID: " + id));
//        UserProfile profile = userProfileRepository.findByUserId(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User profile not found for ID: " + id));
//        UserSettings settings = userSettingsRepository.findByUserId(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User settings not found for ID: " + id));
//        UserProfileDTO profileDTO = null;
//        if (profile != null) {
//            profileDTO = new UserProfileDTO();
//            profileDTO.setBio(profile.getBio());
//            profileDTO.setLocations(profile.getLocations());
//            profileDTO.setSocialLinks(profile.getSocialLinks());
//        }
//        UserSettingsDTO settingsDTO = null;
//        if (settings != null) {
//            settingsDTO = new UserSettingsDTO();
//            settingsDTO.setPreferences(settings.getPreferences());
//            settingsDTO.setNotifications(settings.isNotifications());
//        }
//        UserStatisticsDTO statisticsDTO = null;
//        if(statistics != null) {
//            statisticsDTO = new UserStatisticsDTO();
//            statisticsDTO.setRating(statistics.getRating());
//            statisticsDTO.setGamesPlayed(statistics.getGamesPlayed());
//            statisticsDTO.setGamesWon(statistics.getGamesWon());
//        }
//        return UserResponseDTO.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName())
//                        .settings(settingsDTO).profile(profileDTO).statistics(statisticsDTO).build();
//    }
    public void updateUserStatistic(UUID userId, GameResultDTO gameResultDTO) {
        UserStatistics userStatistics = userStatisticsRepository
                .findById_KeycloakIdAndId_Mode(userId,gameResultDTO.gameMode)
                .orElseThrow(() -> new ResourceNotFoundException("User statistics not found for ID: " + userId));
        userStatistics.setGamesPlayed(userStatistics.getGamesPlayed() + 1);
        if(gameResultDTO.isWinner){
            userStatistics.setGamesWon(userStatistics.getGamesWon() + 1);
        } else if(gameResultDTO.isDrawn){
            userStatistics.setGamesDrawn(userStatistics.getGamesDrawn() + 1);
        } else {
            userStatistics.setGamesLost(userStatistics.getGamesLost() + 1);
        }
        if(gameResultDTO.isRating){
            //...
        }
        userStatisticsRepository.save(userStatistics);

    }
}
