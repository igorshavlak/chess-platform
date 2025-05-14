package com.absolute.chessplatform.userservice.services;

import com.absolute.chessplatform.userservice.dtos.GameResultDTO;
import com.absolute.chessplatform.userservice.dtos.UserProfileDTO;
import com.absolute.chessplatform.userservice.dtos.UserResponseDTO;
import com.absolute.chessplatform.userservice.dtos.UserStatisticsDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {
     void updateUserStatistic(UUID id, GameResultDTO gameResultDTO);
     List<UserProfileDTO> getUsersProfilesByIds(List<UUID> uuids);
     UserStatisticsDTO getClassicStatistics(UUID userId);
     UserProfileDTO getUserProfile(UUID userId);
     List<UserProfileDTO> searchUsersByNickname(String nickname,  UUID requesterId);
}
