package com.absolute.chessplatform.socialcommunityservice.domain.social.repositories;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatParticipant;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository {
    ChatMessage saveMessage(ChatMessage message);
    ChatParticipant saveParticipant(ChatParticipant chatParticipant);
    ChatRoom saveChatRoom(ChatRoom chatRoom);

    List<ChatMessage> findMessagesByRoom(UUID roomId);
    List<ChatParticipant> findChatParticipantsByRoom(UUID roomId);
    Optional<ChatRoom> findChatRoomById(UUID roomId);
    List<ChatRoom> findByCreator(UUID creatorId);

}
