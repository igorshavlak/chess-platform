package com.absolute.chessplatform.socialcommunityservice.applications.api.social;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatParticipant;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatRoom createRoom (String name, String type, UUID creatorId);
    ChatParticipant joinRoom(UUID roomId, UUID userId);
    ChatMessage sendMessage(UUID roomId, UUID senderId, String content);
    List<ChatMessage> listMessages(UUID roomId);

}
