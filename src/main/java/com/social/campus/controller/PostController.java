package com.social.campus.controller;

import com.social.campus.payload.ApiResponse;
import com.social.campus.payload.PostDto;
import com.social.campus.payload.UserDto;
import com.social.campus.repository.PostRepository;
import com.social.campus.response.ResponseHandler;
import com.social.campus.service.PostService;
import lombok.val;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostService postService;

    /**
     * 1. Create Post
     */
    @PostMapping("/create/{userId}")
    public ResponseEntity<Object>createPost(@RequestBody PostDto post,@PathVariable Integer userId){
        PostDto postDto = this.postService.createPost(post,userId);
        return ResponseHandler.responseBuilder("Post created successfully", HttpStatus.OK,postDto);
    }

    /**
     * 2. Update post
     */
    @PutMapping("/update/{postId}")
    public ResponseEntity<Object>updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId){
        PostDto updatePost = this.postService.updatePost(postDto,postId);
        return ResponseHandler.responseBuilder("Post updated successfully",HttpStatus.OK,updatePost);
    }
    /**
     * 3. Delete post
     */
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Object>deletePost(@val @PathVariable Integer postId){
        PostDto deletedPost = this.postService.deletePost(postId);
        return ResponseHandler.responseBuilder("Post deleted successfully",HttpStatus.OK,deletedPost);
    }
    /**
     * 4. Get post by id
     */
    @GetMapping("/getSinglePost/{postId}")
    public ResponseEntity<Object>getPostById(@val @PathVariable Integer postId){
        return ResponseEntity.ok(this.postService.getPostById(postId));
    }
    /**
     * 5. Get AllPost
     */
    @GetMapping("/getAllPost")
    public ResponseEntity<Object>getAllPost(){
        return ResponseEntity.ok(this.postService.getAllPost());
    }

    /**
     * 6. Like Post
     */
    @PostMapping("/{postId}/like/{userId}")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Integer userId, @PathVariable Integer postId) {
        postService.likePost(userId, postId);
        return ResponseEntity.ok(new ApiResponse("Post liked successfully!", true));
    }

    /**
     * 7. UnLike Post
     */
    @PostMapping("/{postId}/unlike/{userId}")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable Integer userId, @PathVariable Integer postId) {
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok(new ApiResponse("Post liked successfully!", true));
    }

    /**
     * 8. Get likes count
     */
    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Object> getLikesCount(@PathVariable Integer postId) {
        Integer count = postService.likesCount(postId);
       // return ResponseEntity.ok(new ApiResponse("Likes count fetched successfully!", true));
        return ResponseHandler.responseBuilder("Following count fetched successfully",HttpStatus.OK,count);
    }

    /**
     * 9. Get users who liked post
     */
    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<UserDto>> getUsersWhoLikedPost(@PathVariable Integer postId) {
        List<UserDto> users = postService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(users);
    }
    /**
     * 10. Upload media
     */
    @PostMapping("/{postId}/media/upload")
    public ResponseEntity<ApiResponse> uploadMedia(
            @PathVariable Integer postId,
            @RequestParam("media") MultipartFile media) {
        postService.uploadMedia(postId, media);
        return ResponseEntity.ok(new ApiResponse("Media uploaded successfully!", true));
    }
    /**
     * 11. Delete media
     */
    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<ApiResponse> deleteMedia(
            @PathVariable Integer postId,
            @PathVariable Integer mediaId) {
        postService.deleteMedia(postId, mediaId);
        return ResponseEntity.ok(new ApiResponse("Media deleted successfully!", true));
    }
    /**
     * 12. Save Post
     */
    @PostMapping("/{postId}/save/{userId}")
    public ResponseEntity<ApiResponse> savePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        System.out.println("controller savePost");
        this.postService.savePost(postId, userId);
        return ResponseEntity.ok(new ApiResponse("Post saved successfully!", true));
    }
    /**
     * 13. UnSave Post
     */
    @DeleteMapping("/{postId}/unsave/{userId}")
    public ResponseEntity<ApiResponse> unSavePost(@PathVariable Integer postId, @RequestParam Integer userId) {
        postService.unSavePost(postId, userId);
        return ResponseEntity.ok(new ApiResponse("Post unsaved successfully!", true));
    }
    /**
     * 14. Count post
     */
    @GetMapping("/count/{userId}")
    public ResponseEntity<Object> getPostCount(@PathVariable Integer userId) {
        Integer postCount = postService.getPostCount(userId);
        return ResponseHandler.responseBuilder("Post count fetched successfully", HttpStatus.OK, postCount);
    }
}
