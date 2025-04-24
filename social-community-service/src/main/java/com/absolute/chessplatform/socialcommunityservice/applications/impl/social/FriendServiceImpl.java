package com.absolute.chessplatform.socialcommunityservice.applications.impl.social;

import com.absolute.chessplatform.socialcommunityservice.applications.api.social.FriendService;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.adapters.FriendRepositoryAdapter;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepositoryAdapter friendRepositoryAdapter;

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
    public List<Friendship> listFriends(UUID userId) {
        return friendRepositoryAdapter.findFriendshipByUser(userId);
    }

    @Override
    public List<FriendRequest> listRequests(UUID targetId) {
        return friendRepositoryAdapter.findFriendRequestsByTargetId(targetId);
    }
}
