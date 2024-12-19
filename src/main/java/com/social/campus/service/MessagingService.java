package com.social.campus.service;

import com.social.campus.entity.Conversation;
import com.social.campus.entity.Message;

import java.util.List;

public interface MessagingService {

    List<Conversation> getConversations(Integer userId);

    List<Message> getMessages(Integer conversationId);

    void sendMessage(Integer senderId, Integer receiverId, String content);

    void deleteMessage(Integer messageId);
}
