package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.adapters;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;
import com.absolute.chessplatform.socialcommunityservice.domain.social.repositories.FriendRepository;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendRequestEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.FriendshipEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers.FriendRequestMapper;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers.FriendshipMapper;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories.FriendRequestJpaRepository;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories.FriendshipJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryAdapter implements FriendRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;
    private final FriendRequestJpaRepository friendRequestJpaRepository;

    @Override
    public Friendship saveFriendship(Friendship friendship) {
        FriendshipEntity friendshipEntity = FriendshipMapper.toEntity(friendship);
        FriendshipEntity saved = friendshipJpaRepository.save(friendshipEntity);
        return FriendshipMapper.toDomain(saved);
    }

    @Override
    public List<Friendship> findFriendshipByUser(UUID userID) {
        return friendshipJpaRepository.findByUserId(userID)
                .stream()
                .map(FriendshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        FriendshipEntity friendshipEntity = FriendshipMapper.toEntity(friendship);
        friendshipJpaRepository.delete(friendshipEntity);
    }

    @Override
    public void deleteFriendRequest(FriendRequest friendRequest) {
        FriendRequestEntity friendRequestEntity = FriendRequestMapper.toEntity(friendRequest);
        friendRequestJpaRepository.delete(friendRequestEntity);
    }

    @Override
    public List<FriendRequest> findFriendRequestsByTargetId(UUID targetId) {
        return friendRequestJpaRepository.findByTargetId(targetId)
                .stream()
                .map(FriendRequestMapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public FriendRequest saveFriendRequest(FriendRequest request) {
        FriendRequestEntity entity = FriendRequestMapper.toEntity(request);
        FriendRequestEntity saved = friendRequestJpaRepository.save(entity);
        return FriendRequestMapper.toDomain(saved);
    }

    @Override
    public Optional<FriendRequest> findFriendRequestById(UUID id) {
        return friendRequestJpaRepository.findById(id)
                .map(FriendRequestMapper::toDomain);
    }

}
