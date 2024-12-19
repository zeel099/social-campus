package com.social.campus.repository;

import com.social.campus.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ConversationRepository extends JpaRepository<Conversation,Integer> {
    Optional<Conversation> findByUser1_UserIdAndUser2_UserId(Integer user1Id, Integer user2Id);
}
