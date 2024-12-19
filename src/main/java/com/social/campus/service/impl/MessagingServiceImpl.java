package com.social.campus.service.impl;

import com.social.campus.entity.Conversation;
import com.social.campus.entity.Message;
import com.social.campus.entity.User;
import com.social.campus.exception.ResourceNotFoundException;
import com.social.campus.repository.ConversationRepository;
import com.social.campus.repository.MessageRepository;
import com.social.campus.repository.UserRepository;
import com.social.campus.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessagingServiceImpl implements MessagingService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Conversation> getConversations(Integer userId) {
        return conversationRepository.findAll()
                .stream()
                .filter(conversation -> conversation.getUser1().getUserId().equals(userId)
                        || conversation.getUser2().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessages(Integer conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    @Override
    public void sendMessage(Integer senderId, Integer receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", receiverId));

        Optional<Conversation> conversationOptional = conversationRepository
                .findByUser1_UserIdAndUser2_UserId(senderId, receiverId);

        Conversation conversation = conversationOptional.orElseGet(() -> {
            Conversation newConversation = new Conversation();
            newConversation.setUser1(sender);
            newConversation.setUser2(receiver);
            return conversationRepository.save(newConversation);
        });

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        messageRepository.delete(message);
    }

}
