package com.absolute.chessplatform.socialcommunityservice.presentation.controllers;

import com.absolute.chessplatform.socialcommunityservice.applications.api.social.FriendService;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.FriendRequest;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.Friendship;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.FriendRequestDTO;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.FriendshipDTO;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.SendFriendRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/sendRequest")
    public ResponseEntity<FriendRequest> sendRequest(@RequestBody SendFriendRequestDTO sendFriendRequestDTO) {
        FriendRequest friendRequest = friendService.sendRequest(sendFriendRequestDTO.getFromUserId(), sendFriendRequestDTO.getToUserId());
        return ResponseEntity.ok(friendRequest);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDTO>> listRequests(@RequestParam UUID targetId) {
        List<FriendRequestDTO> list = friendService.listRequests(targetId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable UUID requestId) {
        friendService.acceptRequest(requestId);
        return ResponseEntity.ok("request was accepted");
    }

    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable UUID requestId) {
        friendService.rejectRequest(requestId);
        return ResponseEntity.ok("request was rejected");
    }

    @GetMapping("/listFriends")
    public ResponseEntity<List<FriendshipDTO>> listFriends(@RequestParam UUID userId) {
        List<FriendshipDTO> list = friendService.listFriends(userId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{userId}/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.noContent().build();
    }
}



