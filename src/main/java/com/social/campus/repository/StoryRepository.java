package com.social.campus.repository;

import com.social.campus.entity.Story;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Integer> {
    // Get all stories for a user
    List<Story> findByUser_UserId(Integer userId);

    // Get all stories (for display in feed, e.g., based on conditions)
    List<Story> findByExpiresAtAfter(LocalDateTime currentTime);
}
