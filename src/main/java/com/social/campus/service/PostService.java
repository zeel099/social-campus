package com.social.campus.service;


import com.social.campus.payload.PostDto;
import com.social.campus.payload.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto,Integer userId);

    PostDto updatePost(PostDto postDto,Integer postId);

    PostDto deletePost(Integer postId);

    PostDto getPostById(Integer postId);

    List<PostDto> getAllPost();

    void likePost(Integer userId,Integer postId);

    void unlikePost(Integer userId,Integer postId);

    Integer likesCount(Integer postId);

    List<UserDto> getUsersWhoLikedPost(Integer postId);

    void uploadMedia(Integer postId, MultipartFile media);

    void deleteMedia(Integer postId, Integer mediaId);

    List<PostDto> fetchPostsByTag(String tag, Integer userId);

    void savePost(Integer postId,Integer userId);

    void unSavePost(Integer postId,Integer userId);

    Integer getPostCount(Integer userId);
}
