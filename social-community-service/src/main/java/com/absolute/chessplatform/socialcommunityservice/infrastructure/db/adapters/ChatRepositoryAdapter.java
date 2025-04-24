package com.absolute.chessplatform.socialcommunityservice.infrastructure.db.adapters;

import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatParticipant;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;
import com.absolute.chessplatform.socialcommunityservice.domain.social.repositories.ChatRepository;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatMessageEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.entities.ChatRoomEntity;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers.ChatMessageMapper;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.mappers.ChatRoomMapper;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories.ChatMessageJpaRepository;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories.ChatParticipantJpaRepository;
import com.absolute.chessplatform.socialcommunityservice.infrastructure.db.repositories.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryAdapter implements ChatRepository {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatParticipantJpaRepository chatParticipantJpaRepository;


    @Override
    public ChatMessage saveMessage(ChatMessage message) {
        ChatMessageEntity e = ChatMessageMapper.toEntity(message);
        ChatMessageEntity saved = chatMessageJpaRepository.save(e);
        return ChatMessageMapper.toDomain(saved);
    }

    @Override
    public ChatParticipant saveParticipant(ChatParticipant chatParticipant) {
        return null;
    }

    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = ChatRoomMapper.toEntity(chatRoom);
        ChatRoomEntity saved = chatRoomJpaRepository.save(chatRoomEntity);
        return ChatRoomMapper.toDomain(saved);
    }

    @Override
    public List<ChatMessage> findMessagesByRoom(UUID roomId) {
        return List.of();
    }

    @Override
    public List<ChatParticipant> findChatParticipantsByRoom(UUID roomId) {
        return List.of();
    }

    @Override
    public Optional<ChatRoom> findChatRoomById(UUID roomId) {
        return Optional.empty();
    }

    @Override
    public List<ChatRoom> findByCreator(UUID creatorId) {
        return List.of();
    }
}
