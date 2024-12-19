package com.social.campus.controller;

import com.social.campus.entity.User;
import com.social.campus.payload.ApiResponse;
import com.social.campus.payload.UserDto;
import com.social.campus.response.ResponseHandler;
import com.social.campus.service.UserService;
import lombok.val;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    /**
     * 1. Sign up a user (Create User)
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> signUpUser(@RequestBody UserDto request) {
        UserDto userDto = this.userService.signUpUser(request);

        return ResponseHandler.responseBuilder("User created successfully",HttpStatus.OK,userDto);
    }
    /**
     * 2. Update user profile
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUserProfile(@val @RequestBody UserDto userDto, @PathVariable("id") Integer id){
        UserDto updateUser = this.userService.updateUserProfile(userDto, id);
        return ResponseHandler.responseBuilder("User updated successfully", HttpStatus.OK,updateUser);
    }

    /**
     * 3. Delete user profile
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserProfile(@val @PathVariable Integer id) {
        UserDto deletedUser = this.userService.deleteUserProfile(id);
        return ResponseEntity.ok(new ApiResponse("User Account deleted successfully",true));
    }

    /**
     * 4. Get user by Id
     */
    @GetMapping("/getSingleUser/{id}")
    public ResponseEntity<Object> getUserById(@val @PathVariable Integer id){
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    /**
     * 5. Get All user
     */

    @GetMapping("/getAllUsers")
    public ResponseEntity<Object>getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    /**
     * 6. Follow request sent
     */
    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<ApiResponse> followUser(
            @PathVariable Integer userId,
            @PathVariable Integer followUserId
    ) {
        this.userService.followUser(userId, followUserId);
        return ResponseEntity.ok(new ApiResponse("Follow request sent successfully", true));
    }
    /**
     * 7. Follow request accept
     */
    @PostMapping("/{userId}/accept-follow/{requestUserId}")
    public ResponseEntity<ApiResponse> acceptFollowRequest(
            @PathVariable Integer userId,
            @PathVariable Integer requestUserId
    ) {
        this.userService.acceptFollowRequest(userId, requestUserId);
        return ResponseEntity.ok(new ApiResponse("Follow request accepted successfully", true));
    }
    /**
     * 8. Get pending follow request
     */
    @GetMapping("/{userId}/pending-requests")
    public ResponseEntity<Object> getPendingFollowRequests(@PathVariable Integer userId) {
        List<UserDto> pendingRequests = userService.getPendingFollowRequests(userId);
        return ResponseHandler.responseBuilder(
                "Pending follow requests fetched successfully",
                HttpStatus.OK,
                pendingRequests
        );
    }
    /**
     * 9. Unfollow user
     */
    @DeleteMapping("/{userId}/unfollow/{unfollowUserId}")
    public ResponseEntity<ApiResponse> unfollowUser(
            @PathVariable Integer userId,
            @PathVariable Integer unfollowUserId
    ) {
        UserDto updatedUser = userService.unfollowUser(userId, unfollowUserId);
        return ResponseEntity.ok(new ApiResponse("User unfollow successfully....",true));
    }

    /**
     * 10. Followers Count
     */
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Object> getFollowersCount(@PathVariable Integer userId) {
        Integer count = this.userService.getFollowersCount(userId);
        return ResponseHandler.responseBuilder("Follower count fetched successfully", HttpStatus.OK,count);
    }
    /**
     * 11. Following Count
     */
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Object> getFollowingCount(@PathVariable Integer userId) {
        Integer count = this.userService.getFollowingCount(userId);
        return ResponseHandler.responseBuilder("Following count fetched successfully",HttpStatus.OK,count);
    }
    /**
     * 12. Get Followers List
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserDto>> getFollowersList(@PathVariable Integer userId){
        List<UserDto> followers = userService.getFollowersList(userId);
        return ResponseEntity.ok(followers);
    }
    /**
     * 13. Get Following List
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserDto>> getFollowingList(@PathVariable Integer userId){
        List<UserDto> following =userService.getFollowingList(userId);
        return ResponseEntity.ok(following);
    }

    /**
     * 14. Upload profile picture
     */
    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<UserDto> uploadProfilePicture(
            @PathVariable Integer userId,
            @RequestParam("profileImage") MultipartFile profileImage
    ) {
        UserDto updatedUser = userService.uploadProfilePicture(userId, profileImage);
        return ResponseEntity.ok(updatedUser);
    }
    /**
     * 15. Blocked user
     */
    @PostMapping("/{userId}/block/{blockUserId}")
    public ResponseEntity<ApiResponse> blockUser(
            @PathVariable Integer userId,
            @PathVariable Integer blockUserId
    ) {
        userService.blockUser(userId, blockUserId);
        return ResponseEntity.ok(new ApiResponse("User blocked successfully.", true));
    }

    /**
     * 16. unBlocked user
     */
    // Unblock a user
    @DeleteMapping("/{userId}/unblock/{unblockUserId}")
    public ResponseEntity<ApiResponse> unblockUser(
            @PathVariable Integer userId,
            @PathVariable Integer unblockUserId
    ) {
        userService.unblockUser(userId, unblockUserId);
        return ResponseEntity.ok(new ApiResponse("User unblocked successfully.", true));
    }
    /**
     * 17. get unBlocked user list
     */
    @GetMapping("/{userId}/blocked")
    public ResponseEntity<List<UserDto>> getBlockedUsers(@PathVariable Integer userId) {
        List<UserDto> blockedUsers = userService.getBlockedUsers(userId);
        return ResponseEntity.ok(blockedUsers);
    }
    /**
     * 18. search user
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUserByUsername(@RequestParam String username) {
        List<UserDto> users = userService.searchUserByUsername(username);
        return ResponseEntity.ok(users);
    }
}


