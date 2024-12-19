package com.social.campus.service;

import com.social.campus.entity.Story;

import java.util.List;

public interface StoryService {
    Story createStory(Integer userId, String contentUrl);

    List<Story> getUserStories(Integer userId);

    void viewStory(Integer userId,Integer storyId);

    void deleteStory(Integer storyId);
}
