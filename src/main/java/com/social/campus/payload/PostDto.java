package com.social.campus.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private Integer postId;

    private Integer userId; // Extracted from the User entity

    private String content;

    private String mediaUrl;

    private Integer likesCount;

    private Integer commentsCount;

    private LocalDateTime createdAt;
}
