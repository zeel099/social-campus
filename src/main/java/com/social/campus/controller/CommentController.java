package com.social.campus.controller;

import com.social.campus.payload.ApiResponse;
import com.social.campus.payload.CommentDto;
import com.social.campus.repository.CommentRepository;
import com.social.campus.response.ResponseHandler;
import com.social.campus.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Object> addComment(
            @PathVariable Integer userId,
            @PathVariable Integer postId,
            @RequestBody String content
    ) {
        CommentDto commentDto = commentService.addComment(userId, postId, content);
        return ResponseHandler.responseBuilder("Comment added successfully...", HttpStatus.OK,commentDto);
    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<Object> editComment(
            @PathVariable Integer commentId,
            @RequestBody String content
    ) {
        CommentDto commentDto = commentService.editComment(commentId, content);
        return ResponseHandler.responseBuilder("Comment updated successfully...",HttpStatus.OK,commentDto);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse("Comment deleted successfully!", true));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Object> getCommentsForPost(@PathVariable Integer postId) {
        List<CommentDto> comments = commentService.getCommentsForPost(postId);
        return ResponseHandler.responseBuilder("Fetch comments ",HttpStatus.OK,comments);
    }
}
