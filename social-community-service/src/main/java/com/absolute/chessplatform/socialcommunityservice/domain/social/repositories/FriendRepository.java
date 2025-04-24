package com.absolute.chessplatform.socialcommunityservice.domain.social.repositories;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository {
    //friendship
    Friendship saveFriendship(Friendship friendship);
    List<Friendship> findFriendshipByUser(UUID userID);
    void deleteFriendship(Friendship friendship);


    //friendRequest
    FriendRequest saveFriendRequest(FriendRequest friendship);
    Optional<FriendRequest> findFriendRequestById(UUID userID);
    void deleteFriendRequest(FriendRequest friendRequest);
    List<FriendRequest> findFriendRequestsByTargetId(UUID targetId);




}
