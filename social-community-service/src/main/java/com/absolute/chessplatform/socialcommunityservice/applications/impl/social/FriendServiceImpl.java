package com.absolute.chessplatform.socialcommunityservice.applications.impl.social;

import com.absolute.chessplatform.socialcommunityservice.applications.api.social.FriendService;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.adapters.FriendRepositoryAdapter;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.exceptions.ResourceNotFoundException;
import com.absolute.chessplatform.socialcommunityservice.presentation.clients.UserServiceClient;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.FriendRequestDTO;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.FriendshipDTO;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.UserProfileDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepositoryAdapter friendRepositoryAdapter;
    private final UserServiceClient userServiceClient;

    @Override
    public FriendRequest sendRequest(UUID fromUserId, UUID toUserId) {
        FriendRequest friendRequest = new FriendRequest(
                UUID.randomUUID(),
                fromUserId,
                toUserId,
                "PENDING",
                Instant.now(),
                null
        );
        return friendRepositoryAdapter.saveFriendRequest(friendRequest);
    }

    @Override
    @Transactional
    public void acceptRequest(UUID requestId) {
        FriendRequest req = friendRepositoryAdapter.findFriendRequestById(requestId).orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));
        Friendship friendship = new Friendship(
                UUID.randomUUID(),
                req.getRequesterId(),
                req.getTargetId(),
                Instant.now()
        );
        friendRepositoryAdapter.saveFriendship(friendship);
        Friendship friendship1 = new Friendship(
                UUID.randomUUID(),
                req.getTargetId(),
                req.getRequesterId(),
                Instant.now()
        );
        friendRepositoryAdapter.saveFriendship(friendship1);
        friendRepositoryAdapter.deleteFriendRequest(req);
    }

    @Override
    public void rejectRequest(UUID requestId) {
        FriendRequest r = friendRepositoryAdapter.findFriendRequestById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));
        FriendRequest updated = new FriendRequest(
                r.getId(),
                r.getRequesterId(),
                r.getTargetId(),
                "REJECTED",
                r.getCreatedAt(),
                Instant.now()
        );
        friendRepositoryAdapter.saveFriendRequest(updated);
    }

    @Transactional
    @Override
    public void removeFriend(UUID userId, UUID friendId) {
        friendRepositoryAdapter.findFriendshipByUser(userId).stream()
                .filter(f -> f.getFriendId().equals(friendId))
                .forEach(friendRepositoryAdapter::deleteFriendship);
        friendRepositoryAdapter.findFriendshipByUser(friendId).stream()
                .filter(f -> f.getFriendId().equals(userId))
                .forEach(friendRepositoryAdapter::deleteFriendship);
    }

    @Override
    public List<FriendshipDTO> listFriends(UUID userId) {
        List<Friendship> friendships = friendRepositoryAdapter.findFriendshipByUser(userId);
        if (friendships.isEmpty()) {
            return Collections.emptyList();
        }
        List<UUID> friendIds = friendships.stream()
                .map(Friendship::getFriendId)
                .toList();
        ResponseEntity<List<UserProfileDTO>> response = userServiceClient.getUserProfiles(friendIds);
        if (!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Failed to fetch friend profiles, status: " + response.getStatusCode());
        }
        List<UserProfileDTO> profiles = response.getBody();
        if (profiles == null || profiles.isEmpty()) {
            throw new ResourceNotFoundException("Friend profiles not found");
        }
        Map<UUID, UserProfileDTO> profileMap = profiles.stream()
                .collect(Collectors.toMap(UserProfileDTO::getId, Function.identity()));


        return friendships.stream()
                .map(friendship -> {
                    UserProfileDTO profile = profileMap.get(friendship.getFriendId());
                    if (profile == null) {
                        throw new ResourceNotFoundException(
                                "User profile not found for id: " + friendship.getFriendId());
                    }
                    return new FriendshipDTO(
                            friendship.getId(),
                            friendship.getFriendId(),
                            friendship.getSince(),
                            profile
                    );
                })
                .toList();
    }

    @Override
    public List<FriendRequestDTO> listRequests(UUID targetId) {
        List<FriendRequest> requests = friendRepositoryAdapter.findFriendRequestsByTargetId(targetId);
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }
        List<UUID> requesterIds = requests.stream()
                .map(FriendRequest::getRequesterId)
                .toList();

        ResponseEntity<List<UserProfileDTO>> response =
                userServiceClient.getUserProfiles(requesterIds);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("Failed to fetch requester profiles, status: "
                    + response.getStatusCode());
        }
        List<UserProfileDTO> profiles = response.getBody();
        if (profiles == null || profiles.isEmpty()) {
            throw new ResourceNotFoundException("Requester profiles not found");
        }

        Map<UUID, UserProfileDTO> profileMap = profiles.stream()
                .collect(Collectors.toMap(UserProfileDTO::getId, Function.identity()));

        return requests.stream()
                .map(request -> {
                    UserProfileDTO profile = profileMap.get(request.getRequesterId());
                    if (profile == null) {
                        throw new ResourceNotFoundException(
                                "User profile not found for id: " + request.getRequesterId());
                    }
                    return new FriendRequestDTO(
                            request.getId(),
                            request.getRequesterId(),
                            request.getStatus(),
                            request.getCreatedAt(),
                            request.getRespondedAt(),
                            profile
                    );
                })
                .toList();
    }

}
