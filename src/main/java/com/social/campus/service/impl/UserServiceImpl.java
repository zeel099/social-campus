package com.social.campus.service.impl;

import com.social.campus.entity.User;
import com.social.campus.exception.FileStorageException;
import com.social.campus.exception.ResourceNotFoundException;
import com.social.campus.exception.ResourceNotFoundExceptionString;
import com.social.campus.payload.UserDto;
import com.social.campus.repository.UserRepository;
import com.social.campus.service.UserService;
import jakarta.transaction.Transactional;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final String uploadDir = "uploads/profile-pictures/";

    @Override
    public UserDto signUpUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto,User.class);
        user.setCreatedAt(LocalDateTime.now());
        User newUser = this.userRepository.save(user);
        return this.modelMapper.map(newUser,UserDto.class);
    }

    @Override
    public UserDto updateUserProfile(UserDto userDto, Integer userId) {

        User user = this.userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","userId",userId));
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCourse(userDto.getCourse());
        user.setBio(userDto.getBio());
        user.setUpdatedAt(LocalDateTime.now());
        User updateUser = this.userRepository.save(user);

        return this.modelMapper.map(updateUser,UserDto.class);
    }

    @Override
    public UserDto deleteUserProfile(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        this.userRepository.delete(user);
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User>users = this.userRepository.findAll();

        List<UserDto>userDtos = users.stream().map((user) -> this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    @Transactional
    public UserDto followUser(Integer userId, Integer followUserId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        User followUser = this.userRepository.findById(followUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "followUserId", followUserId));

        if (user.getFollowing().contains(followUser)) {

            throw new IllegalStateException("User with ID " + userId + " is already following User with ID " + followUserId);
        }
        if (followUser.getPendingFollowers().contains(user)) {
            throw new IllegalStateException("Follow request already sent to User with ID " + followUserId);
        }

        followUser.getPendingFollowers().add(user);
        this.userRepository.save(followUser);

        return this.modelMapper.map(followUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto acceptFollowRequest(Integer userId, Integer requestUserId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        User requestUser = this.userRepository.findById(requestUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "requestUserId", requestUserId));

        // Ensure the request exists
        if (!user.getPendingFollowers().contains(requestUser)) {
            throw new IllegalStateException("No follow request from User with ID " + requestUserId);
        }

        // Accept request
        user.getPendingFollowers().remove(requestUser);
        user.getFollowers().add(requestUser);
        requestUser.getFollowing().add(user);

        this.userRepository.save(user);
        this.userRepository.save(requestUser);

        return this.modelMapper.map(user, UserDto.class);
    }


    @Override
    public List<UserDto> getPendingFollowRequests(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<User> pendingRequests = user.getPendingFollowers().stream().toList();
        return pendingRequests.stream()
                .map(pendingUser -> this.modelMapper.map(pendingUser, UserDto.class))
                .toList();
    }

    @Override
    public UserDto unfollowUser(Integer userId, Integer unfollowUserId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        User unfollowUser = this.userRepository.findById(unfollowUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "unfollowUserId", unfollowUserId));

        if (!user.getFollowing().contains(unfollowUser)) {
            throw new IllegalStateException("User with ID " + userId + " is not following User with ID " + unfollowUserId);
        }
        user.getFollowing().remove(unfollowUser);
        unfollowUser.getFollowers().remove(user);

        this.userRepository.save(user);
        this.userRepository.save(unfollowUser);

        return this.modelMapper.map(unfollowUser, UserDto.class);
    }

    @Override
    public Integer getFollowersCount(Integer userId) {
      return this.userRepository.countFollowersByUserId(userId);
    }

    @Override
    public Integer getFollowingCount(Integer userId) {
        return this.userRepository.countFollowingByUserId(userId);
    }

    @Override
    public List<UserDto> getFollowersList(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        return user.getFollowers()
                .stream()
                .map(follower -> modelMapper.map(follower,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getFollowingList(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        return user.getFollowing()
                .stream()
                .map(following -> modelMapper.map(following,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto uploadProfilePicture(Integer userId, MultipartFile profileImage){
        try {
            User user = this.userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

            Files.createDirectories(Paths.get(uploadDir));

            String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();

            String filePath = uploadDir + fileName;
            profileImage.transferTo(new File(filePath));

            user.setProfilePictureUrl(filePath);
            User updatedUser = this.userRepository.save(user);

            return modelMapper.map(updatedUser, UserDto.class);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + profileImage.getOriginalFilename(), ex);
        }
    }
    @Override
    public UserDto blockUser(Integer userId, Integer blockUserId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        User blockUser = this.userRepository.findById(blockUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "blockUserId", blockUserId));

        // Check if the user is already blocked
        if (user.getBlockedUsers().contains(blockUser)) {
            throw new IllegalStateException("User with ID " + blockUserId + " is already blocked by User with ID " + userId);
        }

        // Add to the blocked list
        user.getBlockedUsers().add(blockUser);

        // Remove from follower and following lists
        user.getFollowers().remove(blockUser);
        user.getFollowing().remove(blockUser);

        blockUser.getFollowers().remove(user);
        blockUser.getFollowing().remove(user);

        // Save the changes
        this.userRepository.save(user);
        this.userRepository.save(blockUser);

        return this.modelMapper.map(user, UserDto.class);
    }


    @Override
    public void unblockUser(Integer userId, Integer unblockUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        User unblockUser = userRepository.findById(unblockUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "unblockUserId", unblockUserId));

        if (!user.getBlockedUsers().contains(unblockUser)) {
            throw new IllegalStateException("User is not blocked");
        }

        user.getBlockedUsers().remove(unblockUser);
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getBlockedUsers(Integer userId) {
        List<User> blockedUsers = userRepository.findBlockedUsersByUserId(userId);
        return blockedUsers.stream()
                .map(blockedUser -> modelMapper.map(blockedUser, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUserByUsername(String username) {
        List<User> users = userRepository.findByUserNameContainingIgnoreCase(username);
        if (users.isEmpty()) {
            throw new ResourceNotFoundExceptionString("User", "username", username);
        }
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
