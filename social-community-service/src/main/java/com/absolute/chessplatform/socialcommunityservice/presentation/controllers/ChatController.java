package com.absolute.chessplatform.socialcommunityservice.presentation.controllers;

import com.absolute.chessplatform.socialcommunityservice.applications.api.social.ChatService;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatMessage;
import com.absolute.chessplatform.socialcommunityservice.domain.social.models.ChatRoom;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.CreateRoomDTO;
import com.absolute.chessplatform.socialcommunityservice.presentation.dtos.SendMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/createChat")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody CreateRoomDTO createRoomDTO){
        ChatRoom room = chatService.createRoom(createRoomDTO.getName(), createRoomDTO.getType(),createRoomDTO.getCreatorId());
        return ResponseEntity.ok(room);
    }
    @PostMapping("/send/{roomId}/messages")
    public ResponseEntity<ChatMessage> sendMessage(@PathVariable UUID roomId, @RequestBody SendMessageDTO sendMessageDTO){
        ChatMessage chatMessage = chatService.sendMessage(roomId, sendMessageDTO.getSenderId(),sendMessageDTO.getContent());
        return ResponseEntity.ok(chatMessage);
    }
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> listMessages(@PathVariable UUID roomId) {
        List<ChatMessage> list = chatService.listMessages(roomId);
        return ResponseEntity.ok(list);
    }

}
