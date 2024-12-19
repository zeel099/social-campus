package com.social.campus.service;

import com.social.campus.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Integer userId, Integer postId, String content);

    CommentDto editComment(Integer commentId, String content);

    void deleteComment(Integer commentId);

    List<CommentDto> getCommentsForPost(Integer postId);
}
