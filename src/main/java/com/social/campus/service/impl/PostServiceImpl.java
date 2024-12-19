package com.social.campus.service.impl;

import com.social.campus.entity.Like;
import com.social.campus.entity.Post;
import com.social.campus.entity.PostMedia;
import com.social.campus.entity.User;
import com.social.campus.exception.ResourceNotFoundException;
import com.social.campus.payload.PostDto;
import com.social.campus.payload.UserDto;
import com.social.campus.repository.LikeRepository;
import com.social.campus.repository.PostMediaRepository;
import com.social.campus.repository.PostRepository;
import com.social.campus.repository.UserRepository;
import com.social.campus.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository  postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private LikeRepository likeRepository;

    private final String mediaUploadDir = "uploads/post-media/";

    @Override
    public PostDto createPost(PostDto postDto,Integer userId) {
        Post post = this.modelMapper.map(postDto, Post.class);
        Post newPost = this.postRepository.save(post);
        return this.modelMapper.map(newPost,PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        System.out.println("PostId:"+postId);
        Post post = this.postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","postId",postId));
        post.setContent(postDto.getContent());
        post.setMediaUrl(postDto.getMediaUrl());
        post.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = this.postRepository.save(post);
        return this.modelMapper.map(updatedPost,PostDto.class);
    }

    @Override
    public PostDto deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        this.postRepository.delete(post);
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPost() {
        List<Post>posts = this.postRepository.findAll();
        List<PostDto>postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public void likePost(Integer userId, Integer postId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

//        if (this.likeRepository.existsByUser_UserIdAndPost_PostId(userId, postId)) {
//            throw new IllegalStateException("User has already liked this post.");
//        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setCreatedAt(LocalDateTime.now());

        this.likeRepository.save(like);

        post.setLikesCount(post.getLikesCount() + 1);
        this.postRepository.save(post);
    }

    @Override
    public void unlikePost(Integer userId, Integer postId) {
        // Fetch the like record
        //Like like = this.likeRepository.findByUserIdAndPostId(userId, postId);
                //.orElseThrow(() -> new ResourceNotFoundException("Like", "postId", postId));

//        this.likeRepository.delete(like);
//
//        Post post = like.getPost();
//        post.setLikesCount(post.getLikesCount() - 1);
//        this.postRepository.save(post);
    }

//    @Override
//    public Integer likesCount(Integer postId) {
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
//
//        return post.getLikedBy().size();
//    }
    @Override
    public Integer likesCount(Integer postId) {
        // Fetch the count directly using the custom query
        return postRepository.countLikesByPostId(postId);
    }

    @Override
    public List<UserDto> getUsersWhoLikedPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        return post.getLikedBy().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public void uploadMedia(Integer postId, MultipartFile media) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

            Files.createDirectories(Paths.get(mediaUploadDir));

            String fileName = UUID.randomUUID().toString() + "_" + media.getOriginalFilename();
            String filePath = mediaUploadDir + fileName;
            media.transferTo(new File(filePath));

            PostMedia postMedia = new PostMedia();
            postMedia.setMediaUrl(filePath);
            postMedia.setPost(post);

            postMediaRepository.save(postMedia);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while uploading media.", e);
        }
    }


    @Override
    public void deleteMedia(Integer postId, Integer mediaId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        PostMedia media = postMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "mediaId", mediaId));

        if (!media.getPost().getPostId().equals(postId)) {
            throw new IllegalArgumentException("Media does not belong to the specified post.");
        }

        try {
            Files.deleteIfExists(Paths.get(media.getMediaUrl()));
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while deleting media file.", e);
        }

        postMediaRepository.delete(media);
    }

    @Override
    public List<PostDto> fetchPostsByTag(String tag, Integer userId) {
        return List.of();
    }

    @Override
    public void savePost(Integer postId,Integer userId) {
        System.out.println("service savePost....");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        if (user.getSavedPosts().contains(post)) {
            throw new IllegalArgumentException("Post is already saved by the user.");
        }

        user.getSavedPosts().add(post);
        userRepository.save(user);
    }

    @Override
    public void unSavePost(Integer postId,Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        if (!user.getSavedPosts().contains(post)) {
            throw new IllegalArgumentException("Post is not saved by the user.");
        }

        user.getSavedPosts().remove(post);
        userRepository.save(user);
    }

    @Override
    public Integer getPostCount(Integer userId) {

        return postRepository.countByUserUserId(userId);
    }
}
