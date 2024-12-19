package com.social.campus.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Integer commentId;

    private String content;

    private Integer userId;

    private Integer postId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
