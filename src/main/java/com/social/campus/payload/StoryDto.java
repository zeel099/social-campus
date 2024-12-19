package com.social.campus.payload;

import com.social.campus.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoryDto {

    private User user;

    private String contentUrl; // URL of the story content (image/video)

    private LocalDateTime createdAt; // Timestamp when the story was created

    private LocalDateTime expiresAt; // Expiry timestamp (e.g., 24 hours later)

    private Integer viewsCount = 0;
}
