package com.social.campus.repository;

import com.social.campus.entity.Message;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySender_UserIdOrReceiver_UserId(Integer senderId, Integer receiverId);
    List<Message> findByConversationId(Integer conversationId);
}
