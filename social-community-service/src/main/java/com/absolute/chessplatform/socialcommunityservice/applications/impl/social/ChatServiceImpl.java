package com.absolute.chessplatform.socialcommunityservice.applications.impl.social;

import com.absolute.chessplatform.socialcommunityservice.applications.api.social.ChatService;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatParticipant;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.adapters.ChatRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepositoryAdapter chatRepositoryAdapter;

    @Override
    public ChatRoom createRoom(String name, String type, UUID creatorId) {
        ChatRoom chatRoom = new ChatRoom(
                UUID.randomUUID(),
                name,
                type,
                creatorId,
                Instant.now()
        );
        return chatRepositoryAdapter.saveChatRoom(chatRoom);
    }

    @Override
    public ChatParticipant joinRoom(UUID roomId, UUID userId) {
        ChatParticipant chatParticipant = new ChatParticipant(
                roomId,
                userId,
                Instant.now()
        );
        return chatRepositoryAdapter.saveParticipant(chatParticipant);
    }

    @Override
    public ChatMessage sendMessage(UUID roomId, UUID senderId, String content) {
        ChatMessage chatMessage = new ChatMessage(
                UUID.randomUUID(),
                roomId,
                senderId,
                content,
                Instant.now(),
                "SENT"
        );
        return chatRepositoryAdapter.saveMessage(chatMessage);
    }

    @Override
    public List<ChatMessage> listMessages(UUID roomId) {
        return chatRepositoryAdapter.findMessagesByRoom(roomId);
    }
}
