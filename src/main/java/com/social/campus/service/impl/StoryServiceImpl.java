package com.social.campus.service.impl;

import com.social.campus.entity.Story;
import com.social.campus.entity.User;
import com.social.campus.exception.ResourceNotFoundException;
import com.social.campus.repository.StoryRepository;
import com.social.campus.repository.UserRepository;
import com.social.campus.service.StoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Override
    public Story createStory(Integer userId, String contentUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Story story = new Story();
        story.setUser(user);
        story.setContentUrl(contentUrl);
        story.setCreatedAt(LocalDateTime.now());
        story.setExpiresAt(LocalDateTime.now().plusHours(24)); // Story expires in 24 hours

        return storyRepository.save(story);
    }

    @Override
    public List<Story> getUserStories(Integer userId) {
        return storyRepository.findByUser_UserId(userId);
    }

    @Transactional
    @Override
    public void viewStory(Integer userId, Integer storyId) {
        // Fetch story by ID
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "storyId", storyId));

        // Fetch user by ID (viewer)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Add the user to the story's viewers if not already viewed
        if (!story.getViewedBy().contains(user)) {
            story.getViewedBy().add(user);
            story.setViewCount(story.getViewCount() + 1); // Increment the view count
            storyRepository.save(story);
        } else {
            throw new IllegalStateException("User has already viewed this story.");
        }
    }

    @Override
    public void deleteStory(Integer storyId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "storyId", storyId));

        storyRepository.delete(story);
    }
}
