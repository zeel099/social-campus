package com.social.campus.controller;

import com.social.campus.entity.Conversation;
import com.social.campus.entity.Message;
import com.social.campus.payload.ApiResponse;
import com.social.campus.service.MessagingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessagingController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessagingService messagingService;

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<Conversation>> getConversations(@PathVariable Integer userId) {
        List<Conversation> conversations = messagingService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Integer conversationId) {
        List<Message> messages = messagingService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{senderId}/send/{receiverId}")
    public ResponseEntity<ApiResponse> sendMessage(
            @PathVariable Integer senderId,
            @PathVariable Integer receiverId,
            @RequestParam String content
    ) {
        messagingService.sendMessage(senderId, receiverId, content);
        return ResponseEntity.ok(new ApiResponse("Message sent successfully!", true));
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessage(@PathVariable Integer messageId) {
        messagingService.deleteMessage(messageId);
        return ResponseEntity.ok(new ApiResponse("Message deleted successfully!", true));
    }
}
