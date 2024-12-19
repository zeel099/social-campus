package com.social.campus.controller;

import com.social.campus.entity.Story;
import com.social.campus.payload.ApiResponse;
import com.social.campus.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story")
public class StoryController {

    @Autowired
    private StoryService storyService;

    /**
     * 1. Create Story
     */
    @PostMapping("/{userId}/create")
    public ResponseEntity<ApiResponse> createStory(
            @PathVariable Integer userId,
            @RequestParam String contentUrl
    ) {
        storyService.createStory(userId, contentUrl);
        return ResponseEntity.ok(new ApiResponse("Story created successfully!", true));
    }

    /**
     * 2. Get User Stories
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserStories(@PathVariable Integer userId) {
        List<Story> stories = storyService.getUserStories(userId);
        return ResponseEntity.ok(stories);
    }

    /**
     * 3. View Story
     */
    @PostMapping("/{storyId}/view/{userId}")
    public ResponseEntity<ApiResponse> viewStory(
            @PathVariable Integer userId,
            @PathVariable Integer storyId
    ) {
        storyService.viewStory(userId, storyId);
        return ResponseEntity.ok(new ApiResponse("Story viewed successfully!", true));
    }

    /**
     * 4. Delete Story
     */
    @DeleteMapping("/{storyId}/delete")
    public ResponseEntity<ApiResponse> deleteStory(@PathVariable Integer storyId) {
        storyService.deleteStory(storyId);
        return ResponseEntity.ok(new ApiResponse("Story deleted successfully!", true));
    }
}
