package com.social.campus.service;

import com.social.campus.entity.User;
import com.social.campus.payload.UserDto;
import jakarta.transaction.UserTransaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDto signUpUser(UserDto userDto);

    UserDto updateUserProfile(UserDto userDto,Integer userId);

    UserDto deleteUserProfile(Integer userId);

    UserDto getUserById(Integer userId);

    UserDto blockUser(Integer userId, Integer blockUserId);

    void unblockUser(Integer userId, Integer unblockUserId);

    List<UserDto> getAllUsers();

    UserDto followUser(Integer userId,Integer followUserId);

    UserDto acceptFollowRequest(Integer userId, Integer requestUserId);

    List<UserDto> getPendingFollowRequests(Integer userId);

    UserDto unfollowUser(Integer userId,Integer unfollowUserId);

    Integer getFollowersCount(Integer userId);

    Integer getFollowingCount(Integer userId);

    List<UserDto> getFollowersList (Integer userId);

    List<UserDto> getFollowingList (Integer userId);

    UserDto uploadProfilePicture (Integer userId, MultipartFile profileImage);

    List<UserDto> getBlockedUsers(Integer userId);

    List<UserDto> searchUserByUsername(String username);
}
